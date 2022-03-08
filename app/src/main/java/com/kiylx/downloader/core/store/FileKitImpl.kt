package com.kiylx.downloader.core.store

import com.kiylx.download_module.file_platform.FDFile
import com.kiylx.download_module.file_platform.FakeFile
import com.kiylx.download_module.fileskit.FileKit
import java.io.Closeable
import java.io.FileDescriptor

class FileKitImpl :FileKit<FDFile> {
    override fun create(path: String?, isCreateFile: Boolean): FakeFile<FDFile> {
        TODO("Not yet implemented")
    }

    override fun find(path: String?): FakeFile<FDFile> {
        TODO("Not yet implemented")
    }

    override fun isExist(path: String?, fileKind: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun `is`(path: String?): Int {
        TODO("Not yet implemented")
    }

    override fun rmdir(path: String?) {
        TODO("Not yet implemented")
    }

    override fun reName(file: FakeFile<FDFile>?, newName: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkSpace(path: String?, size: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun getName(filePath: String?): String {
        TODO("Not yet implemented")
    }

    override fun getName(file: FakeFile<FDFile>?): String {
        TODO("Not yet implemented")
    }

    override fun get(filePath: String?): FakeFile<*> {
        TODO("Not yet implemented")
    }

    override fun move(path: String?, newPath: String?, fileKind: Int) {
        TODO("Not yet implemented")
    }

    override fun copy(path: String?, newPath: String?, fileKind: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getExtension(file: FakeFile<FDFile>?): String {
        TODO("Not yet implemented")
    }

    override fun allocate(fd: FileDescriptor, length: Long) {
        TODO("Not yet implemented")
    }

    override fun closeQuietly(closeable: Closeable?) {
        TODO("Not yet implemented")
    }
}