package com.kiylx.toolslib.nav_tool

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.util.*

/**
 * 存放底部导航栏和需要变换的fragment。
 * 处理底部导航栏与fragment的交互。
 * 这个版本适用于viewpager这种无法干预fragment创建与销毁的情形，
 * 使用FragmentManager中putFragment和getFragment方法实现
 */
class BottomNavContainer(
    factory: FragmentFactory,
    fragmentLayoutId: Int, //activity中显示fragment的容器
    defaultShowedFragment: String//activity中默认显示的fragment
) : BaseBottomNavContainer(factory, fragmentLayoutId, defaultShowedFragment) {


    override fun save(outState: Bundle, supportFragmentManager: FragmentManager) {
        outState.putString(currentKey, currentFragment)
        for (i in fragmentsList) {
            supportFragmentManager.putFragment(outState, i.key, i.value)
        }
    }

    override fun restore(
        savedInstanceState: Bundle?,
        supportFragmentManager: FragmentManager,
        keys: Array<String>
    ) {
        if (savedInstanceState != null) {
            savedInstanceState.getString(currentKey)?.let {
                currentFragment = it
            }
            keys.forEach {
                val f = supportFragmentManager.getFragment(savedInstanceState, it)
                if (f != null) {
                    fragmentsList[it] = f
                }
            }
        } else {
            showDefaultFragment()
        }
    }

}
