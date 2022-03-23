package com.kiylx.librarykit.store.fileindexview.tree

import java.io.File

/**
 * 创建者 kiylx
 * 创建时间 2022/3/16 15:23
 * 新版本，IndexView只能在旧版本上使用了
 * 启用分区存储后，此版本兼容MediaStore和SAF
 */
class FileTreeNG : FileTree {
    override fun initData(
        homePath: String,
        showHidden: Boolean,
        matchPattern: FileTree.MatchPattern,
        includeExt: Array<out String?>
    ) {
        TODO("Not yet implemented")
    }

    override fun setShowHidden(showHidden: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getCurrentPath(): String? {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun updateUI(currentPathDirs: List<FileInfo>) {
        TODO("Not yet implemented")
    }

    override fun enterThisFolder(
        path: String?,
        insertToDeque: Boolean,
        matchPattern: FileTree.MatchPattern,
        filter: Array<out String?>
    ): List<FileInfo>? {
        TODO("Not yet implemented")
    }

    override fun parseDrawable(isFile: Boolean, ext: String?): Int {
        TODO("Not yet implemented")
    }

    override fun findChildren(
        folder: File,
        matchPattern: FileTree.MatchPattern,
        filter: Array<out String?>
    ): MutableList<FileInfo> {
        TODO("Not yet implemented")
    }
}