package com.kiylx.downloader.kits

import androidx.recyclerview.widget.DiffUtil.Callback
import com.kiylx.download_module.view.SimpleDownloadInfo

class Differ(
    private var oldList: List<SimpleDownloadInfo> = mutableListOf(),
    private var newList: List<SimpleDownloadInfo> = mutableListOf()
) : Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData: SimpleDownloadInfo = oldList[oldItemPosition]
        val newData: SimpleDownloadInfo = newList[newItemPosition]
        return oldData.uuid == newData.uuid
    }

    //每次更新，数据源会产生不同的list,
    //但除去添加和删除数据之外，更新数据内容时，新旧数据都是对同一个对象的引用，旧数据永远与新数据一致。
    //而每次更新，下载进度是不一样的，所以对于正在下载的任务直接返回false标识更新，不再做额外判断
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //val oldData: SimpleDownloadInfo = oldList[oldItemPosition]
        val newData: SimpleDownloadInfo = newList[newItemPosition]
        return !newData.isRunning
    }
}