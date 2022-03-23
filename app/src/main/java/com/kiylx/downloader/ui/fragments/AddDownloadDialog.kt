package com.kiylx.downloader.ui.fragments

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kiylx.download_module.lib_core.interfaces.ConnectionListener
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.download_module.lib_core.network.TaskDataReceive
import com.kiylx.downloader.MyApplication.Companion.myApplication
import com.kiylx.downloader.R
import com.kiylx.downloader.core.download_control.DownloadDelegate
import com.kiylx.downloader.databinding.ActivityAddDownloadBinding
import com.kiylx.downloader.kits.buildRequest
import com.kiylx.downloader.ui.activitys.adddownload.AddDownloadViewModel
import com.kiylx.librarykit.toolslib.MyTextWatcher
import java.io.IOException
import java.net.HttpURLConnection

@Deprecated("未使用")
class AddDownloadDialog : DialogFragment() {
    var downloadInfo: DownloadInfo? = null
    lateinit var binding: ActivityAddDownloadBinding
    private lateinit var viewModel: AddDownloadViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AddDownloadViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ActivityAddDownloadBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this.requireContext())
            .setView(binding.root)
            .setPositiveButton("开始下载") { dialog, which ->
                dismiss()
            }
            .setNegativeButton("取消") { dialog, which ->
                dismiss()
            }
        initLayoutView(binding)
        return dialog.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            if (it.window != null) {
                val window: Window = it.window!!
                val layoutParams: WindowManager.LayoutParams = window.attributes
                layoutParams.apply {
                    //gravity = Gravity.BOTTOM;//指定显示位置
                    width = WindowManager.LayoutParams.MATCH_PARENT //指定显示大小
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                window.apply {
                    //设置背景，不然无法扩展到屏幕边缘
                    setBackgroundDrawable(ColorDrawable(Color.rgb(255, 255, 255)))
                    //显示消失动画
                    //setWindowAnimations(R.style.animate_dialog)
                    //让属性设置生效
                    attributes = layoutParams
                }
                //设置点击外部可以取消对话框
                isCancelable = true
            }
        }

    }

    private fun initLayoutView(activityBinding: ActivityAddDownloadBinding) {
        activityBinding.expansionHeader.setOnClickListener { view: View? ->
            activityBinding.advancedLayout.toggle()
            activityBinding.expansionHeader.toggleExpand()
        }
        activityBinding.urlInputLayout.editText?.let {
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.url = s.toString()
                    return s
                }
            })
        }
        activityBinding.fileNameInputLayout.editText?.let {
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.fileName = s.toString()
                    return s
                }
            })
        }
        activityBinding.filePathInputLayout.editText?.let {
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.path = s.toString()
                    return s
                }
            })
        }
        activityBinding.layoutDescription.editText?.let {//下载描述
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.description = s.toString()
                    return s
                }
            })
        }

        activityBinding.piecesNumberSelect.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.downloadInfo.threadCounts = seekBar?.progress!!
            }
        })
        activityBinding.userAgent.editText?.let {
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.userAgent = s.toString()
                    return s
                }
            })
        }
        activityBinding.layoutChecksum.editText?.let {
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.checkSum = s.toString()
                    return s
                }
            })
        }
        activityBinding.layoutReferer.editText?.let {//http referer
            it.addTextChangedListener(object : MyTextWatcher(it) {
                override fun checkText(s: Editable?): Editable? {
                    viewModel.downloadInfo.referer = s.toString()
                    return s
                }
            })
        }
        activityBinding.testUrlButton.setOnClickListener {
            val textView = activityBinding.testResultText

            TaskDataReceive.testConnect(viewModel.downloadInfo.buildRequest(), object :
                ConnectionListener {
                override fun onResponseHandle(
                    response: okhttp3.Response?,
                    code: Int,
                    message: String?
                ) {
                    var resultMsg = " "
                    resultMsg = when (code) {
                        HttpURLConnection.HTTP_OK -> "OK"
                        HttpURLConnection.HTTP_PRECON_FAILED -> "Precondition failed"
                        HttpURLConnection.HTTP_UNAVAILABLE -> "HTTP_UNAVAILABLE"
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> "HTTP_INTERNAL_ERROR"
                        else -> " UnKnowError"
                    }
                    textView.text = resultMsg
                }

                override fun onMovedPermanently(httpCode: Int, newUrl: String?) {}
                override fun onIOException(e: IOException?) {}
                override fun onTooManyRedirects() {}
            })
        }
        activityBinding.addDownloadButton.setOnClickListener {
            //开始下载，并关闭此界面
            DownloadDelegate.getDownloads().execDownloadTask(viewModel.downloadInfo, false)
            dismissAllowingStateLoss()
        }
        activityBinding.cancelDownloadButton.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    fun store(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }else{
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, viewModel.downloadInfo.fileName)
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            val contentResolver =myApplication().contentResolver
            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

            viewModel.downloadInfo.path=uri!!.path!!
        }
    }
}