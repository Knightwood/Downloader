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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kiylx.librarykit.store.exception.UnknownUriException;
import com.kiylx.librarykit.store.system.fd.FileDescriptorWrapper;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;

public interface FileSystemFacade
{
    void seek(@NonNull FileOutputStream fout, long offset) throws IOException;

    void allocate(@NonNull FileDescriptor fd, long length) throws IOException;

    void closeQuietly(Closeable closeable);

    String makeFilename(@NonNull Uri dir,
                        @NonNull String desiredFileName);

    void moveFile(@NonNull Uri srcDir,
                  @NonNull String srcFileName,
                  @NonNull Uri destDir,
                  @NonNull String destFileName,
                  boolean replace) throws IOException, FileAlreadyExistsException;

    void copyFile(@NonNull Uri srcFile,
                  @NonNull Uri destFile,
                  boolean truncateDestFile) throws IOException;

    FileDescriptorWrapper getFD(@NonNull Uri path);

    String getExtensionSeparator();

    String appendExtension(@NonNull String fileName, @NonNull String mimeType);

    @Nullable
    String getDefaultDownloadPath();

    @Nullable
    String getUserDirPath();

    boolean deleteFile(@NonNull Uri path) throws FileNotFoundException;

    Uri getFileUri(@NonNull Uri dir,
                   @NonNull String fileName);

    Uri getFileUri(@NonNull String relativePath,
                   @NonNull Uri dir);

    boolean fileExists(@NonNull Uri filePath) throws UnknownUriException;

    long lastModified(@NonNull Uri filePath) throws UnknownUriException;

    boolean isStorageWritable();

    boolean isStorageReadable();

    Uri createFile(@NonNull Uri dir,
                   @NonNull String fileName,
                   boolean replace) throws IOException;
    void write(@NonNull byte[] data,
               @NonNull Uri destFile) throws IOException, UnknownUriException;

    void write(@NonNull CharSequence data,
               @NonNull Charset charset,
               @NonNull Uri destFile) throws IOException, UnknownUriException;

    String makeFileSystemPath(@NonNull Uri uri) throws UnknownUriException;

    String makeFileSystemPath(@NonNull Uri uri,
                              String relativePath) throws UnknownUriException;

    File getTempDir(Context appContext);

    void cleanTempDir(Context appContext) throws IOException;

    File makeTempFile(Context appContext,@NonNull String postfix);

    long getDirAvailableBytes(@NonNull Uri dir);

    String getExtension(String fileName);

    boolean isValidFatFilename(String name);

    String buildValidFatFilename(String name);

    String normalizeFileSystemPath(String path);

    String getDirPath(@NonNull Uri dir) throws UnknownUriException;

    String getFilePath(@NonNull Uri filePath) throws UnknownUriException;

    Uri getParentDirUri(@NonNull Uri filePath) throws UnknownUriException;


    String getDirName(@NonNull Uri dir);

    long getFileSize(@NonNull Uri filePath);

    void truncate(@NonNull Uri filePath, long newSize) throws IOException;

    boolean isExist(@NonNull Uri filePathUri);

}
