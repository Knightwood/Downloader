package com.kiylx.downloader.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kiylx.downloader.R
import com.kiylx.downloader.core.db.other.DownloadBean
import com.kiylx.downloader.core.download_control.DownloadDelegate
import com.kiylx.downloader.kits.ConstRes
import com.kiylx.librarykit.tools.live_data_bus.core.OstensibleObserver

class DownloadTaskDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_task_detail)
        DownloadDelegate.getDefaultEventBus().get<DownloadBean>(ConstRes.downloadDetailChannelName)
            .observeChannel(this , object :OstensibleObserver<DownloadBean>(){
                override fun onChanged(t: DownloadBean?) {
                    TODO("Not yet implemented")
                }
            })

    }
}