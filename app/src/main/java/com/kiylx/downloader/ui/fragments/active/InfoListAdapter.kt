package com.kiylx.downloader.ui.fragments.active

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.librarykit.tools.adapter.SimpleHolder
import com.kiylx.librarykit.tools.adapter.SimpleListAdapter

@Deprecated("no used")
class InfoListAdapter(onItemClicked: (DownloadInfo) -> Unit) : SimpleListAdapter<DownloadInfo>(
    DiffCallback,
    onItemClicked
) {
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DownloadInfo>() {
            override fun areItemsTheSame(
                oldItem: DownloadInfo,
                newItem: DownloadInfo
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: DownloadInfo,
                newItem: DownloadInfo
            ): Boolean {
                TODO("Not yet implemented")
            }

        }
    }
}