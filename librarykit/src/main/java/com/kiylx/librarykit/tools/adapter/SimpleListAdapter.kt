package com.kiylx.librarykit.tools.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class SimpleListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onItemClicked: (T) -> Unit
) : ListAdapter<T, SimpleHolder>(diffCallback) {
    var myClick: MyClickListener? = null

    fun setMyClickListener(listener: MyClickListener) {
        this.myClick = listener
    }
}