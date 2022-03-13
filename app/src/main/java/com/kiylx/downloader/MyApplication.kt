package com.kiylx.downloader

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kiylx.downloader.core.download_control.InitConfigs

class MyApplication : Application() {

    private lateinit var initDownloadConfig: InitConfigs

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        mContext = applicationContext
        initDownloadConfig = InitConfigs.get()
    }

    companion object {
        lateinit var mApplication: Application

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun myApplication(): Application {
            return mApplication
        }

        fun myApplicationContext(): Context {
            return mContext
        }
    }
}