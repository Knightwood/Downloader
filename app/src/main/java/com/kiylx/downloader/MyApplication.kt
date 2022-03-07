package com.kiylx.downloader

import android.app.Application
import android.content.Context
import com.kiylx.download_module.Downloads
import com.kiylx.downloader.download_control.InitDownloadConfig

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