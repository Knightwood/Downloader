package com.kiylx.librarykit.tools.adapter

import android.view.View

/**
 * 将点击事件向外传递。
 * 比如activity或fragment，通过调用BasicAdapter中的setOnClickListener方法参与到适配器实现的点击事件中
 */
interface MyClickListener {
    /**
     * @param itemview中的view
     * @param pos 调用adapterPosition得到的position
     */
    fun onClick(v: View?, pos: Int)
}