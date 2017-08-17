package com.example.wenxiao.mediaplayer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt2;
import com.example.wenxiao.mediaplayer.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class Fragment_alum extends Fragment {
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_alum,container,false);
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView= (ListView) getView().findViewById(R.id.listview);
        final List<Music> musics= MusicData.getMusicData(getContext());
        for (int i=0;i<musics.size();i++){
            for (int j=0;j<musics.size();j++){
                    if (musics.get(i).getAlbumName().equals(musics.get(j).getAlbumName())) {
                        musics.remove(i);
                }
            }
        }
        MyAdoupt2 MA=  new MyAdoupt2(musics,getContext());
        listView.setAdapter(MA);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //管理Fragment
                String name=musics.get(position).getAlbumName();
                Intent intent=new Intent(getContext(),Fragment_alumone.class);
                intent.putExtra("singer",name);
                getContext().startActivity(intent);
            }
        });
    }
}
