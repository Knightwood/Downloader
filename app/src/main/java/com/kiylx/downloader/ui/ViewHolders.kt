package com.kiylx.downloader.ui

import com.kiylx.downloader.R
import com.kiylx.librarykit.tools.adapter.HolderControls
import com.kiylx.librarykit.tools.adapter.HolderInfo

object ViewHolders : HolderControls() {
    val DownloadInfoHolder: HolderInfo = HolderInfo(0, R.layout.item_downloadinfo)
    val FinsihInfoHolder:HolderInfo=HolderInfo(0, R.layout.item_downloadinfo)
    init {
        begin()
    }

    override fun begin() {
        register(DownloadInfoHolder)
    }

    override fun end() {
        TODO("Not yet implemented")
    }


}