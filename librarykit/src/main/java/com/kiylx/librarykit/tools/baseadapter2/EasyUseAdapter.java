package com.kiylx.librarykit.tools.baseadapter2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiylx.librarykit.tools.LogUtil;

import java.util.List;

/**
 * 创建者 kiylx
 * 创建时间 2020/11/8 19:24
 * packageName：com.crystal.aplayer.module_base.tools.baseadapter2
 * 描述：
 */
public final class EasyUseAdapter<T, N extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<N> {
    protected List<T> list = null;//T类型的bean的list集合
    protected ViewHolderControl<T> viewHolderControl;
    private static final String TAG = "BASEADAPTER";

    public EasyUseAdapter(List<T> list) {
        this.list = list;
    }

    public EasyUseAdapter(List<T> list, @NonNull ViewHolderControl<T> viewHolderControl) {
        this.list = list;
        this.viewHolderControl=viewHolderControl;
    }

    /**
     * @return 返回初始化adapter时传入的数据
     */
    public List<T> getData() {
        return list;
    }

    /**
     * 给adapter设置数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        this.list = list;
    }

    public void setViewHolderControl(ViewHolderControl<T> viewHolderControl) {
        this.viewHolderControl = viewHolderControl;
    }

    @Override
    public int getItemViewType(int position) {
        return viewHolderControl.getViewType(list.get(position));
    }

    @NonNull
    @Override
    public N onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewHolderControl.getLayoutId(viewType), parent, false);
        LogUtil.d(TAG, "----------onCreateViewHolder  ");
        return viewHolderControl.createHolder(itemView, viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull N holder, int position) {
        T data = list.get(position);
        viewHolderControl.bindData(holder, data, getItemViewType(position));
        LogUtil.d(TAG, "----------onBindViewHolder  ");
    }

    /**
     * @return 获取list的大小，若list是null，返回0
     */
    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }
}
