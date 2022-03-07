package com.kiylx.librarykit.tools.baseadapter2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiylx.librarykit.tools.LogUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者 kiylx
 * 创建时间 2020/11/22 22:16
 * 描述：这个适配器会记录旧数据
 */
public abstract class BaseAdapter3<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    private static final  String tag="BaseAdapter3";
    protected List<T> dataList = null;//T类型的bean的list集合
    //protected WeakReference<Context> contextWeakReference;
    private int lastListSize = 0;//记录list的size

    public BaseAdapter3(@NonNull List<T> dataList) {
        this(dataList,null);
    }

    public BaseAdapter3(@NonNull List<T> dataList, Context context) {
        this.dataList = dataList;
        //this.contextWeakReference = new WeakReference<>(context==null? BaseApplication.getContext():context);
        recordListSize(dataList);
    }

    @Override
    public int getItemCount() {
        if (dataList == null)
            return 0;
        else
            return dataList.size();
    }

    public void removeOldData(@NonNull List<T> list) {
        this.dataList.clear();
        this.dataList.addAll(list);
        recordListSize(list);
        notifyDataSetChanged();
    }

    /**
     * @param newList 不仅包含旧数据还包含新数据
     */
    public void addMoreData(@NonNull List<T> newList) {
        if (this.dataList == null) {
            this.dataList = newList;
            recordListSize(newList);
            notifyDataSetChanged();
        } else {
            int newListSize = newList.size();
            notifyItemRangeInserted(lastListSize,newListSize);
            LogUtil.d(tag,"更新范围："+this.lastListSize+"//"+newListSize);
            recordListSize(newListSize);
        }

    }

    /**
     * @param newList 只包含新数据但不包含旧数据
     */
    public void addMoreData2(@NonNull List<T> newList) {
        if (this.dataList == null) {
            this.dataList = new ArrayList<>(newList);
            notifyDataSetChanged();
        } else {
            this.dataList.addAll(newList);
            notifyItemRangeInserted(this.lastListSize , newList.size());
        }
        recordListSize(newList);
    }

    private void recordListSize(@NonNull List<T> list) {
        this.lastListSize = this.dataList == null ? 0 : list.size();
    }

    private void recordListSize(int size) {
        this.lastListSize = size;
    }
}
