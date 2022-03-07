package com.kiylx.librarykit.tools.baseadapter1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

/**
 * 创建者 kiylx
 * 创建时间 2020/11/3 19:51
 * packageName：com.crystal.aplayer.module_base.tools.baseadapter1
 * 描述：工具类，获取itemview或baseholder
 */
public class HolderCreator {

    public static View getItemLayout(View parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, (ViewGroup) parent, false);
    }

    public static <N> BaseHolder<N> getViewHolder(View parent, @LayoutRes int layoutId) {
        return new BaseHolder<N>(getItemLayout(parent, layoutId));
    }


}
