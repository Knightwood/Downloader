package com.kiylx.downloader.ui.fragments

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kiylx.download_module.lib_core.model.TaskLifecycle
import com.kiylx.download_module.lib_core.model.TaskLifecycle.CANCEL
import com.kiylx.download_module.lib_core.model.TaskLifecycle.dec
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.MyApplication.Companion.myApplication
import com.kiylx.downloader.core.db.other.DataSources
import com.kiylx.downloader.core.db.other.genSimpleDownloadInfo
import com.kiylx.downloader.core.download_control.DownloadDelegate.Companion.getDownloads
import com.kiylx.downloader.core.download_control.viewSources.ViewObserverImpl
import com.kiylx.librarykit.store.system.systemkits.SystemFacadeHelper
import com.kiylx.librarykit.toolslib.copyText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.*

class FragmentViewModel : ViewModel() {
    private val downloads = getDownloads()

    fun getMainList(): MutableLiveData<List<SimpleDownloadInfo>> = DataSources.getInstance.getMainList()

    fun getFinishList(): MutableLiveData<List<SimpleDownloadInfo>> =DataSources.getInstance.getFinishList()

    /**
     * 返回infoId指定的DownloadBean
     */
    suspend fun getInfo(infoId: UUID): SimpleDownloadInfo {
        return DataSources.getInstance.downloadInfoDao
            .getDownloads(infoId.toString()).genSimpleDownloadInfo()
    }

    fun cancelDownload(data: SimpleDownloadInfo) {
        downloads.cancelTask(data.uuid)
    }

    fun deleteDownload(data: SimpleDownloadInfo) {
        downloads.cancelTask(data.uuid)
        deleteFile(data.filePath)
        deleteDownloadRecord(data.uuid)
    }

    fun shareDownloadedFile(data: SimpleDownloadInfo) {
        TODO("Not yet implemented")
    }

    fun copyDownloadUrl(data: SimpleDownloadInfo) = copyText(myApplication(), "url", data.url)

    private fun deleteDownloadRecord(id: UUID?) = DataSources.getInstance.deleteDownloadRecord(id)

    private fun deleteFile(filePath: String) {
        SystemFacadeHelper.getFileSystemFacade(myApplication()).deleteFile(Uri.parse(filePath))
    }
}