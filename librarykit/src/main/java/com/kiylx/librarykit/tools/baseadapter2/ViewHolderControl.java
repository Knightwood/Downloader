package com.kiylx.librarykit.tools.baseadapter2;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建者 kiylx
 * 创建时间 2020/11/9 13:01
 * packageName：com.crystal.aplayer.module_base.tools.baseadapter2
 * 描述：定义获取viewholder类型，创建viewholder实例以及绑定数据的方法
 */
public interface ViewHolderControl<T> {
    /**
     * @param data 数据bean
     * @return 根据bean返回不同的viewtype
     * 默认返回0。根据bean来提供不同的viewtype，用于加载不同itemView生成不同的viewHolder
     */
    int getViewType(T data);

    @Deprecated
    int getViewType(int i);

    @Deprecated
    int getViewType(String s);

    /**
     * @param viewType viewtype
     * @return 根据viewType, 返回不同的itemView的layoutId
     */
    int getLayoutId(int viewType);

    /**
     * 泛型继承自baseholder的holder类
     *
     * @param itemView itemView
     * @param viewType viewType
     * @return 根据ViewType的不同生成不同的viewHolder实例
     */
    <N extends RecyclerView.ViewHolder> N createHolder(View itemView, int viewType);

    /**
     * @param holder       viewHolder
     * @param data         给viewholder的数据bean
     * @param itemViewType 此itemview的viewtype
     *                     根据viewType将数据绑定在不同的holder上
     */
    <N extends RecyclerView.ViewHolder> boolean bindData(N holder, T data, int itemViewType);
}
