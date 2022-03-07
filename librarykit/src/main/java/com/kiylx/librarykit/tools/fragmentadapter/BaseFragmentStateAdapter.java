package com.kiylx.librarykit.tools.fragmentadapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 创建者 kiylx
 * 创建时间 2020/9/18 15:58
 * packageName：com.crystal.mytoolslibrary.fragmentadapter
 * 描述：
 */
public class BaseFragmentStateAdapter extends FragmentStateAdapter {
    public BaseFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
