package com.kiylx.downloader.ui.fragments.active

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.R
import com.kiylx.downloader.core.download_control.DownloadDelegate.Companion.getDownloads
import com.kiylx.downloader.kits.ViewHolders
import com.kiylx.librarykit.tools.adapter.SimpleAdapter
import com.kiylx.librarykit.tools.adapter.SimpleHolder


class InfoAdapter(var dataLists: MutableList<SimpleDownloadInfo>) :
    SimpleAdapter<SimpleHolder>() {
    private val holders = ViewHolders

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holderView = SimpleHolder(
            layoutInflater.inflate(
                holders.getLayoutId(viewType).itemLayoutId,
                parent,
                false
            ), ViewHolders.DownloadInfoHolder
        )

        holderView.itemView.setOnClickListener{
            //整个条目的点击事件
        }
        (holderView as InfoViewHolder).apply {
            setClickEvent(startAndPause,downloadControl){
                val pos=holderView.adapterPosition
                val data=dataLists[pos]
                when(it.id){
                    R.id.start_pause_button->{
                        data.id?.let { uuid -> getDownloads().pauseDownload(uuid) }
                    }
                }
                myClick?.onClick(it,pos)//点击事件向外传递
            }
        }
        return holderView
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        val data: SimpleDownloadInfo = dataLists[position]
        when (holder.holderInfo.type) {
            ViewHolders.DownloadInfoHolder.type -> {
                (holder as InfoViewHolder).bindData(data)
            }
            ViewHolders.UNKNOWN.type -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ViewHolders.DownloadInfoHolder.type
    }

    override fun getItemCount(): Int = dataLists.size

}