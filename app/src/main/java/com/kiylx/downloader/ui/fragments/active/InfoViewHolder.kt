package com.kiylx.downloader.ui.fragments.active

import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.R
import com.kiylx.downloader.ui.ViewHolders
import com.kiylx.librarykit.tools.adapter.HolderInfo
import com.kiylx.librarykit.tools.adapter.SimpleHolder
import com.kiylx.toolslib.caclProgress

class InfoViewHolder(item: View, holderInfo: HolderInfo = ViewHolders.UNKNOWN) :
    SimpleHolder(item, holderInfo) {

    val startAndPause: ImageButton = item.findViewById(R.id.start_pause_button)
    val downloadControl: ImageButton = item.findViewById(R.id.download_control)

    val titleView: TextView = item.findViewById(R.id.download_file_name)
    val progressBar: SeekBar = item.findViewById(R.id.download_progress)
    val currentBytesView: TextView = item.findViewById(R.id.download_current_bytes)
    val totalBytesView: TextView = item.findViewById(R.id.download_total_size)
    val speedView: TextView = item.findViewById(R.id.download_speed)

    fun bindData(data: SimpleDownloadInfo) {
        data.run {
            titleView.text = name
            progressBar.progress = caclProgress(currentLength, fileSize)
            currentBytesView.text = currentLength.toString()
            totalBytesView.text = fileSize.toString()
            speedView.text = speed.toString()
        }
    }
}