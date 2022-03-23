package com.kiylx.downloader.core.store

import android.net.Uri
import com.kiylx.download_module.file_platform.FDFile
import com.kiylx.download_module.file_platform.FakeFile
import com.kiylx.download_module.fileskit.FileKit
import com.kiylx.downloader.MyApplication.Companion.myApplicationContext
import com.kiylx.librarykit.store.system.filekits.FileSystemFacade
import com.kiylx.librarykit.store.system.systemkits.SystemFacadeHelper
import java.io.Closeable
import java.io.File
import java.io.FileDescriptor

class FileKitImpl : FileKit<FileDescriptor> {
    private val fileSystemFacadeImpl: FileSystemFacade =
        SystemFacadeHelper.getFileSystemFacade(myApplicationContext())

    /**
     * 这是为了解决遗留设计问题
     * 在下载引擎里，默认使用path类处理文件，但是在这里，当使用android平台的api时，要使用uri和文件名称
     * 因此这里要把包含路径和文件名称的path拆开
     */
    private fun parsePath(path: String): Pair<Uri, String> {
        val result = path.run {
            val pos = lastIndexOf(File.separator)
            val folder = substring(0, pos)
            val name = substring(pos)
            Pair(Uri.parse(folder), name)
        }
        return result
    }

    /**
     * @param isCreateFile 此参数无用
     */
    override fun create(path: String, isCreateFile: Boolean): FakeFile<FileDescriptor> {
        val fd = path.run {
            val tmp = parsePath(path)
            val uri = fileSystemFacadeImpl.createFile(tmp.first, tmp.second, false)
            fileSystemFacadeImpl.getFD(uri)
        }
        return FDFile(fd!!.open("rw"))
    }

    /**
     * 返回此路径表示的文件
     */
    override fun find(path: String): FakeFile<FileDescriptor> {
        val uri = Uri.parse(path)
        val fd = fileSystemFacadeImpl.getFD(uri)
        return FDFile(fd!!.open("rw"))
    }

    override fun isExist(path: String, fileKind: Int): Boolean {
        return fileSystemFacadeImpl.isExist(Uri.fromFile(File(path)))
    }

    /**
     * @param path 文件的路径
     *             删除此路径的文件
     */
    //必须实现
    override fun rmdir(path: String) {
        fileSystemFacadeImpl.deleteFile(Uri.parse(path))
    }

    override fun reName(file: FakeFile<FileDescriptor>, newName: String?): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * @param path 路径
     * @param size 期望存储能至少有这么大的空间
     * @return 如果能存下如此大小的文件，返回true
     */
    //必须实现
    override fun checkSpace(path: String, size: Long): Boolean {
        val result = fileSystemFacadeImpl.getDirAvailableBytes(Uri.parse(path))
        return result > size
    }

    override fun getName(filePath: String): String {
        TODO("Not yet implemented")
    }

    //若实现本类中的getExtension方法，则必须实现此方法
    override fun getName(file: FakeFile<FileDescriptor>): String {
        TODO("Not yet implemented")
    }


    override fun move(path: String, newPath: String, fileKind: Int) {
        TODO("Not yet implemented")
    }

    override fun copy(path: String, newPath: String, fileKind: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getExtension(file: FakeFile<FileDescriptor>): String {
        TODO("Not yet implemented")
    }

    override fun allocate(fd: FileDescriptor, length: Long) {
        TODO("Not yet implemented")
    }

    override fun closeQuietly(closeable: Closeable) {
        TODO("Not yet implemented")
    }

    override fun thisPathIsWhat(path: String?): Int {
        TODO("Not yet implemented")
    }

    override fun getThisPathFile(filePath: String?): FakeFile<*> {
        TODO("Not yet implemented")
    }
}