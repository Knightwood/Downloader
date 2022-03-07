package com.kiylx.downloader.ui.activitys.adddownload

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kiylx.download_module.lib_core.interfaces.ConnectionListener
import com.kiylx.download_module.lib_core.network.TaskDataReceive
import com.kiylx.downloader.databinding.ActivityAddDownloadBinding
import com.kiylx.downloader.download_control.DownloadDelegate.Companion.getDownloads
import com.kiylx.toolslib.MyTextWatcher
import java.io.IOException
import java.net.HttpURLConnection.*


class AddDownloadActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityAddDownloadBinding
    private lateinit var viewModel: AddDownloadViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityAddDownloadBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        viewModel = ViewModelProvider(this).get(AddDownloadViewModel::class.java)
        initLayoutView()
    }

    fun initLayoutView() {
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

        activityBinding.piecesNumberSelect.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.downloadInfo.threadCounts= seekBar?.progress!!
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
            val textView=activityBinding.testResultText

            TaskDataReceive.testConnect(viewModel.downloadInfo,object: ConnectionListener {
                override fun onResponseHandle(
                    response: okhttp3.Response?,
                    code: Int,
                    message: String?
                ) {
                    var message:String=""
                    message = when (code) {
                        HTTP_OK -> "OK"
                        HTTP_PRECON_FAILED -> "Precondition failed"
                        HTTP_UNAVAILABLE -> "HTTP_UNAVAILABLE"
                        HTTP_INTERNAL_ERROR -> "HTTP_INTERNAL_ERROR"
                        else -> " UnKnowError"
                    }
                    textView.text=message
                }
                override fun onMovedPermanently(httpCode: Int, newUrl: String?) {}
                override fun onIOException(e: IOException?) {}
                override fun onTooManyRedirects() {}
            })
        }
       activityBinding.addDownloadButton.setOnClickListener {
           //开始下载，并关闭此界面
           getDownloads().execDownloadTask(viewModel.downloadInfo,false)
           finish()
       }
        activityBinding.cancelDownloadButton.setOnClickListener {
            finish()
        }
    }
}