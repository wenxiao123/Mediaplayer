package com.example.wenxiao.mediaplayer.Fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts;
import com.example.wenxiao.mediaplayer.MyInterface.IMusicPlay;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.RoundImageView;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;
import com.example.wenxiao.mediaplayer.otherActivity;
import com.example.wenxiao.mediaplayer.service.PlayMusicService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts.SERVICE_PLAYER_PLAY;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class Fragment_player extends Fragment implements ApplicationCounts {
    private RoundImageView roundImageView;
    //公共音乐接口
    private boolean isReversal = false;// 是否反转
    private IMusicPlay binder;
    //与Service连接的对象
    private int currentRoatate = 0;
    private ServiceConnection conn;
    //Application对象
    private PlayMusicApplication app;
    //歌曲List集合
    private List<Music> musics;
    //按钮：播放/暂停、上一曲、下一曲
    private ImageView idPlay, idNext;
    //是否正在播放歌曲
    private boolean isPlaying;
    //按钮的点击监听器
    private ButtonClickListener buttonClickListener;
    //广播接收者
    private BroadcastReceiver receiver;
    //广播接收者的意图过滤器
    private IntentFilter filter;
    private int currentId = -1;
    private TextView tvTitle, tvArtist;
    int currentImg = 0;
    private SeekBar seekBar;
    private boolean isTouch;
    String a;
    private int [] image=new int []{
            R.drawable.zh1,
            R.drawable.zn1
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (app.isPlaying()) {
                seekBar.setProgress(binder.getProgress());
            }
            return false;
        }
    });
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        return view;
    }
    private void initView() {
        app = (PlayMusicApplication) getActivity().getApplication();
        //获取音乐列表
        musics = app.getMisics();
        currentId = app.getCurrentMusicIndex();
        a=currentId+"";
        //初始化按钮
        idPlay = (ImageView) getActivity().findViewById(R.id.idPlay);
        idNext = (ImageView) getActivity().findViewById(R.id.idNext);
        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setSelected(true);
        tvArtist = (TextView) getActivity().findViewById(R.id.tvArtist);
        roundImageView = (RoundImageView) getActivity().findViewById(R.id.roundImageView);
        buttonClickListener = new ButtonClickListener();
        roundImageView.setOnClickListener(buttonClickListener);
        idPlay.setOnClickListener(buttonClickListener);
        idNext.setOnClickListener(buttonClickListener);
        Intent intent = new Intent(getContext(), PlayMusicService.class);
        //建立连接
        conn = new InnerServiceConnection();
        //绑定Service
        getContext().bindService(intent, conn, BIND_AUTO_CREATE);
        //初始化广播接收者
        initReceiver();
        currentId = app.getCurrentMusicIndex();
        seekBar= (SeekBar) getView().findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //从指定位置播放
                //mPlayer.seekTo(seekBar.getProgress());
                binder.zz(seekBar.getProgress());
                isTouch = false;
            }

        });
    }
    private void initReceiver() {
        //初始化广播接收者
        receiver = new InnerReceiver();
        //初始化意图过滤器
        filter = new IntentFilter();
        //添加接收的广播类型
        filter.addAction(SERVICE_PLAYER_PLAY);
        filter.addAction(SERVICE_PLAYER_PLAY1);
        //注册广播接收者
        getContext().registerReceiver(receiver, filter);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        currentId = app.getCurrentMusicIndex();
        //获得播放歌曲的名字
        tvTitle.setText(musics.get(currentId).getMusicName());
        //获得播放歌曲的歌手
        tvArtist.setText(musics.get(currentId).getSingerName());
        if (app.isPlaying()==true){
            RotateAnimation animation =
                    new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(10000);
            animation.setRepeatCount(-1);
            animation.setInterpolator(new LinearInterpolator());
            animation.setFillAfter(true);
            roundImageView.startAnimation(animation);
            app.setPlaying(true);
        }
        seekBar.setMax((int) musics.get(currentId).getDuration());
        new Thread(){
            @Override
            public void run() {
                while(true)
                {
                    if (!isTouch)
                    {
                        handler.sendEmptyMessage(0);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    private class ButtonClickListener implements View.OnClickListener {
        RotateAnimation animation =
                new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.idPlay:
                    if(currentImg>=1) {
                        currentImg=-1;
                    }
                    idPlay.setImageResource(image[++currentImg]);
                    if (app.isPlaying()) {
                        binder.stop();
                        //isPlaying = false;
                        app.setPlaying(false);
                        animation.setDuration(0);
                        animation.setRepeatCount(0);
                        roundImageView.startAnimation(animation);
                    } else {
                        binder.play();
                        handler.sendEmptyMessage(0);
                        app.setPlaying(true);

                    }
                    break;
                case R.id.idNext:
                    binder.playNext();
                    isPlaying = true;
                    app.setPlaying(true);
                    animation.setDuration(10000);
                    animation.setRepeatCount(-1);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setFillAfter(true);
                    roundImageView.startAnimation(animation);
                    break;
                case R.id.roundImageView:
                    Intent intent1 = new Intent(getContext(), otherActivity.class);
                    getContext().startActivity(intent1);
                    break;
            }
        }
    }

    private class InnerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IMusicPlay) service;
            app.setBinder(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SERVICE_PLAYER_PLAY.equals(action)) {
                //获得当前正在播放的歌曲
                currentId = app.getCurrentMusicIndex();
                //获得播放歌曲的名字
                tvTitle.setText(musics.get(currentId).getMusicName());
                tvTitle.setSingleLine(true);
                tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvTitle.setHorizontallyScrolling(true); //让文字可以水平滑动
                //获得播放歌曲的歌手
                tvArtist.setText(musics.get(currentId).getSingerName());
                RotateAnimation animation =
                        new RotateAnimation(0, 360,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(10000);
                animation.setRepeatCount(-1);
                animation.setInterpolator(new LinearInterpolator());
                animation.setFillAfter(true);
                roundImageView.startAnimation(animation);
            }else if (SERVICE_PLAYER_PLAY1.equals(action)) {
                //获得当前正在播放的歌曲
                currentId = app.getCurrentMusicIndex();
                //获得播放歌曲的名字
                tvTitle.setText(musics.get(currentId).getMusicName());
                //获得播放歌曲的歌手
                tvTitle.setSingleLine(true);
                tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvTitle.setHorizontallyScrolling(true); //让文字可以水平滑动
                tvArtist.setText(musics.get(currentId).getSingerName());
                RotateAnimation animation =
                        new RotateAnimation(0, 360,
                                Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(10000);
                animation.setRepeatCount(-1);
                animation.setInterpolator(new LinearInterpolator());
                animation.setFillAfter(true);
                roundImageView.startAnimation(animation);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unbindService(conn);
        getContext().unregisterReceiver(receiver);
        System.exit(0);
    }
    /**
     * 重写返回键，将其变为home键
     */

}