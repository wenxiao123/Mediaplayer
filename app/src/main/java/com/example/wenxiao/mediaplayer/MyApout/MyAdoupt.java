package com.example.wenxiao.mediaplayer.MyApout;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenxiao.mediaplayer.Fragments.Fragment_alum;
import com.example.wenxiao.mediaplayer.Fragments.Fragment_menu;
import com.example.wenxiao.mediaplayer.Fragments.Fragment_player;
import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;
import com.example.wenxiao.mediaplayer.otherActivity;
import com.example.wenxiao.mediaplayer.service.PlayMusicService;

import java.util.List;
import java.util.zip.Inflater;

import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.ACTIVITY_PLAY_BUTTON_CLICK;
import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.ACTIVITY_PLAY_BUTTON_CLICK1;
import static java.security.AccessController.getContext;


/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class MyAdoupt extends BaseAdapter {
    private List<Music> musics;
    private LayoutInflater inflater;
    private PlayMusicApplication app;
    public MyAdoupt(List<Music> musics, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null)
        {
            convertView = inflater.inflate(R.layout.items,null);

            holder = new ViewHolder();
            holder.btn = (TextView) convertView.findViewById(R.id.btn1);
            holder.showtv = (TextView) convertView.findViewById(R.id.showtv);
            holder.btn1= (TextView) convertView.findViewById(R.id.btn2);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Music music = musics.get(position);
        app= (PlayMusicApplication) inflater.getContext().getApplicationContext();
        holder.btn .setText(music.getSingerName());
        holder.showtv .setText(music.getMusicName());
        holder.showtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p=position+"";
                Intent intent = new Intent();
                intent.putExtra("name",p);
                intent.setAction(ACTIVITY_PLAY_BUTTON_CLICK1);
                inflater.getContext().sendBroadcast(intent);
            }
        });
        holder.btn1.setText(music.getDuration1());
        return convertView;
    }
    class ViewHolder{
        TextView btn;
        TextView btn1;
        TextView showtv;
    }
}
