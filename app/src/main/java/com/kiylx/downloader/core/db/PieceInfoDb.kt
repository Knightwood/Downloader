package com.kiylx.downloader.core.db

import androidx.room.*
import com.kiylx.download_module.lib_core.model.PieceInfo
import com.kiylx.downloader.MyApplication
import com.kiylx.downloader.core.db.other.DownloadBean
import com.kiylx.downloader.core.db.other.PieceBean
import kotlinx.coroutines.flow.Flow

@Database(entities = [DownloadBean::class], version = 1)
abstract class PieceInfoDb : RoomDatabase() {
    abstract fun pieceInfoDao(): PieceInfoDao

    companion object {
        private val pieceDownloadInfoDb = Room.databaseBuilder(
            MyApplication.myApplication(),
            PieceInfoDb::class.java,
            "pieceInfoDatabase"
        ).build()

        fun dbInstance(): PieceInfoDao {
            return pieceDownloadInfoDb.pieceInfoDao()
        }
    }
}

@Dao
interface PieceInfoDao {
    @Query("SELECT * FROM PieceBean")
    fun getAll(): Flow<List<PieceBean>>

    @Query("SELECT * FROM PieceBean WHERE uuid = :uuid")
    fun queryInfoById(uuid: String): List<PieceBean>

    @Query("DELETE FROM PieceBean WHERE uuid = :uuid")
    fun deleteInfo(uuid: String)
    @Insert
    fun insert(info: PieceInfo)
    @Update
    fun update(info: PieceInfo)

}