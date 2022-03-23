package com.kiylx.downloader.core.store

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.kiylx.downloader.MyApplication.Companion.myApplication
import com.kiylx.librarykit.tools.preferences.PreferenceTools

/**
 * 偏好设置
 */
class PreferenceImpl() {
    companion object{
        const val downloadLimitPref ="downloadLimitPref"
        const val defaultfileSaveInPref ="defaultSaveInPref"

        /*@RequiresApi(Build.VERSION_CODES.Q)
        fun getSavePath():String{
            var path=PreferenceTools.getString(myApplication(), defaultfileSaveInPref,"")
            if (path.isNullOrEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Downloads.getContentUri("external")
                        .path
                   val contentValues =ContentValues()
                    contentValues.put()
                    val downloadPathUri = MediaStore.Downloads.DOWNLOAD_URI
                    path =
                }
            }
            return path
        }*/

    }

}
