package com.kiylx.downloader.core.store

import com.kiylx.download_module.lib_core.interfaces.Repo
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.download_module.lib_core.model.HeaderStore
import com.kiylx.download_module.lib_core.model.PieceInfo
import com.kiylx.download_module.view.SimpleDownloadInfo
import java.util.*

class RepoImpl:Repo {
    override fun saveInfo(info: DownloadInfo?) {
        TODO("Not yet implemented")
    }

    override fun deleteInfo(id: UUID?) {
        TODO("Not yet implemented")
    }

    override fun queryInfoById(id: UUID?): DownloadInfo {
        TODO("Not yet implemented")
    }

    override fun queryInfo(info: DownloadInfo?): DownloadInfo {
        TODO("Not yet implemented")
    }

    override fun syncInfoToDisk(info: DownloadInfo?, action: Repo.SyncAction?) {
        TODO("Not yet implemented")
    }

    override fun updateHeader(uuid: UUID?, kind: String?, value: String?) {
        TODO("Not yet implemented")
    }

    override fun getHeadersById(uuid: UUID?, vararg exclude: String?): Array<HeaderStore> {
        TODO("Not yet implemented")
    }

    override fun getHeadersByName(uuid: UUID?, vararg include: String?): Array<HeaderStore> {
        TODO("Not yet implemented")
    }

    override fun queryPieceInfo(uuid: UUID?): MutableList<PieceInfo> {
        TODO("Not yet implemented")
    }

    override fun deletePieceInfo(uuid: UUID?): Boolean {
        TODO("Not yet implemented")
    }

    override fun syncPieceInfoToDisk(info: PieceInfo?, action: Repo.SyncAction?) {
        TODO("Not yet implemented")
    }

    override fun queryList(kind: Int): MutableList<SimpleDownloadInfo> {
        TODO("Not yet implemented")
    }
}