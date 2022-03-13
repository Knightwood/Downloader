package com.kiylx.librarykit.tools.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class SimpleAdapter<VH : SimpleHolder> : RecyclerView.Adapter<VH>() {
    var myClick: MyClickListener? = null

    fun setMyClickListener(listener: MyClickListener) {
        this.myClick = listener
    }

}