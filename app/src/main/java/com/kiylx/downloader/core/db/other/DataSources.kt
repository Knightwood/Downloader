package com.kiylx.downloader.core.db.other

import androidx.lifecycle.MutableLiveData
import com.kiylx.download_module.lib_core.model.HeaderStore
import com.kiylx.download_module.lib_core.model.PieceInfo
import com.kiylx.download_module.lib_core.model.TaskLifecycle
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.download_module.view.ViewSources
import com.kiylx.downloader.core.db.DownloadInfoDb
import com.kiylx.downloader.core.db.PieceInfoDb
import com.kiylx.downloader.core.download_control.viewSources.ViewObserverImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.*

/**
 * 引用着数据库和viewSources,集成一些数据读取、处理、删除等方法
 */
class DataSources private constructor() {
    val downloadInfoDao by lazy { DownloadInfoDb.dbInstance() }
    val pieceInfoDao by lazy { PieceInfoDb.dbInstance() }
    val headerInfoDao by lazy { HeaderDb.dbInstance() }
    val viewSources by lazy { ViewSources.viewSourcesInstance }

    fun getMainList(): MutableLiveData<List<SimpleDownloadInfo>> = ViewObserverImpl.mainInfos

    fun getFinishList(): MutableLiveData<List<SimpleDownloadInfo>> = ViewObserverImpl.finishInfos

    fun getMainPageListFromDb(): Flow<List<SimpleDownloadInfo>> =
        DataSources.getInstance.downloadInfoDao
            .getDownloads(
                TaskLifecycle.dec(TaskLifecycle.OH),
                TaskLifecycle.dec(TaskLifecycle.RUNNING)
            )
            .distinctUntilChanged()
            .map {
                val result: List<SimpleDownloadInfo> = it.map { bean ->
                    bean.genSimpleDownloadInfo()
                }
                return@map result
            }

    fun getFinishListFromDb(): Flow<List<SimpleDownloadInfo>> =
        DataSources.getInstance.downloadInfoDao
            .getDownloads(
                TaskLifecycle.dec(TaskLifecycle.SUCCESS),
                TaskLifecycle.dec(TaskLifecycle.FAILED)
            )
            .distinctUntilChanged()
            .map {
                val result: List<SimpleDownloadInfo> = it.map { bean ->
                    bean.genSimpleDownloadInfo()
                }
                return@map result
            }

    fun deleteDownloadRecord(id: UUID?) {
        id?.let {
            downloadInfoDao.deleteInfo(it.toString())
            pieceInfoDao.deleteInfo(it.toString())
        }

    }

    fun queryPieceInfoById(uuid: String): MutableList<PieceInfo> {
        val list = pieceInfoDao.queryInfoById(uuid)
        val result: MutableList<com.kiylx.download_module.lib_core.model.PieceInfo> =
            mutableListOf()
        list.forEach {
            result.add(it.convert())
        }
        return result
    }

    fun queryHeadersExclude(uuid: String, vararg exclude: String?): Array<HeaderStore> {
        val list = headerInfoDao.getHeadersById(uuid)
        val result: MutableList<HeaderStore> = mutableListOf()
        list.forEach {
            if (!exclude.contains(it.name)) {
                result.add(it)
            }
        }
        return result.toTypedArray()
    }

    fun queryHeadersInclude(uuid: String, vararg include: String?): Array<HeaderStore> {
        val list = headerInfoDao.getHeadersById(uuid)
        val result: MutableList<HeaderStore> = mutableListOf()
        list.forEach {
            if (include.contains(it.name)) {
                result.add(it)
            }
        }
        return result.toTypedArray()
    }

    companion object {
        val getInstance: DataSources by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DataSources() }
    }
}