package com.kiylx.downloader.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.download_control.DownloadDelegate.Companion.getInfoSources

class FragmentViewModel : ViewModel() {
   fun getActiveList():MutableLiveData<MutableList<SimpleDownloadInfo>> = getInfoSources().mainInfos
   fun getFinishList():MutableLiveData<MutableList<SimpleDownloadInfo>> = getInfoSources().finishInfos
}