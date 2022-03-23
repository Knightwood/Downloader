package com.kiylx.downloader.core.store

import com.kiylx.download_module.DownloadsListKind.Companion.active_kind
import com.kiylx.download_module.DownloadsListKind.Companion.finish_kind
import com.kiylx.download_module.DownloadsListKind.Companion.frozen_kind
import com.kiylx.download_module.DownloadsListKind.Companion.wait_kind
import com.kiylx.download_module.lib_core.interfaces.Repo
import com.kiylx.download_module.lib_core.interfaces.Repo.SyncAction.*
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.download_module.lib_core.model.HeaderStore
import com.kiylx.download_module.lib_core.model.PieceInfo
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.download_module.view.ViewSources
import com.kiylx.downloader.core.db.other.DataSources
import com.kiylx.downloader.core.db.other.convert
import java.util.*

/**
 * 此类，用于downloadEngine内部调用。外部不该使用它
 */
class RepoImpl : Repo {
    private val dataSources: DataSources = DataSources.getInstance
    private val views: ViewSources = dataSources.viewSources

    override fun saveInfo(info: DownloadInfo) {
        dataSources.downloadInfoDao.insertInfo(info.convert())
    }

    override fun deleteInfo(id: UUID?) {
        dataSources.deleteDownloadRecord(id)
    }

    override fun queryInfoById(id: UUID?): DownloadInfo {
        return dataSources.downloadInfoDao.getDownloads(id.toString()).convert()
    }

    override fun queryInfo(info: DownloadInfo?): DownloadInfo? {
        return if (info == null)
            null
        else
            queryInfoById(info.uuid)
    }

    override fun syncInfoToDisk(info: DownloadInfo, action: Repo.SyncAction) {
        when (action) {
            ADD -> saveInfo(info)
            UPDATE, UPDATE_STATE -> dataSources.downloadInfoDao.updateInfo(info.convert())
            DELETE -> deleteInfo(info.uuid)
        }
    }

    override fun updateHeader(uuid: UUID, kind: String, value: String) {
        dataSources.headerInfoDao.updateHeader(uuid, kind, value)
    }

    override fun getHeadersById(uuid: UUID, vararg exclude: String?): Array<HeaderStore> {
        return dataSources.queryHeadersExclude(uuid = uuid.toString(), exclude = exclude)
    }

    override fun getHeadersByName(uuid: UUID, vararg include: String?): Array<HeaderStore> {
        return dataSources.queryHeadersInclude(uuid = uuid.toString(), include = include)
    }

    override fun queryPieceInfo(uuid: UUID): MutableList<PieceInfo> {
        return dataSources.queryPieceInfoById(uuid.toString())
    }

    override fun deletePieceInfo(uuid: UUID): Boolean {
        dataSources.pieceInfoDao.deleteInfo(uuid.toString())
        return true
    }

    override fun syncPieceInfoToDisk(info: PieceInfo, action: Repo.SyncAction) {
        when (action) {
            ADD -> dataSources.pieceInfoDao.insert(info)
            UPDATE, UPDATE_STATE -> dataSources.pieceInfoDao.update(info)
            DELETE -> dataSources.pieceInfoDao.deleteInfo(info.id.toString())
        }
    }

    override fun queryList(kind: Int): List<SimpleDownloadInfo> {
        when (kind) {
            wait_kind, frozen_kind -> {
                return views.mainInfos.filter {
                    !it.isRunning
                }
            }
            active_kind -> {
                return views.mainInfos.filter {
                    it.isRunning
                }
            }
            finish_kind -> {
                return views.finishInfos
            }
        }
        return emptyList()
    }
}