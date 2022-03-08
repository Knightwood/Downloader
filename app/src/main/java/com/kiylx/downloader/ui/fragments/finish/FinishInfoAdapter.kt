package com.kiylx.downloader.ui.fragments.finish

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kiylx.download_module.view.SimpleDownloadInfo
import com.kiylx.downloader.kits.ViewHolders
import com.kiylx.librarykit.tools.adapter.SimpleAdapter
import com.kiylx.librarykit.tools.adapter.SimpleHolder

class FinishInfoAdapter(var dataLists: MutableList<SimpleDownloadInfo>) :
    SimpleAdapter<SimpleHolder>() {
    private val holders = ViewHolders

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holderView = SimpleHolder(
            layoutInflater.inflate(
                holders.getLayoutId(viewType).itemLayoutId,
                parent,
                false
            ), ViewHolders.FinsihInfoHolder
        )

        holderView.itemView.setOnClickListener{
            //整个条目的点击事件
        }
        (holderView as FinishInfoViewHolder).apply {
            setClickEvent(infoMoreView){
                val pos=holderView.adapterPosition
                myClick?.onClick(it,pos)//点击事件向外传递
            }
        }
        return holderView
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        val data: SimpleDownloadInfo = dataLists[position]
        when (holder.holderInfo.type) {
            ViewHolders.FinsihInfoHolder.type -> {
                (holder as FinishInfoViewHolder).bindData(data)
            }
            ViewHolders.UNKNOWN.type -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return ViewHolders.FinsihInfoHolder.type
    }

    override fun getItemCount(): Int = dataLists.size

}