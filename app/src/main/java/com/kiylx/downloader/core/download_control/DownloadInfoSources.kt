package com.kiylx.downloader.core.download_control

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kiylx.download_module.lib_core.interfaces.DownloadResultListener
import com.kiylx.download_module.view.SimpleDownloadInfo

class DownloadInfoSources : DownloadResultListener {
    val activeInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()
    val waitInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()
    val mainInfos: MediatorLiveData<MutableList<SimpleDownloadInfo>> = MediatorLiveData()//包括正在下载和等待下载的条目
    val finishInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()

    init {
        mainInfos.addSource(activeInfos) { activeList ->
            val tmp = mutableListOf<SimpleDownloadInfo>()
            waitInfos.value?.let { tmp.addAll(it) }
            tmp.addAll(activeList)
            mainInfos.value = tmp
        }
    }

    override fun updatedActive(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
            //if (list.size != activeInfos.value?.size)//如果条目有所增减，更新。其余情况，因为都是引用，内容会自动更新
            activeInfos.value = list
        }
    }

    override fun updatedWait(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
            if (list.size != waitInfos.value?.size)//如果条目有所增减，更新。
                waitInfos.value = list
        }
    }

    override fun updatedFinish(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
            if (list.size != finishInfos.value?.size)//如果条目有所增减，更新。
                finishInfos.value = list
        }
    }

}