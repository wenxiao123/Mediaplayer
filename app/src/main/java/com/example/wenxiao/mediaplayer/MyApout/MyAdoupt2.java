package com.example.wenxiao.mediaplayer.MyApout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class MyAdoupt2 extends BaseAdapter {
    private List<Music> musics;
    private LayoutInflater inflater;

    public MyAdoupt2(List<Music> musics, Context context) {
        this.musics = musics;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Music getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int x=0;
        View view = inflater.inflate(R.layout.item2,null);
        TextView showtv= (TextView) view.findViewById(R.id.showtv);
        TextView btn= (TextView) view.findViewById(R.id.btn2);
        Music music=musics.get(position);
        List<Music> musics1= MusicData.getMusicData(inflater.getContext());
        for (int i=0;i<musics1.size();i++){
            if (music.getAlbumName().equals(musics1.get(i).getAlbumName())){
                x=x+1;
            }
        }
        showtv.setText(music.getAlbumName());
        btn.setText(x+"首");
        return view;
    }
}
