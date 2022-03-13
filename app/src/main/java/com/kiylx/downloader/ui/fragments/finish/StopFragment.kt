package com.kiylx.downloader.ui.fragments.finish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.downloader.R
import com.kiylx.downloader.databinding.FragmentStopBinding
import com.kiylx.downloader.kits.Differ
import com.kiylx.downloader.ui.fragments.FragmentViewModel
import com.kiylx.librarykit.tools.adapter.MyClickListener
import com.kiylx.librarykit.toolslib.getViewModel

class StopFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FragmentViewModel
    private val adapter: FinishInfoAdapter = FinishInfoAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= getViewModel(activity, FragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = FragmentStopBinding.inflate(layoutInflater, container, false)

        recyclerView = root.finishRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        adapter.setMyClickListener(object : MyClickListener {
            override fun onClick(v: View?, pos: Int) {
                v?.let {
                    when(it.id){
                        R.id.info_more->{
                            //打开菜单
                            val popMenu = PopupMenu(activity, v)
                            popMenu.apply {
                                menuInflater.inflate(R.menu.download_info_more, menu)
                                setOnMenuItemClickListener { item ->
                                    item?.let { it ->
                                        when (it.itemId) {
                                            R.id.cancel_download -> {}
                                            R.id.delete -> {}
                                            R.id.share_download_file -> {}
                                            R.id.copy_download_url -> {}
                                        }
                                    }
                                    true
                                }
                            }
                            popMenu.show()
                        }
                    }
                }
            }

        })
        return root.root
    }
    override fun onStart() {
        super.onStart()
        observerInfos()
    }
    private fun observerInfos() {
        viewModel.getFinishList().observe(this) { newList ->
            //同时下载最多也就5条数据，数据量小，此处就不开协程处理差异了
            val oldList = adapter.dataLists
            val diffResult: DiffUtil.DiffResult =
                DiffUtil.calculateDiff(Differ(oldList, newList), true)
            adapter.dataLists = newList.toMutableList()
            diffResult.dispatchUpdatesTo(adapter)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance()=
            StopFragment()

    }
}