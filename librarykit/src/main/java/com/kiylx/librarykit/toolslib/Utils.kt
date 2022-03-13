package com.kiylx.librarykit.toolslib

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 通过与fragment关联的activity获得activity生命周期的viewmodel.
 * 用于一个activity,多个fragment时，fragment之间共享同一个viewmodel
 */
fun <T : ViewModel> getViewModel(activity: FragmentActivity?, clazz: Class<T>): T {
    val vm = activity?.run {
        ViewModelProvider(this).get(clazz)
    }
    if (vm != null)
        return vm
    else
        throw Exception("Invalid Activity")
}

/**
 * 复制文字，粘贴到剪贴板
 */
fun copyText(context: Application, title: String, msg: String) {
    val manager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
    val clip: ClipData = ClipData.newPlainText(title, msg);
    manager.setPrimaryClip(clip);
}
