package com.kiylx.downloader.download_control

import androidx.lifecycle.MutableLiveData
import com.kiylx.download_module.lib_core.interfaces.DownloadResultListener
import com.kiylx.download_module.view.SimpleDownloadInfo

class DownloadInfoSources : DownloadResultListener {
    val activeInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()
    val waitInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()
    val mainInfos:MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()

    val finishInfos: MutableLiveData<MutableList<SimpleDownloadInfo>> = MutableLiveData()

    override fun updatedActive(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
            //if (list.size != activeInfos.value?.size)//如果条目有所增减，更新。其余情况，因为都是引用，内容会自动更新
                activeInfos.value = list
        }
    }

    override fun updatedWait(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
                waitInfos.value = list
        }
    }

    override fun updatedFinish(list: MutableList<SimpleDownloadInfo>?) {
        if (list != null && list.isNotEmpty()) {
                finishInfos.value = list
        }
    }

}