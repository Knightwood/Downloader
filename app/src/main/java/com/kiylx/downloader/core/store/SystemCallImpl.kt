package com.kiylx.downloader.core.store

import android.system.Os
import android.system.OsConstants
import com.kiylx.download_module.file_platform.system.SysCall
import java.io.FileDescriptor
import java.io.IOException


class SystemCallImpl:SysCall {
    /*
  * Return the number of bytes that are free on the file system
  * backing the given FileDescriptor
  *
  * TODO: maybe there is analog for KitKat?
  */
    override fun availableBytes(fd: FileDescriptor): Long {
        return try {
            val stat = Os.fstatvfs(fd)
            stat.f_bavail * stat.f_bsize
        } catch (e: java.lang.Exception) {
            throw IOException(e)
        }
    }

    override fun checkConnectivity(): Boolean {
        TODO("Not yet implemented")
    }

    override fun fallocate(fd: FileDescriptor, length: Long) {
        try {
            val curSize = Os.fstat(fd).st_size
            val newBytes = length - curSize
            val availBytes = availableBytes(fd)
            if (availBytes < newBytes) throw IOException(
                "Not enough free space; " + newBytes + " requested, " +
                        availBytes + " available"
            )
            Os.posix_fallocate(fd, 0, length)
        } catch (e: Exception) {
            try {
                Os.ftruncate(fd, length)
            } catch (ex: Exception) {
                throw IOException(ex)
            }
        }
    }

    override fun lseek(fd: FileDescriptor, offset: Long) {
       Os.lseek(fd,offset,OsConstants.SEEK_SET)
    }
}