package com.kiylx.toolslib.nav_tool

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.util.*

abstract class BaseBottomNavContainer(
    var factory: FragmentFactory,
    val fragmentLayoutId: Int,
    var defaultShowedFragment: String//activity中默认显示的fragment
) : LifecycleObserver {
    var currentFragment: String = defaultShowedFragment//当前正显示的fragment
    val fragmentsList: MutableMap<String, Fragment> = mutableMapOf()
    var fragmentManager: FragmentManager? = null
    private var bottomNavigationView: BottomNavigationView? = null//底部导航栏

    fun initBottomNav(
        @NotNull fragmentManager: FragmentManager,
        @NotNull navigationView: BottomNavigationView,
        @Nullable listener: NavigationBarView.OnItemSelectedListener?,
        @Nullable listener2: NavigationBarView.OnItemReselectedListener?
    ): BaseBottomNavContainer {
        this.fragmentManager = fragmentManager
        bottomNavigationView = navigationView
        bottomNavigationView!!.apply {
            setOnItemSelectedListener(listener)
            setOnItemReselectedListener(listener2)
        }
        return this
    }

    fun showDefaultFragment() {
        var f: Fragment? = fragmentsList[defaultShowedFragment]
        if (f == null) {
            val tmp = factory.newFragment(defaultShowedFragment)
            if (tmp.isPresent)
                f = tmp.get()
            else {
                throw ClassNotFoundException("prime fragment not exist")
            }
        }
        fragmentsList[defaultShowedFragment]=f
        showFragment(defaultShowedFragment,f, false)
    }

    /**
     * 隐藏其他fragment,显示参数指定的fragment，如果fragment不存在，则什么都不做
     * @param nextFragmentKey 将要显示的fragment的key
     */
    fun switchFragment(nextFragmentKey: String) {
        if (nextFragmentKey == currentFragment) return
        var fragment: Fragment? = fragmentsList[nextFragmentKey]
        if (fragment == null) {
            val tmp = factory.newFragment(nextFragmentKey)
            if (tmp.isPresent) {
                fragment = tmp.get()
                fragmentsList[nextFragmentKey] = fragment
            } else {
                return
            }
        }
        showFragment(nextFragmentKey,fragment,true)
        currentFragment = nextFragmentKey
    }

    fun showFragment(key: String,fragment: Fragment, hideAll: Boolean) {
        fragmentManager?.let {
            val transaction = it.beginTransaction()
            if (hideAll)
                hideAll(transaction)
            if (!fragment.isAdded) {
                transaction.add(fragmentLayoutId, fragment, key)
            }
            transaction.show(fragment).commit()
        }
    }

    fun hideAll(transaction: FragmentTransaction): FragmentTransaction {
        val list = fragmentsList.values
        for (i in list) {
            transaction.hide(i)
        }
        return transaction
    }

    fun registerFragment(key: String, fragment: Fragment) {
        fragmentsList.putIfAbsent(key, fragment)
    }

    fun registerFragments(pairs: List<Pair<String, Fragment>>) {
        fragmentsList.putAll(pairs)
    }

    fun unRegisterFragment(key: String) {
        fragmentsList.remove(key)
    }

    abstract fun save(outState: Bundle, supportFragmentManager: FragmentManager)

    abstract fun restore(
        savedInstanceState: Bundle?,
        supportFragmentManager: FragmentManager,
        keys: Array<String>
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun resume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun clear() {
        this.fragmentManager = null
    }

    companion object {
        const val currentKey = "currentFragmentKey"

        /**
         * 随机生成一个key
         * 截取randomUUID()方法生成的字符串加上fragmentName
         */
        fun generateFragmentKey(fragmentName: String): String {
            val pre = UUID.randomUUID().toString().substring(0, 8)
            return fragmentName + "_" + pre
        }
    }
}

/**
 * 通过key值提供fragment的实例
 */
interface FragmentFactory {
    fun newFragment(key: String): Optional<Fragment>
}