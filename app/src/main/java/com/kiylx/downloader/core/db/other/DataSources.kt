package com.kiylx.downloader.core.db.other

import androidx.room.Room
import com.kiylx.downloader.MyApplication.Companion.myApplication
import com.kiylx.downloader.core.db.DownloadInfoDb
import com.kiylx.downloader.core.db.PieceInfoDb
import java.util.*

class DataSources private constructor() {
    private val downloadInfoDb = Room.databaseBuilder(
        myApplication(),
        DownloadInfoDb::class.java,
        "downloadInfoDatabase"
    ).build()

    private val pieceDownloadInfoDb = Room.databaseBuilder(
        myApplication(),
        PieceInfoDb::class.java,
        "pieceInfoDatabase"
    ).build()

    val downloadInfoDao = downloadInfoDb.downloadInfoDao()
    val pieceInfoDao = pieceDownloadInfoDb.pieceInfoDao()


    fun deleteDownloadRecord(id: UUID?) {
        id?.let {
            downloadInfoDao.deleteInfo(it.toString())
            pieceInfoDao.deleteInfo(it.toString())
        }

    }

    companion object {
        val dataSources: DataSources by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DataSources() }
    }
}