package com.kiylx.downloader.kits

import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.download_module.lib_core.model.TaskLifecycle.*
import com.kiylx.download_module.utils.TextUtils
import com.kiylx.download_module.view.SimpleDownloadInfo
import okhttp3.Request

fun DownloadInfo.buildRequest(): Request {
    return generateRequest(url, userAgent, referer)
}

fun SimpleDownloadInfo.buildRequest(): Request {
    return generateRequest(url,null,null)
}

fun generateRequest(url: String, userAgent: String?, referer: String?): Request {
    val builder: Request.Builder = Request.Builder()
        .url(url) //构建request
    if (!userAgent.isNullOrBlank()) {
        builder.addHeader("User-Agent", userAgent)
    }
    if (!referer.isNullOrBlank()) builder.addHeader("Referer", referer)
    return builder.build()
}

enum class TaskState(val code: Int) {
    UNKNOWN(0),
    WAITTING(1),
    DOWNLOADING(2),
    COMPLETED(3),
    CANCELED(4),
    DOWNLOAD_FAILED(5);
}