package com.kiylx.librarykit.store.fileindexview.tree

import android.content.Context
import java.io.File


interface FileTree {
    /**
     * @param path   在此路径展示文件列表
     * @param action 选择文件或是选择文件夹
     * @param filter 只显示特定几种类型的文件
     */
    fun initData(
        homePath: String,
        showHidden: Boolean,
        matchPattern: MatchPattern = MatchPattern.NONE,
        includeExt: Array<out String?> = emptyArray()
    )

    /**
     * @param showHidden 是否显示隐藏文件
     */
    fun setShowHidden(showHidden: Boolean)

    /**
     * @return 当前层级路径，初始化时是根目录的路径
     */
    fun getCurrentPath(): String?

    /**
     * 回到上一级文件夹，
     * 取出栈中的字符串，这个字符串就是当前目录的父路径。
     */
    fun goBack()
    fun updateUI(currentPathDirs: List<FileInfo>)

    /**
     * @param path 当前的，传入的路径
     * @param insertToDeque 是否将当前路径加入回退栈
     * @param matchPattern 匹配模式，true：filter为包含模式，显示filter中条目匹配的文件和所有文件夹
     *                        false： filter为排除模式，显示所有文件夹和无法与filter中条目匹配的文件
     *@param filter 包含文件名称或者后缀的列表
     *
     * @return file的子file列表，如果传入的路径不存在或这是文件将返回null
     */
    fun enterThisFolder(
        path: String?,
        insertToDeque: Boolean = true,
        matchPattern: MatchPattern = MatchPattern.NONE,
        filter: Array<out String?> = emptyArray()
    ): List<FileInfo>?

    fun parseDrawable(isFile: Boolean, ext: String? = null): Int
    fun refresh()
    fun findChildren(
        folder: File,
        matchPattern: MatchPattern,
        filter: Array<out String?>
    ): MutableList<FileInfo>

    fun createFile(name: String)
    fun createFolder(name:String)

    enum class MatchPattern(i: Int) {
        INCLUDE(1), EXCLUDE(2), NONE(0);

        companion object {
            fun getMatchPatternByCode(code: Int): MatchPattern {
                return when (code) {
                    0 -> NONE
                    1 -> INCLUDE
                    2 -> EXCLUDE
                    else -> NONE
                }
            }

            fun getCodeByMatchPattern(code: MatchPattern): Int {
                return when (code) {
                    NONE -> 0
                    INCLUDE -> 1
                    EXCLUDE -> 2
                }
            }
        }

    }
}


/**
 * 选择文件，选择文件夹,普通的显示而不进行选择
 */
enum class SelectAction(i: Int) {
    FILE(1), FOLDER(2), NONE(0);

    companion object {
            fun getCodeBySelectAction(code: SelectAction): Int {
                return when (code) {
                    NONE -> 0
                    FILE -> 1
                    FOLDER -> 2
                }
            }

        fun getSelectActionByCode(code: Int): SelectAction {
            return when (code) {
                0 -> NONE
                1 -> FILE
                2 -> FOLDER
                else -> NONE
            }
        }
    }
}


interface UpdateUi {
    fun updateUI(currentPathDirs: List<FileInfo>)

    //fun clickThis(path: String?)//传出点击某一项或回退到某一层级时的文件夹的路径被触发，用于通知当前被选择的路径
    fun parseDrawable(isFile: Boolean, ext: String?): Int
}