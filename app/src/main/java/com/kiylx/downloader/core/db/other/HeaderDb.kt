package com.kiylx.downloader.core.db.other

import androidx.room.*
import com.kiylx.download_module.lib_core.model.HeaderStore
import com.kiylx.downloader.MyApplication
import java.util.*

@Database(entities = [HeaderBean::class], version = 1)
abstract class HeaderDb : RoomDatabase() {
    abstract fun headerDao(): HeaderDao

    companion object {
        private val headerDb = Room.databaseBuilder(
            MyApplication.myApplication(),
            HeaderDb::class.java,
            "headerDatabase"
        ).build()

        fun dbInstance(): HeaderDao {
            return headerDb.headerDao()
        }
    }
}

@Dao
interface HeaderDao {
    @Query("UPDATE HeaderBean SET headerValue = :value WHERE infoUUID = :uuid AND headerName = :kind")
    fun updateHeader(uuid: UUID, kind: String, value: String)

    @Query("SELECT * FROM HeaderBean WHERE infoUUID=:uuid")
    fun getHeadersById(uuid: String): List<HeaderStore>

}