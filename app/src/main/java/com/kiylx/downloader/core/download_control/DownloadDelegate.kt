package com.kiylx.downloader.core.download_control

import com.kiylx.download_module.Downloads
import com.kiylx.librarykit.tools.live_data_bus.core.BusCore

class DownloadDelegate {
    companion object {
        fun getDownloads(): Downloads {
            return InitConfigs.get().downloads
        }

        fun getDownloadConfig(): InitConfigs {
            return InitConfigs.get()
        }

        fun getDefaultEventBus():BusCore{
            return InitConfigs.get().dataBus
        }
    }
}