package com.kiylx.librarykit.store.fileindexview.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kiylx.librarykit.R
import com.kiylx.librarykit.store.fileindexview.tree.FileInfo
import com.kiylx.librarykit.tools.adapter.HolderControls
import com.kiylx.librarykit.tools.adapter.HolderInfo
import com.kiylx.librarykit.tools.adapter.SimpleAdapter
import com.kiylx.librarykit.tools.adapter.SimpleHolder

class FileListAdapter(var dataLists: MutableList<FileInfo>) : SimpleAdapter<SimpleHolder>() {
    private val holders = ViewHolders

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val holderView = SimpleHolder(
            layoutInflater.inflate(
                holders.getLayoutId(viewType).itemLayoutId,
                parent,
                false
            ),
            ViewHolders.FileInfoHolder//目前只有一种类型，所以没有利用viewType获得其他holder.但以后可能会改版
        )
        holderView.itemView.setOnClickListener {
            //整个条目的点击事件
            val pos = holderView.adapterPosition
            myClick?.onClick(it, pos)//点击事件向外传递
        }
        return holderView
    }

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        val data: FileInfo = dataLists[position]
        when (holder.holderInfo.type) {
            ViewHolders.FileInfoHolder.type -> {
                (holder as InfoViewHolder).bindData(data)
            }
            ViewHolders.UNKNOWN.type -> {}
        }
    }

    /**
     * 只有一种类型，所以固定返回这个类型
     */
    override fun getItemViewType(position: Int): Int {
        return ViewHolders.FileInfoHolder.type
    }

    override fun getItemCount(): Int = dataLists.size

}

/**
 * 单例，存储所有的viewholder类型
 */
object ViewHolders : HolderControls() {
    val FileInfoHolder: HolderInfo = HolderInfo(0, R.layout.file_item)

    init {
        begin()
    }

    override fun begin() {
        register(FileInfoHolder)
    }

    override fun end() {
        TODO("Not yet implemented")
    }
}

class InfoViewHolder(item: View, holderInfo: HolderInfo = ViewHolders.UNKNOWN) :
    SimpleHolder(item, holderInfo) {
    val item_1 = item.findViewById<RelativeLayout>(R.id.item_1)
    val imgView = item.findViewById<ImageView>(R.id.myFileImage)
    val titleView = item.findViewById<TextView>(R.id.myFileName)

    fun bindData(data: FileInfo) {
        imgView.setImageResource(data.imgId)
        titleView.text = data.fileName
    }
}