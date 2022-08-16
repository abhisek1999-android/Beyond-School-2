package com.maths.beyond_school_280720220930.english_activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class EnglishViewPager extends FragmentStateAdapter {
    private final List<Fragment> fragments;
    private final FragmentManager fm;
    private final Lifecycle lifecycle;

    public EnglishViewPager(List<Fragment> fragments, FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        this.fragments = fragments;
        this.fm = fm;
        this.lifecycle = lifecycle;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
