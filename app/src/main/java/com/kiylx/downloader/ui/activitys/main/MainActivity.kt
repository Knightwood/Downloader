package com.kiylx.downloader.ui.activitys.main

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.kiylx.downloader.R
import com.kiylx.downloader.databinding.ActivityMainBinding
import com.kiylx.downloader.ui.fragments.active.ActiveFragment
import com.kiylx.downloader.ui.fragments.finish.StopFragment
import com.kiylx.downloader.ui.fragments.setting.SettingsFragment
import com.kiylx.librarykit.toolslib.nav_tool.BottomNavContainer2
import com.kiylx.librarykit.toolslib.nav_tool.FragmentFactory
import com.permissionx.guolindev.PermissionX
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private val fragmentKeys = arrayOf(activeFragment_tag, finishFragment_tag, settingFragment_tag)
    private lateinit var bottomNavContainer: BottomNavContainer2

    private val bottomNavSelectListener =
        NavigationBarView.OnItemSelectedListener(fun(it: MenuItem): Boolean {
            when (it.itemId) {
                R.id.active_indicator ->
                    bottomNavContainer.switchFragment(activeFragment_tag)
                R.id.finish_indicator ->
                    bottomNavContainer.switchFragment(finishFragment_tag)
                R.id.setting_indicator ->
                    bottomNavContainer.switchFragment(settingFragment_tag)
                else -> {}
            }
            return true
        })

    override fun onSaveInstanceState(outState: Bundle) {
        bottomNavContainer.save(outState, supportFragmentManager)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        bottomNavContainer = BottomNavContainer2(object : FragmentFactory {
            override fun newFragment(key: String): Optional<Fragment> {
                when (key) {
                    activeFragment_tag ->
                        return Optional.of(ActiveFragment.newInstance())
                    finishFragment_tag ->
                        return Optional.of(StopFragment.newInstance())
                    settingFragment_tag ->
                        return Optional.of(SettingsFragment.newInstance())
                }
                return Optional.empty()
            }

        }, activityBinding.fragmentContainer.id, activeFragment_tag)
        bottomNavContainer.initBottomNav(
            supportFragmentManager,
            activityBinding.bottomNav, bottomNavSelectListener, null)
        bottomNavContainer.restore(savedInstanceState, supportFragmentManager, fragmentKeys)
    }

    override fun onStart() {
        super.onStart()
        requestAppPermission()
    }

    private fun requestAppPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "即将重新申请的权限是程序必须依赖的权限", "我已明白", "取消")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "你需要给予必要的权限以运行此程序", "我已明白", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (!allGranted) {
                    Toast.makeText(this, "您拒绝了权限,无法访问网络下载及存储下载文件", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val activeFragment_tag: String = "activeFragment_001"
        const val finishFragment_tag: String = "finishFragment_002"
        const val settingFragment_tag: String = "settingFragment_003"
    }
}