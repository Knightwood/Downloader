package com.kiylx.downloader.core.db.other

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kiylx.download_module.lib_core.model.*
import java.util.*

@Entity
data class DownloadBean(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo val url: String,
    @ColumnInfo var fileFolder: String,
    @ColumnInfo var fileName: String = "",
    @ColumnInfo var filePath: String = "",
    @ColumnInfo var mimeType: String? = null,
    @ColumnInfo var totalBytes: Long = -1L,//需要下载文件的总大小，其从response中获取，或者从调用它的方法中给予
    @ColumnInfo var isHasMetadata: Boolean = false,
    @ColumnInfo var fetchCount: Int = 0,//初始化后，计数是0,获取一次metadata计数加一  //MetaData
    @ColumnInfo var retryAfter: Int = 0,//多久后重新尝试下载
    @ColumnInfo var retryCount: Int = 0,
    @ColumnInfo var userAgent: String? = null,
    @ColumnInfo var referer: String = "",
    @ColumnInfo var lifeCycle: TaskLifecycle = TaskLifecycle.OH,
    @ColumnInfo var isRunning: Boolean = false, //下载任务是否正在进行
    @ColumnInfo var finalCode: Int = StatusCode.STATUS_INIT,
    @ColumnInfo var finalMsg: String? = null,
    @ColumnInfo var taskResult: TaskResult.TaskResultCode,
    @ColumnInfo var isPartialSupport: Boolean = false,//是否支持分块下载
    @ColumnInfo(defaultValue = "1") var threadCounts: Int,
    @ColumnInfo var blockSize: Long = -1,//下载时根据分配线程数量（threadNum）决定的文件分块大小。（最后一个分块可能会小于或大于其他分块大小）
    @ColumnInfo var splitStart: Array<Long?>,
    @ColumnInfo var splitEnd: Array<Long?>,
    @ColumnInfo var currentBytes: Long = -1,
    @ColumnInfo var speed: Long = 0,
    @ColumnInfo val isDownloadSuccess: Boolean,
    @ColumnInfo var checkSum: String? = null, // MD5, SHA-256。 添加下载生成downloadinfo时添加，也可不添加 。若添加此值，在下载完成时，会校验此值
    @ColumnInfo var description: String? = null//下载描述信息
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DownloadBean

        if (uuid != other.uuid) return false
        if (url != other.url) return false
        if (fileFolder != other.fileFolder) return false
        if (fileName != other.fileName) return false
        if (filePath != other.filePath) return false
        if (mimeType != other.mimeType) return false
        if (totalBytes != other.totalBytes) return false
        if (isHasMetadata != other.isHasMetadata) return false
        if (fetchCount != other.fetchCount) return false
        if (retryAfter != other.retryAfter) return false
        if (retryCount != other.retryCount) return false
        if (userAgent != other.userAgent) return false
        if (referer != other.referer) return false
        if (lifeCycle != other.lifeCycle) return false
        if (isRunning != other.isRunning) return false
        if (finalCode != other.finalCode) return false
        if (finalMsg != other.finalMsg) return false
        if (isPartialSupport != other.isPartialSupport) return false
        if (threadCounts != other.threadCounts) return false
        if (blockSize != other.blockSize) return false
        if (!splitStart.contentEquals(other.splitStart)) return false
        if (!splitEnd.contentEquals(other.splitEnd)) return false
        if (currentBytes != other.currentBytes) return false
        if (speed != other.speed) return false
        if (isDownloadSuccess != other.isDownloadSuccess) return false
        if (checkSum != other.checkSum) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + fileFolder.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + filePath.hashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + totalBytes.hashCode()
        result = 31 * result + isHasMetadata.hashCode()
        result = 31 * result + fetchCount
        result = 31 * result + retryAfter
        result = 31 * result + retryCount
        result = 31 * result + (userAgent?.hashCode() ?: 0)
        result = 31 * result + referer.hashCode()
        result = 31 * result + lifeCycle.hashCode()
        result = 31 * result + isRunning.hashCode()
        result = 31 * result + finalCode
        result = 31 * result + (finalMsg?.hashCode() ?: 0)
        result = 31 * result + isPartialSupport.hashCode()
        result = 31 * result + threadCounts
        result = 31 * result + blockSize.hashCode()
        result = 31 * result + splitStart.contentHashCode()
        result = 31 * result + splitEnd.hashCode()
        result = 31 * result + currentBytes.hashCode()
        result = 31 * result + speed.hashCode()
        result = 31 * result + isDownloadSuccess.hashCode()
        result = 31 * result + (checkSum?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}

fun DownloadBean.genSimpleDownloadInfo(): com.kiylx.download_module.view.SimpleDownloadInfo {
    return com.kiylx.download_module.view.SimpleDownloadInfo(
        uuid = uuid,
        fileName = fileName,
        filePath = filePath,
        url = url,
        fileSize = totalBytes,
        currentLength = currentBytes,
        speed = 0L,
        finalCode = finalCode,
        finalMsg = finalMsg,
        state = lifeCycle,
        isRunning = isRunning,
        taskResult = taskResult
    )
}

/**
 * 将downloadindo转换成downloadbean以存储
 */
fun DownloadInfo.convert(): DownloadBean {
    return DownloadBean(
        uuid!!,
        url,
        fileFolder,
        fileName,
        path,
        mimeType,
        totalBytes,
        isHasMetadata,
        fetchCount,
        retryAfter,
        retryCount,
        userAgent,
        referer,
        lifeCycle,
        isRunning,
        finalCode,
        finalMsg,
        taskResult,
        isPartialSupport,
        threadCounts,
        blockSize,
        splitStart,
        splitEnd,
        getDownloadedSize(),
        speed,
        isDownloadSuccess,
        checkSum,
        description
    )
}

fun DownloadBean.convert(): DownloadInfo {
    val bean: DownloadBean = this
    val info = DownloadInfo(
        url,
        fileFolder,
        fileName
    ).apply {
        this.fileName = bean.fileName
        this.path = bean.filePath
        this.mimeType = bean.mimeType
        this.totalBytes = bean.totalBytes
        this.isHasMetadata = bean.isHasMetadata
        this.fetchCount = bean.fetchCount
        this.retryAfter = bean.retryAfter
        this.retryCount = bean.retryCount
        this.userAgent = bean.userAgent
        this.referer = bean.referer
        this.lifeCycle = bean.lifeCycle
        this.isRunning = bean.isRunning
        this.finalCode = bean.finalCode
        this.finalMsg = bean.finalMsg
        this.taskResult = bean.taskResult
        this.isPartialSupport = bean.isPartialSupport
        this.threadCounts = bean.threadCounts
        this.blockSize = bean.blockSize
        this.splitStart = bean.splitStart
        this.splitEnd = bean.splitEnd
        this.currentBytes = arrayOf(bean.currentBytes)
        this.speed = bean.speed
        this.checkSum = bean.checkSum
        this.description = bean.description
    }

    return info
}


@Entity
data class PieceBean @JvmOverloads constructor(
    @ColumnInfo val uuid: String,
    @ColumnInfo val blockId: Int = 0,

    @ColumnInfo var start: Long = -1,
    @ColumnInfo var end: Long = -1,
    @ColumnInfo var curBytes: Long = 0,//当前分块已经下载了多少
    @ColumnInfo var totalBytes: Long = -1,//此分块的完整大小

    @ColumnInfo var finalCode: Int = StatusCode.STATUS_INIT,
    @ColumnInfo var msg: String? = null,
)

fun PieceBean.convert(): PieceInfo {
    return PieceInfo(
        UUID.fromString(uuid), blockId, start, end, curBytes, totalBytes, finalCode, msg
    )
}

fun PieceInfo.convert(): PieceBean {
    return PieceBean(
        id.toString(), blockId, start, end, curBytes, totalBytes, finalCode, msg
    )
}

//Header
@Entity
data class HeaderBean(
    @PrimaryKey val infoUUID: UUID,
    @ColumnInfo val headerName: String,
    @ColumnInfo var headerValue: String
)

fun HeaderBean.convert(): HeaderStore {
    return HeaderStore(infoUUID, headerName, headerValue)
}

fun HeaderStore.convert(): HeaderBean {
    return HeaderBean(infoId, name, value)
}