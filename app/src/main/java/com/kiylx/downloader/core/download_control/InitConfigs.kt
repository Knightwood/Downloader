package com.kiylx.downloader.core.download_control

import com.kiylx.download_module.Context
import com.kiylx.download_module.Downloads
import com.kiylx.download_module.Downloads.Companion.downloadsInstance
import com.kiylx.download_module.view.ViewSources
import com.kiylx.download_module.view.ViewsCenter
import com.kiylx.downloader.core.download_control.handlefinish.Finishhandler
import com.kiylx.downloader.core.download_control.viewSources.ViewObserverImpl
import com.kiylx.downloader.core.store.FileKitImpl
import com.kiylx.downloader.core.store.RepoImpl
import com.kiylx.downloader.core.store.SystemCallImpl
import com.kiylx.librarykit.tools.live_data_bus.core.BusCore

/**
 * 配置所有下载相关的工具类，它跟随application生命周期，所有databus也在此处被引用
 */
class InitConfigs private constructor() {
    lateinit var contextConfigs: Context.ContextConfigs
    lateinit var downloads: Downloads
    lateinit var context: Context
    lateinit var viewsCenter: ViewsCenter
    val dataBus = BusCore.getInstance()

    private fun initContext() {
        contextConfigs = Context.ContextConfigs().apply {
            threadNum = 8
            fileKitClazz = FileKitImpl::class.java
            repoClazz = RepoImpl::class.java
            sysCallClazz = SystemCallImpl::class.java
            downloads = downloadsInstance(this)!!
        }
        context = downloads.mContext.apply {
            limit = 2
            setDownloadFinishHandle(Finishhandler)
            val viewSources=ViewSources.viewSourcesInstance.apply {
                setViewObserver(ViewObserverImpl)
            }
            setViewObserver(viewSources)
        }
    }

    fun clear() {
        viewsCenter.unListen()
    }

    companion object {
        private var singletonInstance: InitConfigs? = null
            get() {
                if (field == null) field = InitConfigs()
                return field
            }

        fun get() = singletonInstance!!
    }

    init {
        initContext()
    }
}