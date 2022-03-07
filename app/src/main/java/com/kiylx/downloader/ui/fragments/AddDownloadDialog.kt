package com.kiylx.downloader.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.kiylx.download_module.lib_core.model.DownloadInfo
import com.kiylx.downloader.R
import com.kiylx.downloader.databinding.ActivityAddDownloadBinding
@Deprecated("未使用")
class AddDownloadDialog : DialogFragment() {
    var downloadInfo: DownloadInfo? = null
    lateinit var binding: ActivityAddDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        initLayoutView()
        return dialog.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            if (it.window != null) {
                val window: Window = it.window!!;
                val layoutParams: WindowManager.LayoutParams = window.attributes;
                layoutParams.apply {
                    //gravity = Gravity.BOTTOM;//指定显示位置
                    width = WindowManager.LayoutParams.MATCH_PARENT; //指定显示大小
                    height = WindowManager.LayoutParams.MATCH_PARENT;
                }
                window.apply {
                    //设置背景，不然无法扩展到屏幕边缘
                    setBackgroundDrawable(ColorDrawable(Color.rgb(255, 255, 255)));
                    //显示消失动画
                    //setWindowAnimations(R.style.animate_dialog);
                    //让属性设置生效
                    attributes = layoutParams;
                }
                //设置点击外部可以取消对话框
                isCancelable = true;
            }
        }

    }

    fun initLayoutView() {
        binding.expansionHeader.setOnClickListener { view: View? ->
            binding.advancedLayout.toggle()
            binding.expansionHeader.toggleExpand()
        }
    }
}