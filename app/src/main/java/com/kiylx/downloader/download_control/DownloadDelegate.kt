package com.kiylx.downloader.download_control

import com.kiylx.download_module.Downloads
import com.kiylx.download_module.lib_core.model.DownloadInfo

class DownloadDelegate {
    companion object {
        fun getDownloads(): Downloads {
            return InitDownloadConfig.getInstance().downloads
        }

        fun getDownloadConfig(): InitDownloadConfig {
            return InitDownloadConfig.getInstance()
        }

        fun getInfoSources(): DownloadInfoSources {
            return InitDownloadConfig.getInstance().sources
        }
    }
}