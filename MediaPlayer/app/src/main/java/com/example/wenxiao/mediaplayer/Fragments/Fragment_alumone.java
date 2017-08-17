package com.example.wenxiao.mediaplayer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wenxiao.mediaplayer.MainActivity;
import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt3;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt4;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.SysApplication;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7 0007.
 */

public class Fragment_alumone extends AppCompatActivity {
    private ListView listView;
    private TextView singername;
    private PlayMusicApplication app;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alumone);
        SysApplication.getInstance().addActivity(this);
        singername= (TextView) findViewById(R.id.alumname);
        listView= (ListView) findViewById(R.id.listview);
        List<Music> musics= MusicData.getMusicData(getApplicationContext());
        List<Music> musics1=new ArrayList<>();
        Intent intent=getIntent();
        app= (PlayMusicApplication) getApplication();
        String name=intent.getStringExtra("singer");
        singername.setText(name);
        for(int i=0;i<musics.size();i++){
           if (musics.get(i).getAlbumName().equals(name)){
               musics1.add(musics.get(i));
           }
        }

        MyAdoupt4 MA=  new MyAdoupt4(musics1,getApplicationContext());
        listView.setAdapter(MA);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断返回键
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Fragment_alumone.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
        //return false;
    }
}
