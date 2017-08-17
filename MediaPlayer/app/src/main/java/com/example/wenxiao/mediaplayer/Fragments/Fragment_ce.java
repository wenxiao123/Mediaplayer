package com.example.wenxiao.mediaplayer.Fragments;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.SysApplication;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;
import com.example.wenxiao.mediaplayer.otherActivity;

import static android.content.Context.AUDIO_SERVICE;
import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.SERVICE_PLAYER_PLAY3;


/**
 * Created by Administrator on 2017/5/27 0027.
 */

public class Fragment_ce extends Fragment {
    private TextView exit,close,sound,t1;
    private PlayMusicApplication app;
    private int max,current;
    private AudioManager mAudioManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ce,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        app= (PlayMusicApplication) getActivity().getApplication();
        t1= (TextView) getView().findViewById(R.id.t1);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"尚未完成",Toast.LENGTH_SHORT).show();
            }
        });
        exit= (TextView) getView().findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });
        close= (TextView) getActivity().findViewById(R.id.close_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Time.class);
                startActivity(intent);
            }
        });
        //初始化AudioManager
        mAudioManager = (AudioManager)getContext().getSystemService(AUDIO_SERVICE);
        //获取音乐总音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取当前音乐音量
        sound= (TextView) getActivity().findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"尚未完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SysApplication.getInstance().exit();
        System.exit(0);
    }
}
