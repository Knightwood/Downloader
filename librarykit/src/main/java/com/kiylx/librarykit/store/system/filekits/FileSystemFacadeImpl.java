/*
 * Copyright (C) 2018, 2019 Tachibana General Laboratories, LLC
 * Copyright (C) 2018, 2019, 2021 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of Download Navi.
 *
 * Download Navi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Download Navi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Download Navi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.kiylx.librarykit.store.system.filekits;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kiylx.librarykit.store.exception.UnknownUriException;
import com.kiylx.librarykit.store.system.fd.FileDescriptorWrapper;
import com.kiylx.librarykit.store.system.fsmodule.FsModule;
import com.kiylx.librarykit.store.system.fsmodule.FsModuleResolver;
import com.kiylx.librarykit.store.system.systemcall.SysCall;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.UUID;

/**
 * 对于文件的操作
 */
public class FileSystemFacadeImpl implements FileSystemFacade {
    @SuppressWarnings(value = "unused")
    private static final String TAG = FileSystemFacadeImpl.class.getSimpleName();

    private static final String EXTENSION_SEPARATOR = ".";
    private static final String TEMP_DIR = "temp";
    /* The file copy buffer size (30 MB) */
    private static final long FILE_COPY_BUFFER_SIZE = 1024 * 1024 * 30;

    private SysCall sysCall;//系统调用实现
    private FsModuleResolver fsResolver;//解析得到SafFsModule或者DefaultFsModule

    public FileSystemFacadeImpl(@NonNull SysCall sysCall,
                                @NonNull FsModuleResolver fsResolver) {
        this.sysCall = sysCall;
        this.fsResolver = fsResolver;
    }

    /*
     * See http://man7.org/linux/man-pages/man2/lseek.2.html
     */

    @Override
    public void seek(@NonNull FileOutputStream fout, long offset) throws IOException {
        try {
            sysCall.lseek(fout.getFD(), offset);

        } catch (UnsupportedOperationException e) {
            fout.getChannel().position(offset);
        }
    }

    /*
     * See http://man7.org/linux/man-pages/man3/posix_fallocate.3.html
     */

    @Override
    public void allocate(@NonNull FileDescriptor fd, long length) throws IOException {
        sysCall.fallocate(fd, length);
    }

