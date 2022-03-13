package com.kiylx.librarykit.toolslib

import android.util.Log
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * 使用kotlin委托机制创建弱引用的工具类
 */
class Weak<T: Any>(initializer: ()-> T?) {
    var weakReference=WeakReference<T?> (initializer())
    constructor():this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        Log.d("Weak Delegate","-----------getValue")
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        Log.d("Weak Delegate","-----------setValue")
        weakReference = WeakReference(value)
    }
}