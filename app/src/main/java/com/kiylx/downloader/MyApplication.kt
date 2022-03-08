package com.kiylx.downloader

import android.app.Application
import com.kiylx.downloader.core.download_control.InitDownloadConfig

class MyApplication : Application() {

   private lateinit var  initDownloadConfig :InitDownloadConfig

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initDownloadConfig= InitDownloadConfig.getInstance()
    }

    companion object {
        var mContext: Application? = null

        fun myApplicationContext(): Application {
            return mContext!!
        }
    }
}