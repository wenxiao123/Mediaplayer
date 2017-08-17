package com.example.wenxiao.mediaplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ZhangYang on 2017/5/27.
 */

public class MyFragmentViewPagerAdapter extends FragmentPagerAdapter{
    //存储Fragment的集合
    private List<Fragment> fragments;
    private List<String> tab;

    public MyFragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tab) {
        super(fm);
        this.fragments = fragments;
        this.tab = tab;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab.get(position);
    }
}
