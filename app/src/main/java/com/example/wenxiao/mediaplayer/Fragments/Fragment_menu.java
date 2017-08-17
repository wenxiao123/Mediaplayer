package com.example.wenxiao.mediaplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;


import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.RoundImageView;
import com.example.wenxiao.mediaplayer.View.SlidingMenu;
import com.example.wenxiao.mediaplayer.adapter.MyFragmentViewPagerAdapter;
import com.example.wenxiao.mediaplayer.viewPager.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class Fragment_menu extends Fragment {
    private RoundImageView toggleBtn;
    private DrawerLayout drawerLayout;
    //ViewPager
    private ViewPager viewPager;
    //声明Fragment集合
    private List<Fragment> fragments;
    private TabLayout tableLayout;
    private List<String> title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_menu,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //menu = (SlidingMenu) getActivity().findViewById(R.id.menu);
        toggleBtn = (RoundImageView) getView().findViewById(R.id.btn1);
        tableLayout= (TabLayout) getView().findViewById(R.id.TabLayout);
        viewPager = (ViewPager) getView().findViewById(R.id.fragment_ViewPager);
        drawerLayout= (DrawerLayout) getActivity().findViewById(R.id.id_drawerLayout);
        //创建集合对象
        fragments = new ArrayList<>();
        //初始化Fragment

           fragments.add(new Fragment_list());
           fragments.add(new Fragment_singer());
           fragments.add(new Fragment_alum());
        title=new ArrayList<>();
        title.add("歌曲");
        title.add("歌手");
        title.add("专辑");
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<title.size();i++){
        tableLayout.addTab(tableLayout.newTab().setText(title.get(i)));
        }
        //创建适配器
        MyFragmentViewPagerAdapter adapter =
                new MyFragmentViewPagerAdapter(getChildFragmentManager(),fragments,title);
        //添加适配器
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    drawerLayout.closeDrawers();
                }else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }
}
