package com.kiylx.downloader.core.download_control.viewSources

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.download_module.lib_core.model.TaskLifecycle
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.download_module.view.UpdateStatus
import com.kiylx.download_module.view.ViewObserver
import com.kiylx.download_module.view.ViewSources

object ViewObserverImpl : ViewObserver {
    val viewSources: ViewSources = ViewSources.viewSourcesInstance
    val mainInfos: MutableLiveData<List<SimpleDownloadInfo>> =
        MutableLiveData()//包括正在下载和等待下载的条目
    val finishInfos: MutableLiveData<List<SimpleDownloadInfo>> = MutableLiveData()

    override fun notifyViewUpdate(
        info: DownloadInfo?,
        lifeCycle: TaskLifecycle?,
        updateStatus: UpdateStatus
    ) {
        mainInfos.value = viewSources.mainInfos
        finishInfos.value = viewSources.finishInfos
    }

}