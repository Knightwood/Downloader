package com.kiylx.librarykit.store.fileindexview.tree

/**
 * 创建者 kiylx
 * 创建时间 2020/4/6 6:53
 */
class FileInfo(var fileName: String?, var filePath: String?) {
    var imgId = 0
    var fileSize: String? = null
    var fileCreateDate: String? = null
    private val parentPath: String? = null

    constructor(imgId: Int, fileName: String?, path: String?) : this(fileName, path) {
        this.imgId = imgId
    }

    constructor(fileName: String?, path: String?, fileSize: String?) : this(fileName, path) {
        this.fileSize = fileSize
    }
}