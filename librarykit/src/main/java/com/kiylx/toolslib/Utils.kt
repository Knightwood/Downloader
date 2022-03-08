package com.kiylx.toolslib

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