package com.example.wenxiao.mediaplayer.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wenxiao.mediaplayer.MainActivity;
import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt;
import com.example.wenxiao.mediaplayer.MyApout.MyAdoupt3;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.SysApplication;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.filter;
import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.SERVICE_PLAYER_PLAY3;
import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 2017/6/7 0007.
 */

public class Fragment_singone extends AppCompatActivity {
    private ListView listView;
    private TextView singername;
    private PlayMusicApplication app;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singone);
        SysApplication.getInstance().addActivity(this);
        singername= (TextView) findViewById(R.id.singname);
        listView= (ListView) findViewById(R.id.listview);
        List<Music> musics= MusicData.getMusicData(getApplicationContext());
        List<Music> musics1=new ArrayList<>();
        initReceiver();
        Intent intent=getIntent();
        app= (PlayMusicApplication) getApplication();
        String name=intent.getStringExtra("singer");
        singername.setText(name);
        for(int i=0;i<musics.size();i++){
           if (musics.get(i).getSingerName().equals(name)){
               musics1.add(musics.get(i));
           }
        }

        MyAdoupt3 MA=  new MyAdoupt3(musics1,getApplicationContext());
        listView.setAdapter(MA);
    }

    private void initReceiver() {
        //初始化广播接收者
        receiver = new InnerReceiver();
        //初始化意图过滤器
        filter = new IntentFilter();
        //添加接收的广播类型
        filter.addAction(SERVICE_PLAYER_PLAY3);
        //注册广播接收者
        getApplicationContext().registerReceiver(receiver, filter);
    }
    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SERVICE_PLAYER_PLAY3.equals(action)) {
               onDestroy();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断返回键
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Fragment_singone.this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
        //return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        System.exit(0);
    }
}
