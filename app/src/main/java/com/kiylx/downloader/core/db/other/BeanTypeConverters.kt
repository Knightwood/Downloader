package com.kiylx.downloader.core.db.other

import androidx.room.TypeConverter
import java.util.*

class BeanTypeConverters {
    @TypeConverter
    fun fromUUID(id: UUID): String {
        return id.toString()
    }

    @TypeConverter
    fun toUUID(id: String): UUID {
        return UUID.fromString(id)
    }

    @TypeConverter
    fun fromLongArray(longArray: Array<Long?>): String {
        val sb = StringBuilder()
        longArray.forEach { num ->
            if (num == null) {
                sb.append("*")
            } else {
                val tmp = num.toString()
                sb.append(tmp)
            }
            sb.append("-")
        }
        return sb.toString()
    }

    @TypeConverter
    fun toLongArray(longString: String): Array<Long?> {
        val strArr = longString.split("-")
        val arr: Array<Long?> = arrayOfNulls(strArr.size)
        var index = 0
        for (i in strArr) {
            if (i != "*") {
                arr[index] = i.toLong()
            }
            index ++
        }
        return arr
    }
}