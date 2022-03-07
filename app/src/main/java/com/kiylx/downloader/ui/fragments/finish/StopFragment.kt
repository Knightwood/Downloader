package com.kiylx.downloader.ui.fragments.finish

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.downloader.R
import com.kiylx.downloader.databinding.FragmentStopBinding
import com.kiylx.downloader.ui.Differ
import com.kiylx.downloader.ui.fragments.FragmentViewModel
import com.kiylx.librarykit.tools.adapter.SimpleAdapter

class StopFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: FragmentViewModel
    private val adapter: FinishInfoAdapter = FinishInfoAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentViewModel::class.java)
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
        adapter.setMyClickListener(object : SimpleAdapter.MyClickListener {
            override fun onClick(v: View?, pos: Int) {
                v?.let {
                    when(it.id){
                        R.id.info_more->{
                            //打开菜单
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
            adapter.dataLists = newList
            diffResult.dispatchUpdatesTo(adapter)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance()=
            StopFragment()

    }
}