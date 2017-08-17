package com.example.wenxiao.mediaplayer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt1;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.Singer;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.ACTIVITY_PLAY_BUTTON_CLICK1;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class Fragment_singer extends Fragment {
    private ListView listView;
    private PlayMusicApplication app;
    private int po;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_singer,container,false);
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        int z=0;
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.listview);
        final List<Music> musics= MusicData.getMusicData(getContext());
        for (int i=0;i<musics.size();i++){
            for (int j=0;j<musics.size();j++){
                    if (musics.get(i).getSingerName().equals(musics.get(j).getSingerName())) {
                        musics.remove(i);
                        po=i;
                }
            }
        }
        app= (PlayMusicApplication) getContext().getApplicationContext();
        //List<Singer> singer=new ArrayList<>();
        MyAdoupt1 MA=  new MyAdoupt1(musics,getContext(),z);
        listView.setAdapter(MA);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //管理Fragment
                String name=musics.get(position).getSingerName();
                Intent intent=new Intent(getContext(),Fragment_singone.class);
                intent.putExtra("singer",name);
                getContext().startActivity(intent);
            }
        });
    }
}
