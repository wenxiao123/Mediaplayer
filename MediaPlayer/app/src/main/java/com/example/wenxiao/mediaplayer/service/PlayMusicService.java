package com.example.wenxiao.mediaplayer.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.Toast;


import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts;
import com.example.wenxiao.mediaplayer.MyInterface.IMusicPlay;
import com.example.wenxiao.mediaplayer.RoundImageView;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class PlayMusicService extends Service implements ApplicationCounts {
    private RoundImageView roundImageView;
    private IBinder binder;
    // 播放器
    private MediaPlayer player;
    //Application对象
    private PlayMusicApplication app;
    //歌曲的List集合
    private List<Music> musics;
    //是否正在播放
    private boolean isPlaying;
    //暂停时的进度
    private int currentPosition;
    //是否由用户操作开始播放歌曲
    private boolean isStarted;
    //广播接收者
    private BroadcastReceiver receiver;
    //广播接收者的意图过滤器
    private IntentFilter filter;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Application对象
        Log.e("", "PlayMusicService onCreate" );
        app = (PlayMusicApplication) getApplication();
        //获取歌曲的List集合
        musics = app.getMisics();
        Log.d("------------->", musics.toString());
        //初始化播放器
        player = new MediaPlayer();
        //添加监听，判断当前这首歌曲是否播放完毕
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isStarted)
                {
                    if (app.getSort()==3){
                    //播放下一首
                    nextMusic();
                    Intent intent = new Intent();
                    intent.setAction(SERVICE_PLAYER_PLAY1);
                    sendBroadcast(intent);
                    }else if(app.getSort()==2){
                        playMusic();
                    }else if(app.getSort()==1){
                        int x=(int)(Math.random()*(musics.size()));
                        app.setCurrentMusicIndex(x);
                        playMusic();
                    }

                }
            }
        });
        //注册广播接收者
        initReceiver();
    }
    /**
     * 初始化广播接收者
     */
    private void initReceiver() {
        //初始化广播接收者
        receiver = new InnerRecriver();
        //初始化意图过滤器
        filter = new IntentFilter();
        //添加接收的广播类型
        filter.addAction(ACTIVITY_PLAY_BUTTON_CLICK);
        filter.addAction(ACTIVITY_PLAY_BUTTON_CLICK1);
        filter.addAction(ACTIVITY_PREVIOUS_BUTTON_CLICK);
        filter.addAction(ACTIVITY_NEXT_BUTTON_CLICK);
        filter.addAction(ACTIVITY_START_MAIN);
        //注册广播接收者
        registerReceiver(receiver,filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new PlayMusicBider();
        return binder;
    }
    /**
     * 广播接收者
     * @return
     */
    private class InnerRecriver extends BroadcastReceiver {
        @Override
       public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //当播放按钮被点击
            if (ACTIVITY_PLAY_BUTTON_CLICK.equals(action))
            {
                if (app.isPlaying())
                {
                    //暂停
                    pauseMusic();
                }else {
                    //播放
                    playMusic();
                }
            }else if (ACTIVITY_PREVIOUS_BUTTON_CLICK.equals(action))
            {
                //上一曲
                previousMusic();
            }else if (ACTIVITY_NEXT_BUTTON_CLICK.equals(action))
            {
                //下一曲
                nextMusic();
            }else if(ACTIVITY_PLAY_BUTTON_CLICK1.equals(action))
            {
                app.setAudioSessionId(player.getAudioSessionId());
                //清空变量中记录的当前播放进度
                currentPosition = 0;
                //更新播放状态
                app.setPlaying(false);
                    String intent1=intent.getStringExtra("name");
                    int a=Integer.parseInt(intent1);
                    //Toast.makeText(getApplicationContext(),intent1,Toast.LENGTH_SHORT).show();
                    app.setCurrentMusicIndex(a);
                if (app.isPlaying())
                {
                    //暂停
                    pauseMusic();
                }else {
                    //播放
                    playMusic();
                }
            }
        }
        }
    //定义Binder，包含播放功能
    public class PlayMusicBider extends Binder implements IMusicPlay {

        @Override
        public void play() {
            Log.e("", "PlayMusicBider------>play " );
            playMusic();

        }

        @Override
        public void play(int a) {
            Log.e("", "getCurrentMusicIndex=="+a );
        }

        @Override
        public void playNext() {
            Log.e("", "PlayMusicBider------>playNext " );
            nextMusic();
        }

        @Override
        public void playPrevious() {
            Log.e("", "PlayMusicBider------>playPrevious " );
            previousMusic();
        }

        @Override
        public void stop() {
            Log.e("", "PlayMusicBider------>stop " );
            pauseMusic();
        }

        @Override
        public int getAudioSessionId() {
            Log.e("", "PlayMusicBider------>getAudioSessionId " );
            return player.getAudioSessionId();
        }

        @Override
        public int posion() {
            return player.getCurrentPosition();
        }

        @Override
        public void zz(int a) {
            player.seekTo(a);
        }

        @Override
        public MediaPlayer player() {
            return player;
        }

        @Override
        public int getProgress() {
            return player.getCurrentPosition();
        }


    }
    /**
     * 上一首
     */
    private void previousMusic() {
        Log.e("", "previousMusic="+app.getCurrentMusicIndex() );
        int index = app.getCurrentMusicIndex();
        index--;
        if (index<0)
        {
            index = musics.size()-1;
        }
        app.setCurrentMusicIndex(index);
        playMusic();
    }

    /**
     * 下一曲
     */
    private void nextMusic() {
        Log.e("", "nextMusic="+app.getCurrentMusicIndex() );
        int index = app.getCurrentMusicIndex();
        index++;
        if (index>=musics.size())
        {
            index = 0;
        }
        app.setCurrentMusicIndex(index);
        playMusic();
    }

    /**
     * 暂停
     */
    private void pauseMusic() {

        Log.e("", "pauseMusic=="+app.getCurrentMusicIndex() );
        if (app.isPlaying())
        {
            //暂停
            player.pause();
            //记录暂停的位置
            currentPosition = player.getCurrentPosition();
            //更新播放状态
            app.setPlaying(false);
//            isPlaying =false;
        }
    }


    /**
     * 播放
     */
    private  void playMusic() {
        if (app.isPlaying())
        {
            //暂停
            player.stop();
        }
        //重置
        player.reset();
        try {
            //设置播放的歌曲
            player.setDataSource(musics.get(app.getCurrentMusicIndex())
                    .getPath());
            Log.e("", "getCurrentMusicIndex=="+app.getCurrentMusicIndex() );
            Log.e("", "music----"+musics.get(app.getCurrentMusicIndex()).getPath());
            //准备资源
            player.prepare();
            //播放
            //从指定位置播放音乐
            player.seekTo(currentPosition);
            player.start();
            app.setAudioSessionId(app.getCurrentMusicIndex());
            //清空变量中记录的当前播放进度
            currentPosition = 0;
            //更新播放状态
            //isPlaying = true;
            app.setPlaying(true);
            //更新状态为：用户操作
            isStarted = true;
            //发送广播：用户操作后开始播放歌曲
            Intent intent = new Intent();
            intent.setAction(SERVICE_PLAYER_PLAY);
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止接收广播
        unregisterReceiver(receiver);
        if (player!=null)
        {
            //释放播放器资源
            if (app.isPlaying())
            {
                //停止播放音乐
                player.stop();
            }
            player.release();
            player=null;
        }
    }
}
