package com.kiylx.librarykit.tools.baseadapter2;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 创建者 kiylx
 * 创建时间 2020/11/8 19:37
 * packageName：com.crystal.aplayer.module_base.tools.baseadapter2
 * 描述：
 */
public class BaseHolder2 extends RecyclerView.ViewHolder {
    private SparseArray<View> views;//缓存一些view
    private HolderType holderTypeEnum;//记录自己是什么类型


    public BaseHolder2(@NonNull View itemView, HolderType typeEnum) {
        super(itemView);
        views = new SparseArray<>();//暂存itemView中的子view的id和view对象，
        this.holderTypeEnum=typeEnum;
    }

    public BaseHolder2(View view) {
        this(view,null);
    }

    /**
     * @return 返回此viewholder的类型
     */
    public HolderType getHolderTypeEnum() {
        return holderTypeEnum;
    }

    /**
     * @param resId 资源id
     * @param <T>   要从itemView中获取的view的类型
     * @return 返回查找的view
     * 查找itemview中resId 的view。
     * views用来存储itemview中的子view，减少findviewbyid的次数
     */
    public <T extends View> T getView(int resId) {
        View v = views.get(resId);
        if (v == null) {
            v = itemView.findViewById(resId);
            views.put(resId, v);
        }
        return (T) v;
    }
    /**
     * 设置TextView文本
     */
    public BaseHolder2 setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置View的Visibility
     */
    public BaseHolder2 setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    /**
     * 设置ImageView的资源
     */
    public BaseHolder2 setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

   // 设置条目点击事件

    public <N> void setOnItemClickListener(View.OnClickListener listener, N info) {
        itemView.setOnClickListener(listener);
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    /**
     * 设置条目长按事件
     */
    public void setOnItemLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    public <T extends AppCompatActivity> void startActivity(Context context, Class<T> clazz){
        Intent i = new Intent(context, clazz);
        context.startActivity(i);
    }
    public void setOnItemClickListener(View.OnClickListener listener,int... viewId) {
        for (int i:viewId) {
            getView(i).setOnClickListener(listener);
        }
    }
    public void setOnItemClickListener(View.OnClickListener listener,View... views) {
        for (View i:views) {
            i.setOnClickListener(listener);
        }
    }
}