    @Override
    public void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();

        } catch (final IOException e) {
            /* Ignore */
        }
    }

    /*
     * If file with required name exists returns new filename in the following format:
     *
     *     base_name (count_number).extension
     *
     * otherwise returns original filename
     */

    public String makeFilename(@NonNull Uri dir,
                               @NonNull String desiredFileName) {
        while (true) {
            /* File doesn't exists, return */
            Uri filePath = getFileUri(dir, desiredFileName);
            if (filePath == null)
                return desiredFileName;

            FsModule fsModule = fsResolver.resolveFsByUri(filePath);
            String fileName = fsModule.getName(filePath);
            if (fileName == null)
                fileName = desiredFileName;

            int openBracketPos = fileName.lastIndexOf("(");
            int closeBracketPos = fileName.lastIndexOf(")");

            /* Try to parse the counter number and increment it for a new filename */
            int countNumber;
            if (openBracketPos > 0 && closeBracketPos > 0) {
                try {
                    countNumber = Integer.parseInt(fileName.substring(openBracketPos + 1, closeBracketPos));

                    desiredFileName = fileName.substring(0, openBracketPos + 1) +
                            ++countNumber + fileName.substring(closeBracketPos);
                    continue;

                } catch (NumberFormatException e) {
                    /* Ignore */
                }
            }

            /* Otherwise create a name with the initial value of the counter */
            countNumber = 1;
            int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
            String baseName = (extensionPos < 0 ? fileName : fileName.substring(0, extensionPos));

            StringBuilder sb = new StringBuilder(baseName + " (" + countNumber + ")");
            if (extensionPos > 0)
                sb.append(EXTENSION_SEPARATOR)
                        .append(getExtension(fileName));

            desiredFileName = sb.toString();
        }
    }

    /*
     * Returns Uri and name of moved file.
     */

    @Override
    public void moveFile(@NonNull Uri srcDir,
                         @NonNull String srcFileName,
                         @NonNull Uri destDir,
                         @NonNull String destFileName,
                         boolean replace) throws IOException, FileAlreadyExistsException {
        //fsModule通过new出来的FsModuleResolverImpl类的实例中，通过resolveFsByUri方法获得
        FsModule fsModule;
        Uri srcFileUri, destFileUri;

        fsModule = fsResolver.resolveFsByUri(srcDir);//可能是SafFsModule也可能是DefaultFsModule
        srcFileUri = fsModule.getFileUri(srcDir, srcFileName, false);
        if (srcFileUri == null)
            throw new FileNotFoundException("Source '" + srcFileName + "' from " + srcDir + " does not exists");

        fsModule = fsResolver.resolveFsByUri(destDir);
        destFileUri = fsModule.getFileUri(destDir, destFileName, replace);
        if (!replace && destFileUri != null)
            throw new FileAlreadyExistsException("Destination '" + destFileUri + "' already exists");

        destFileUri = createFile(destDir, destFileName, false);
        if (destFileUri == null)
            throw new IOException("Cannot create destination file '" + destFileName + "'");

        copyFile(srcFileUri, destFileUri, replace);
        deleteFile(srcFileUri);
    }

    /*
     * This caches the original file length, and throws an IOException
     * if the output file length is different from the current input file length.
     * So it may fail if the file changes size.
     * It may also fail with "IllegalArgumentException: Negative size" if the input file is truncated part way
     * through copying the data and the new file size is less than the current position.
     */

    public void copyFile(@NonNull Uri srcFile,
                         @NonNull Uri destFile,
                         boolean truncateDestFile) throws IOException {

        if (srcFile.equals(destFile))
            throw new IllegalArgumentException("Uri points to the same file");

        try (FileDescriptorWrapper wSrc = getFD(srcFile);
             FileDescriptorWrapper wDest = getFD(destFile)) {

            try (FileInputStream fin = new FileInputStream(wSrc.open("r"));
                 FileOutputStream fout = new FileOutputStream(wDest.open((truncateDestFile ? "rwt" : "rw")));
                 FileChannel input = fin.getChannel();
                 FileChannel output = fout.getChannel()) {
                long size = input.size();
                long pos = 0;
                long count;
                while (pos < size) {
                    long remain = size - pos;
                    count = (Math.min(remain, FILE_COPY_BUFFER_SIZE));
                    long bytesCopied = output.transferFrom(input, pos, count);
                    if (bytesCopied == 0)
                        break;
                    pos += bytesCopied;
                }

                long srcLen = input.size();
                long dstLen = output.size();
                if (srcLen != dstLen)
                    throw new IOException("Failed to copy full contents from '" +
                            srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
            }
        }
    }

    @Override
    public FileDescriptorWrapper getFD(@NonNull Uri path) {
        FsModule fsModule = fsResolver.resolveFsByUri(path);

        return fsModule.openFD(path);
    }

    @Override
    public String getExtensionSeparator() {
        return EXTENSION_SEPARATOR;
    }

    @Override
    public String appendExtension(@NonNull String fileName, @NonNull String mimeType) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = getExtension(fileName);

        if (TextUtils.isEmpty(extension)) {
            extension = mimeTypeMap.getExtensionFromMimeType(mimeType);
        } else {
            String m = mimeTypeMap.getMimeTypeFromExtension(extension);
            if (m == null || !m.equals(mimeType))
                extension = mimeTypeMap.getExtensionFromMimeType(mimeType);
        }

        if (extension != null && !fileName.endsWith(extension))
            fileName += getExtensionSeparator() + extension;

        return fileName;
    }

    /*
     * Return path to the standard Download directory.
     * If the directory doesn't exist, the function creates it automatically.
     */

    @Override
    @Nullable
    public String getDefaultDownloadPath() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory())
            return path;
        else
            return dir.mkdirs() ? path : null;
    }

    /*
     * Return the primary shared/external storage directory.
     */

    @Override
    @Nullable
    public String getUserDirPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File dir = new File(path);
        if (dir.exists() && dir.isDirectory())
            return path;
        else
            return dir.mkdirs() ? path : null;
    }

    @Override
    public boolean deleteFile(@NonNull Uri path) throws FileNotFoundException {
        FsModule fsModule = fsResolver.resolveFsByUri(path);

        return fsModule.delete(path);
    }

    /*
     * Returns a file (if exists) Uri by name from the pointed directory
     */

    @Override
    public Uri getFileUri(@NonNull Uri dir,
                          @NonNull String fileName) {
        FsModule fsModule = fsResolver.resolveFsByUri(dir);

        Uri path = null;
        try {
            path = fsModule.getFileUri(dir, fileName, false);

        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return path;
    }

    /*
     * Returns a file (if exists) Uri by relative path (e.g foo/bar.txt)
     * from the pointed directory
     */

    @Override
    public Uri getFileUri(@NonNull String relativePath,
                          @NonNull Uri dir) {
        FsModule fsModule = fsResolver.resolveFsByUri(dir);

        return fsModule.getFileUri(relativePath, dir);
    }

    @Override
    public boolean fileExists(@NonNull Uri filePath) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(filePath);

        return fsModule.fileExists(filePath);
    }

    @Override
    public long lastModified(@NonNull Uri filePath) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(filePath);

        return fsModule.lastModified(filePath);
    }

    /*
     * Checks if main storage is available for read and write.
     */

    @Override
    public boolean isStorageWritable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /*
     * Checks if main storage is available to at least read.
     */

    @Override
    public boolean isStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    /*
     * Returns Uri of created file.
     * Note: if replace == false, doesn't replace file if it exists and returns its Uri.
     */

    @Override
    public Uri createFile(@NonNull Uri dir,
                          @NonNull String fileName,
                          boolean replace) throws IOException {
        FsModule fsModule = fsResolver.resolveFsByUri(dir);
        try {
            Uri path = fsModule.getFileUri(dir, fileName, false);
            if (path != null) {
                if (!replace)
                    return path;
                else if (!fsModule.delete(path))
                    return null;
            }

            return fsModule.getFileUri(dir, fileName, true);

        } catch (SecurityException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(@NonNull byte[] data,
                      @NonNull Uri destFile) throws IOException, UnknownUriException {
        try (FileDescriptorWrapper w = getFD(destFile)) {
            try (FileOutputStream fout = new FileOutputStream(w.open("rw"))) {
                fout.write(data);
                fout.flush();
                //IOUtils.write(data, fout);
            }
        }
    }

    @Override
    public void write(@NonNull CharSequence data,
                      @NonNull Charset charset,
                      @NonNull Uri destFile) throws IOException, UnknownUriException {
        try (FileDescriptorWrapper w = getFD(destFile)) {
            try (FileOutputStream fout = new FileOutputStream(w.open("rw"));
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fout, charset)) {
                String str = String.valueOf(data);
                outputStreamWriter.write(str);
                //IOUtils.write(data, fout, charset);
            }
        }
    }

    /*
     * If the uri is a file system path, returns the path as is
     */

    @Override
    public String makeFileSystemPath(@NonNull Uri uri) throws UnknownUriException {
        return makeFileSystemPath(uri, null);
    }

    @Override
    public String makeFileSystemPath(@NonNull Uri uri,
                                     String relativePath) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(uri);

        return fsModule.makeFileSystemPath(uri, relativePath);
    }

    /*
     * Return the number of bytes that are free on the file system
     * backing the given Uri
     */

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir) {
        long availableBytes = -1;

        FsModule fsModule = fsResolver.resolveFsByUri(dir);
        try {
            availableBytes = fsModule.getDirAvailableBytes(dir);

        } catch (IllegalArgumentException | IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return availableBytes;
        }

        return availableBytes;
    }

    @Override
    public File getTempDir(Context appContext) {
        File tmpDir = new File(appContext.getExternalFilesDir(null), TEMP_DIR);
        if (!tmpDir.exists())
            if (!tmpDir.mkdirs())
                return null;

        return tmpDir;
    }

    /**
     * 方法未实现
     */
    @Deprecated
    @Override
    public void cleanTempDir(Context appContext) throws IOException {
        File tmpDir = getTempDir(appContext);
        if (tmpDir == null)
            throw new FileNotFoundException("Temp dir not found");

        //org.apache.commons.io.FileUtils.cleanDirectory(tmpDir);
    }

    @Override
    public File makeTempFile(Context appContext, @NonNull String postfix) {
        return new File(getTempDir(appContext), UUID.randomUUID().toString() + postfix);
    }

    @Override
    public String getExtension(String fileName) {
        if (fileName == null)
            return null;

        int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = fileName.lastIndexOf(File.separator);
        int index = (lastSeparator > extensionPos ? -1 : extensionPos);

        if (index == -1)
            return "";
        else
            return fileName.substring(index + 1);
    }

    /*
     * Check if given filename is valid for a FAT filesystem
     */

    @Override
    public boolean isValidFatFilename(String name) {
        return name != null && name.equals(buildValidFatFilename(name));
    }

    /*
     * Mutate the given filename to make it valid for a FAT filesystem,
     * replacing any invalid characters with "_"
     */

    @Override
    public String buildValidFatFilename(String name) {
        if (TextUtils.isEmpty(name) || ".".equals(name) || "..".equals(name))
            return "(invalid)";

        final StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            if (isValidFatFilenameChar(c))
                res.append(c);
            else
                res.append('_');
        }
        /*
         * Even though vfat allows 255 UCS-2 chars, we might eventually write to
         * ext4 through a FUSE layer, so use that limit
         */
        trimFilename(res, 255);

        return res.toString();
    }

    private boolean isValidFatFilenameChar(char c) {
        if ((0x00 <= c && c <= 0x1f))
            return false;
        switch (c) {
            case '"':
            case '*':
            case '/':
            case ':':
            case '<':
            case '>':
            case '?':
            case '\\':
            case '|':
            case 0x7F:
                return false;
            default:
                return true;
        }
    }

    private void trimFilename(StringBuilder res, int maxBytes) {
        byte[] raw = res.toString().getBytes(StandardCharsets.UTF_8);
        if (raw.length > maxBytes) {
            maxBytes -= 3;
            while (raw.length > maxBytes) {
                res.deleteCharAt(res.length() / 2);
                raw = res.toString().getBytes(StandardCharsets.UTF_8);
            }
            res.insert(res.length() / 2, "...");
        }
    }
    /*
     * Append file:// scheme for Uri
     */

    @Override
    public String normalizeFileSystemPath(String path) {
        return (TextUtils.isEmpty(path) ||
                path.startsWith("file://") ||
                path.startsWith("content://") ?
                path :
                "file://" + path);
    }

    /*
     * Returns path if the directory belongs to the filesystem
     */

    @Override
    public String getDirPath(@NonNull Uri dir) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(dir);

        return fsModule.getDirName(dir);
    }

    /*
     * Returns path if the file belongs to the filesystem
     */

    @Override
    public String getFilePath(@NonNull Uri filePath) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(filePath);

        return fsModule.getFilePath(filePath);
    }

    @Override
    public Uri getParentDirUri(@NonNull Uri filePath) throws UnknownUriException {
        FsModule fsModule = fsResolver.resolveFsByUri(filePath);

        return fsModule.getParentDirUri(filePath);
    }

    /*
     * Returns path if the directory belongs to the filesystem,
     * otherwise returns SAF name
     */

    @Override
    public String getDirName(@NonNull Uri dir) {
        FsModule fsModule = fsResolver.resolveFsByUri(dir);

        return fsModule.getDirName(dir);
    }

    @Override
    public long getFileSize(@NonNull Uri filePath) {
        FsModule fsModule = fsResolver.resolveFsByUri(filePath);

        return fsModule.getFileSize(filePath);
    }

    @Override
    public void truncate(@NonNull Uri filePath, long newSize) throws IOException {
        try (FileDescriptorWrapper w = getFD(filePath)) {
            FileDescriptor fd = w.open("rw");
            try (FileChannel chan = new FileOutputStream(fd).getChannel()) {
                chan.truncate(newSize);
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePathUri 文件路径
     */
    @Override
    public boolean isExist(@NonNull Uri filePathUri) {
        return fsResolver.resolveFsByUri(filePathUri).fileExists(filePathUri);
    }
}
