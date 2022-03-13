package com.kiylx.downloader.core.db

import androidx.room.*
import com.kiylx.downloader.core.db.other.BeanTypeConverters
import com.kiylx.downloader.core.db.other.DownloadBean
import kotlinx.coroutines.flow.Flow

@Database(entities = [DownloadBean::class], version = 1)
@TypeConverters(BeanTypeConverters::class)
abstract class DownloadInfoDb : RoomDatabase() {
    abstract fun downloadInfoDao(): DownloadInfoDao
}

@Dao
interface DownloadInfoDao {
    @Query("SELECT * FROM DownloadBean")
    fun getAll(): Flow<List<DownloadBean>>

    @Query("SELECT * FROM DownloadBean WHERE uuid = :id")
    fun getDownloads(id: String): DownloadBean

    @Query("SELECT * FROM DownloadBean WHERE lifeCycle BETWEEN :lifeCycleStart AND :lifeCycleEnd")
    fun getDownloads(lifeCycleStart: Int, lifeCycleEnd: Int): Flow<List<DownloadBean>>

    @Insert
    fun insertInfo(info: DownloadBean)

    @Update
    fun updateInfo(info: DownloadBean)

    @Delete
    fun deleteInfo(info: DownloadBean)

    @Query("DELETE FROM DownloadBean WHERE uuid = :uuid")
    fun deleteInfo(uuid: String)

}