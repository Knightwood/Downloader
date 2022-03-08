package com.kiylx.downloader.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.core.download_control.DownloadDelegate.Companion.getInfoSources
import java.util.*

class FragmentViewModel : ViewModel() {

    fun getActiveWaitList(): MutableLiveData<MutableList<SimpleDownloadInfo>> =
        getInfoSources().mainInfos

    fun getFinishList(): MutableLiveData<MutableList<SimpleDownloadInfo>> =
        getInfoSources().finishInfos

    /**
     * 返回infoId指定的SimpleDownloadInfo
     */
    fun getInfo(infoId: UUID?): SimpleDownloadInfo? {
        val result = getActiveWaitList().value?.run {
             find { it.id == infoId }
        }
        return result
    }
}