package com.kiylx.downloader.ui.fragments.finish

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.R
import com.kiylx.downloader.kits.ViewHolders
import com.kiylx.librarykit.tools.adapter.HolderInfo
import com.kiylx.librarykit.tools.adapter.SimpleHolder

class FinishInfoViewHolder(item: View, holderInfo: HolderInfo = ViewHolders.UNKNOWN) :
    SimpleHolder(item, holderInfo) {

    val urlView: TextView = item.findViewById(R.id.info_url_file)
    val titleView: TextView = item.findViewById(R.id.info_file_name)
    val totalBytesView: TextView = item.findViewById(R.id.info_file_size)
    val msgView:TextView=item.findViewById(R.id.info_msg)

    val infoMoreView: ImageButton = item.findViewById(R.id.info_more)//更多信息按钮

    fun bindData(data: SimpleDownloadInfo) {
        data.run {
            titleView.text = name
            urlView.text=url
            totalBytesView.text = fileSize.toString()
            msgView.text=finalMsg
        }
    }
}