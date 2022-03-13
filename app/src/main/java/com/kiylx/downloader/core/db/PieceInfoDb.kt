package com.kiylx.downloader.core.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import com.kiylx.downloader.core.db.other.DownloadBean
import com.kiylx.downloader.core.db.other.PieceInfo
import kotlinx.coroutines.flow.Flow

@Database(entities = [DownloadBean::class], version = 1)
abstract class PieceInfoDb : RoomDatabase() {
    abstract fun pieceInfoDao(): PieceInfoDao
}

@Dao
interface PieceInfoDao {
    @Query("SELECT * FROM PieceInfo")
    fun getAll(): Flow<List<PieceInfo>>

    @Query("DELETE FROM PieceInfo WHERE uuid = :uuid")
    fun deleteInfo(uuid: String)
}