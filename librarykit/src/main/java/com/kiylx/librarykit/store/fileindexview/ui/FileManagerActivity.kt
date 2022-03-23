package com.kiylx.librarykit.store.fileindexview.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.kiylx.librarykit.R
import com.kiylx.librarykit.store.fileindexview.tree.*
import com.kiylx.librarykit.store.fileindexview.tree.FileTree.MatchPattern.Companion.getCodeByMatchPattern
import com.kiylx.librarykit.store.fileindexview.tree.FileTree.MatchPattern.Companion.getMatchPatternByCode
import com.kiylx.librarykit.store.fileindexview.tree.SelectAction.*
import com.kiylx.librarykit.store.fileindexview.tree.SelectAction.Companion.getCodeBySelectAction
import com.kiylx.librarykit.store.fileindexview.tree.SelectAction.Companion.getSelectActionByCode
import com.kiylx.librarykit.tools.adapter.MyClickListener

class FileManagerActivity : AppCompatActivity(), UpdateUi, View.OnClickListener {
    lateinit var recyclerView: RecyclerView
    lateinit var positiveButton: MaterialButton
    lateinit var newFileButton: MaterialButton
    lateinit var backButton: MaterialButton
    lateinit var pathTextView: MaterialTextView

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(this).apply {
            title = "名称"
            val editView = EditText(this@FileManagerActivity)
            // TODO: 文本框
            setView(editView)
            setPositiveButton("确定") { dialog, which ->
                when (selectAction) {
                    FILE -> fileTree.createFile(editView.text.toString())
                    FOLDER -> fileTree.createFolder(editView.text.toString())
                    NONE -> TODO()
                }
               fileTree.refresh()
            }
            setNegativeButton(
                "取消"
            ) { dialog, which -> dialog?.dismiss() }
        }.create()
    }

    lateinit var adapter: FileListAdapter
    lateinit var fileTree: FileTree

    lateinit var defaultPath: String
    lateinit var includeExt: Array<out String?>
    var showHidden: Boolean = false
    var matchPattern: FileTree.MatchPattern = FileTree.MatchPattern.NONE //对于includeExt列表，是包括还是排除模式
    var selectAction: SelectAction = NONE //选择模式

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)
        intent.extras?.apply {
            defaultPath = get("defaultPath") as String
            includeExt = get("matchPattern") as Array<out String?>
            showHidden = get("includeExt") as Boolean
            matchPattern = getMatchPatternByCode(get("showHidden") as Int)
            selectAction = getSelectActionByCode(get("selectAction") as Int)

            fileTree = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                FileTreeImpl(updateUi = this@FileManagerActivity)
            } else {
                FileTreeNG()
            }
            initViews()
            fileTree.initData(
                defaultPath,
                showHidden,
                matchPattern,
                includeExt
            )
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.index_view_container)
        positiveButton = findViewById<MaterialButton?>(R.id.save_folder).apply {
            setOnClickListener(this@FileManagerActivity)
        }
        newFileButton = findViewById<MaterialButton?>(R.id.new_folder).apply {
            setOnClickListener(this@FileManagerActivity)
        }
        backButton = findViewById<MaterialButton?>(R.id.back_folder).apply {
            setOnClickListener(this@FileManagerActivity)
        }
        pathTextView = findViewById(R.id.folder_path)

        adapter = FileListAdapter(mutableListOf())
        adapter.myClick = object : MyClickListener {
            override fun onClick(v: View?, pos: Int) {
                val data = adapter.dataLists[pos]
                clickThis(data.filePath)
            }

        }
        recyclerView.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        recyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged") //因为每次展示的列表数据都是新的
    override fun updateUI(currentPathDirs: List<FileInfo>) {
        adapter.dataLists = currentPathDirs as MutableList<FileInfo>
        adapter.notifyDataSetChanged()
    }

    fun clickThis(path: String?) {
        fileTree.enterThisFolder(path, true)
    }

    override fun parseDrawable(isFile: Boolean, ext: String?): Int {
        return R.drawable.ic_file_grey600_24dp
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.save_folder -> {
                    val currentPath = fileTree.getCurrentPath()
                    val intent = Intent().apply {
                        putExtra("path", currentPath)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                R.id.new_folder -> {
                    dialog.show()
                }
                R.id.back_folder -> {
                    fileTree.goBack()
                }

            }
        }

    }
}

/**
 * @param defaultPath 当前的，传入的路径
 * @param matchPattern 匹配模式，1：filter为包含模式，显示filter中条目匹配的文件和所有文件夹
 *                        2： filter为排除模式，显示所有文件夹和无法与filter中条目匹配的文件
 *@param includeExt 包含文件名称或者后缀的列表
 * @param showHidden 是否显示隐藏文件
 *
 */
fun openFileManager(
    defaultPath: String,
    includeExt: Array<out String?>,
    showHidden: Boolean,
    matchPattern: FileTree.MatchPattern,
    appCompatActivity: AppCompatActivity,
    requestCode: Int,
    selectAction: SelectAction
) {
    val intent = Intent(appCompatActivity, FileManagerActivity::class.java).apply {
        putExtra("defaultPath", defaultPath)
        putExtra("matchPattern", getCodeByMatchPattern(matchPattern))
        putExtra("includeExt", includeExt)
        putExtra("showHidden", showHidden)
        putExtra("selectAction", getCodeBySelectAction(selectAction))
    }
    appCompatActivity.startActivityForResult(
        intent,
        requestCode
    )
}