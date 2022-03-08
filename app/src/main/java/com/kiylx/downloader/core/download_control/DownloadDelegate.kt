package com.kiylx.downloader.core.download_control

import com.kiylx.download_module.Downloads
import com.kiylx.librarykit.tools.live_data_bus.core.BusCore

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

        fun getDefaultEventBus():BusCore{
            return InitDownloadConfig.getInstance().dataBus
        }
    }
}