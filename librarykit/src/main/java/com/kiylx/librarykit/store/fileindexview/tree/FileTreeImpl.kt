package com.kiylx.librarykit.store.fileindexview.tree

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.kiylx.librarykit.R
import com.kiylx.librarykit.store.system.systemkits.SystemFacadeHelper
import com.kiylx.librarykit.tools.LogUtil
import java.io.File
import java.util.*

class FileTreeImpl(
    private val updateUi: UpdateUi
) : FileTree {
    private var backDeque: Deque<String> = LinkedList() //此时回退栈是空的

    private var matchPattern: FileTree.MatchPattern = FileTree.MatchPattern.NONE//默认不使用过滤
    private var filterArray: Array<out String?> = emptyArray() //筛选特定文件类型的数组

    private var realRootPath = Environment.getExternalStorageDirectory().path //根目录的路径
    private var shouldShowHidden = false

    private var currentPath: String? = null
    var currentPathFiles: List<FileInfo> = mutableListOf()//当前路径下的文件列表

    /**
     * @param homePath   默认展示的路径，展示此路径下的文件列表
     * @param action 选择文件或是选择文件夹
     * @param includeExt 只显示特定几种类型的文件
     */
    override fun initData(
        homePath: String,
        showHidden: Boolean,
        matchPattern: FileTree.MatchPattern,
        includeExt: Array<out String?>
    ) {
        this.shouldShowHidden = showHidden
        this.filterArray = includeExt
        this.matchPattern = matchPattern
        currentPathFiles =
            enterThisFolder(
                homePath,
                matchPattern = matchPattern,
                filter = filterArray
            ) //获得当前路径下的文件列表
        updateUI(currentPathFiles)
    }


    /**
     * @param showHidden 是否显示隐藏文件
     */
    override fun setShowHidden(showHidden: Boolean) {
        this@FileTreeImpl.shouldShowHidden = showHidden
        enterThisFolder(
            currentPath,
            matchPattern = this.matchPattern,
            filter = filterArray,
            insertToDeque = false
        )
        updateUI(currentPathFiles)
    }

    /**
     * 回到上一级文件夹，
     * 取出栈中的字符串，这个字符串就是当前目录的父路径。
     */
    override fun goBack() {
        if (backDeque.isNotEmpty()) {
            currentPathFiles = mutableListOf()//置空
            if (backDeque.first != backDeque.last) {//回退栈是不是回退到底了
                backDeque.pollLast()//取出本层路径
                currentPath = backDeque.last
                currentPathFiles = enterThisFolder(
                    path = currentPath,
                    matchPattern = this.matchPattern,
                    filter = filterArray,
                    insertToDeque = false
                )
            } else {
                val path = backDeque.pollLast()
                path?.let {
                    val parentFolder = File(path).parent
                    currentPathFiles =  enterThisFolder(parentFolder, true, this.matchPattern, filterArray)
                }
            }
            updateUI(currentPathFiles)//展示返回回到上一层级的路径
            LogUtil.d(TAG, "当前路径，返回时$currentPath")
        }
    }

    /**
     * @param path 当前的，传入的路径
     * @param insertToDeque 是否将当前路径加入回退栈
     * @param matchPattern 匹配模式，include：filter为包含模式，显示filter中条目匹配的文件和所有文件夹
     *                        exclude： filter为排除模式，显示所有文件夹和无法与filter中条目匹配的文件
     *@param filter 包含文件名称或者后缀的列表
     *
     * @return file的子file列表，如果传入的路径不存在或这是文件将返回null
     */
    override fun enterThisFolder(
        path: String?,
        insertToDeque: Boolean,
        matchPattern: FileTree.MatchPattern,
        filter: Array<out String?>
    ): List<FileInfo> {
        val folder: File
        if (path == null || path.isBlank() || path.isEmpty()) { //获取根目录
            currentPath = realRootPath
            folder = Environment.getExternalStorageDirectory()
        } else { //获取特定目录下的文件
            currentPath = path
            folder = File(path)
            if (!folder.exists() || folder.isFile) {
                LogUtil.d(TAG, "getChildrenList: 路径不存在或这是文件")
                return emptyList()
            }
        }
        if (insertToDeque)
            backDeque.addLast(currentPath)
        return findChildren(folder, matchPattern, filter)//遍历文件，返回FileInfo
    }

    override fun refresh(){
        currentPathFiles =  enterThisFolder(currentPath, false, this.matchPattern, filterArray)
        updateUI(currentPathFiles)
    }

    override fun findChildren(
        folder: File,
        matchPattern: FileTree.MatchPattern,
        filter: Array<out String?>
    ): MutableList<FileInfo> {
        val resultList: MutableList<FileInfo> = mutableListOf()
        var imgId: Int
        val dirs = folder.listFiles()
        dirs?.let { arrayOfFiles ->
            val tmpList = arrayOfFiles.filter {
                if (filter.isEmpty())
                    isDisplayFile(it.name)
                else {
                    val resultBoolean: Boolean =
                        if (matchPattern == FileTree.MatchPattern.INCLUDE) {
                            filter.contains(it.extension)
                        } else {
                            !filter.contains(it.extension)
                        }
                    resultBoolean && isDisplayFile(it.name)
                }
            }

            for (file in tmpList) {
                imgId = parseDrawable(file.isDirectory)
                resultList.add(FileInfo(imgId, file.name, file.absolutePath))
            }
        }
        return resultList
    }

    override fun createFile(name: String) {
        val f = File(currentPath, name)
        if (!f.exists()) f.createNewFile()
    }

    override fun createFolder(name: String) {
        val f = File(currentPath, name)
        f.mkdirs()
    }

    /**
     * 是否显示隐藏文件，决定文件能否显示
     */
    private fun isDisplayFile(fileName: String): Boolean {
        return if (shouldShowHidden)
            true
        else
            !fileName.startsWith(".")
    }

    /**
     * @return 当前层级路径，初始化时是根目录的路径
     */
    override fun getCurrentPath(): String? {
        if (currentPath == null) {
            currentPath = Environment.getExternalStorageDirectory().path //存储根目录的路径
        }
        return currentPath
    }

    override fun updateUI(currentPathDirs: List<FileInfo>) {
        updateUi.updateUI(currentPathDirs)
    }

    override fun parseDrawable(isFile: Boolean, ext: String?): Int {
        return if (isFile) {
            //R.drawable.ic_file_grey600_24dp
            updateUi.parseDrawable(isFile, ext)
        } else {
            R.drawable.ic_folder_grey600_24dp
        }
    }

    companion object {
        private val TAG = "IndexViewImpl"
    }

}