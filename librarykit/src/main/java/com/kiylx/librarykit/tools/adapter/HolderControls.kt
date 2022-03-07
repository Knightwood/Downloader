package com.kiylx.librarykit.tools.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 统一管理所有的viewholder，比如根据type获得lzyoutid
 * 使用方法： 新建一个object class继承此类，并new出所有的holderinfo,在begin方法中批量注册holderinfo,
 * 并在init块中调用实现好的begin方法。
 */
abstract class HolderControls {
    val UNKNOWN: HolderInfo = HolderInfo(-999, -1)

    private val enums: MutableList<HolderInfo> = mutableListOf()

    //这两个方法由外部决定如何实现，比如继承此类后，实现了很多holderInfo，统一由一个方法注册和销毁
    abstract fun begin()//生命周期开始
    abstract fun end()//结束生命周期

    fun register(vararg holderInfos: HolderInfo) {
        enums.addAll(holderInfos)
    }

    fun register(holderInfos: HolderInfo) {
        if (!enums.contains(holderInfos))
            enums.add(holderInfos)
    }

    fun unregister(holderInfos: HolderInfo) {
        enums.remove(holderInfos)
    }

    fun getVHByTypeId(type: Int): HolderInfo {
        for (t in enums) {
            if (type == t.type) {
                return t
            }
        }
        return UNKNOWN
    }

    fun getVHByLayoutId(layout: Int): HolderInfo {
        for (t in enums) {
            if (layout == t.type) {
                return t
            }
        }
        return UNKNOWN
    }

    fun getTypeId(layout: Int): HolderInfo {
        for (t in enums) {
            if (layout == t.itemLayoutId) {
                return t
            }
        }
        return UNKNOWN
    }

    fun getLayoutId(typeId: Int): HolderInfo {
        for (t in enums) {
            if (typeId == t.itemLayoutId) {
                return t
            }
        }
        return UNKNOWN
    }
}

open class SimpleHolder(item: View, var holderInfo: HolderInfo) :
    RecyclerView.ViewHolder(item) {

    /**
     *对view批量设置点击事件
     */
    fun setClickEvent(vararg views: View, clickListener: View.OnClickListener) {
        views.forEach {
            it.setOnClickListener(clickListener)
        }
    }
}

/**
 * 记录viewholder信息，
 * @param type 类型
 * @param itemLayoutId 布局文件
 */
data class HolderInfo(val type: Int, val itemLayoutId: Int)