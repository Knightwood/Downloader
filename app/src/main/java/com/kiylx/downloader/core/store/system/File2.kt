package com.kiylx.downloader.core.store.system;

import android.content.Context
import android.net.Uri
import com.kiylx.download_module.file_platform.FDFile
import com.kiylx.downloader.core.store.system.fd.FileDescriptorWrapperImpl


fun getFakeFile(context: Context, uri: Uri): FDFile {
    val fdw =
        FileDescriptorWrapperImpl(
            context,
            uri
        )
    return FDFile(fdw.open("rw"))
}