package com.example.wenxiao.mediaplayer;

import android.app.Activity;
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

import android.support.v7.app.AppCompatActivity;

import android.test.ActivityUnitTestCase;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wenxiao.mediaplayer.Fragments.Fragment_menu;
import com.example.wenxiao.mediaplayer.MyInterface.ApplicationCounts;
import com.example.wenxiao.mediaplayer.MyInterface.IMusicPlay;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;
import com.example.wenxiao.mediaplayer.lrc.DefaultLrcBuilder;
import com.example.wenxiao.mediaplayer.lrc.LrcRow;
import com.example.wenxiao.mediaplayer.lrc.LrcView;
import com.example.wenxiao.mediaplayer.service.PlayMusicService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.wenxiao.mediaplayer.R.id.roundImageView;

/**
 * Created by Administrator on 2017/6/1 0001.
 */

public class otherActivity extends AppCompatActivity implements ApplicationCounts {
    private SeekBar seekBar;
    private boolean isTouch;
    private RoundImageView rou;
    //公共音乐接口
    private IMusicPlay binder;
    //与Service连接的对象
    private ServiceConnection conn;
    //Application对象
    private PlayMusicApplication app;
    //歌曲List集合
    private List<Music> musics;
    //按钮：播放/暂停、上一曲、下一曲
    private ImageView idPrevious,idPlay,idNext,idBack;
    //是否正在播放歌曲
    private boolean isPlaying;
    //按钮的点击监听器
    private ButtonClickListener buttonClickListener;
    //广播接收者
    private BroadcastReceiver receiver;
    //广播接收者的意图过滤器
    private IntentFilter filter;
    private int currentId=-1;
    private TextView tvTitle,tvArtist,tv1,lrc,sort;
    int currentImg = 0;
    private int [] image=new int []{
            R.drawable.stop,
            R.drawable.start1,
    };
    private LrcView mLrcView;
    String a;
    int cur;
    private  SlideBackLayout mSlideBackLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_one);
        SysApplication.getInstance().addActivity(this);
        mSlideBackLayout=new SlideBackLayout(otherActivity.this);
        mSlideBackLayout.bind();
        //获取Application对象
        app = (PlayMusicApplication) getApplication();
        //初始化界面
        initViews();
        //初始化广播接收者
        initReceiver();
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
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //获取当前的歌曲播放进度，设置为seekBar的进度
            if(app.isPlaying()){
            seekBar.setProgress(binder.getProgress());
            String date= new SimpleDateFormat("mm:ss").format(seekBar.getProgress());
            tv1.setText(date);
            }
            return false;
        }
    });
    /**
     * 初始化界面
     */
    private void initViews() {
        //获取音乐列表
        musics = app.getMisics();
        //初始化按钮
        binder=app.getBinder();
        currentId = app.getCurrentMusicIndex();
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                if(seekBar1 == seekBar){
                    mLrcView.seekTo(progress, true, fromUser);
                }
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
        int [] image = new int []{
                R.drawable.start1,
                R.drawable.stop,
        };
        mLrcView = (LrcView) findViewById(R.id.lrcView);
        idPlay = (ImageView) findViewById(R.id.idPlay);
        idPrevious = (ImageView) findViewById(R.id.idPrevious);
        idNext = (ImageView) findViewById(R.id.idNext);
        idBack= (ImageView) findViewById(R.id.backbtn);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);
        sort= (TextView) findViewById(R.id.sort);
        sort.setText(app.getMenu());
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.getMenu().equals("顺序"))
                {
                    app.setSort(2);
                    sort.setText("单曲");
                    app.setMenu("单曲");
                }else if (app.getMenu().equals("单曲"))
                {
                    app.setSort(1);
                    sort.setText("插叙");
                    app.setMenu("插叙");
                }else if (app.getMenu().equals("插叙"))
                {
                    app.setSort(3);
                    sort.setText("顺序");
                    app.setMenu("顺序");
                }
            }
        });
        tv1= (TextView) findViewById(R.id.tv1);
        rou= (RoundImageView) findViewById(R.id.btn_r);
        lrc= (TextView) findViewById(R.id.lrc);
        mLrcView.setVisibility(View.GONE);
        rou.setVisibility(View.VISIBLE);
        lrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //让img显示与隐藏
                if (lrc.getText().equals("显示歌词"))
                {
                    //隐藏图片
                    //img.setVisibility(View.INVISIBLE);
                    mLrcView.setVisibility(View.VISIBLE);
                    rou.setVisibility(View.GONE);
                    lrc.setText("取消显示");
                }else
                {
                    //显示图片
                    mLrcView.setVisibility(View.GONE);
                    rou.setVisibility(View.VISIBLE);
                    lrc.setText("显示歌词");
                }
            }
        });
        //为按钮添加监听事件
        buttonClickListener = new ButtonClickListener();
        idPlay.setOnClickListener(buttonClickListener);
        idPrevious.setOnClickListener(buttonClickListener);
        idNext.setOnClickListener(buttonClickListener);
        idBack.setOnClickListener(buttonClickListener);
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
            rou.startAnimation(animation);
            app.setPlaying(true);
            mLrcView.setLrcRows(getLrcRows());
        }

    }
    /**
     * 初始化广播接收者
     */
    private void initReceiver() {
        //初始化广播接收者
        receiver = new InnerReceiver();
        //初始化意图过滤器
        filter = new IntentFilter();
        //添加接收的广播类型
        filter.addAction(SERVICE_PLAYER_PLAY);
        filter.addAction(SERVICE_PLAYER_PLAY1);
        //注册广播接收者
        registerReceiver(receiver,filter);
    }

    private List<LrcRow> getLrcRows(){
       int[] lrc=new int[]{R.raw.we,R.raw.x_1,R.raw.h,R.raw.yjm,R.raw.jj,R.raw.qu,R.raw.ww,R.raw.xgl,
       R.raw.sn,R.raw.am,R.raw.rzdx,R.raw.whp,R.raw.tlbb};
        List<LrcRow> rows = null;
        InputStream is = getResources().openRawResource(lrc[app.getCurrentMusicIndex()]);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line ;
        StringBuffer sb = new StringBuffer();
        try {
            while((line = br.readLine()) != null){
                sb.append(line+"\n");
            }
            System.out.println(sb.toString());
            rows = DefaultLrcBuilder.getIstance().getLrcRows(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 按钮点击监听器
     */
    private class ButtonClickListener implements View.OnClickListener {
        RotateAnimation animation =
                new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.idPlay:
                    if(currentImg>=1) {
                        currentImg=-1;
                    }
                   // Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
                    idPlay.setImageResource(image[++currentImg]);
                    if (app.isPlaying())
                    {
                        binder.stop();
                        app.setPlaying(false);
                        animation.setDuration(0);
                        animation.setRepeatCount(0);
                        rou.startAnimation(animation);
                    }else
                    {
                        binder.play();
                        mLrcView.setLrcRows(getLrcRows());
                        handler.sendEmptyMessage(0);
                        app.setPlaying(true);
                        animation.setDuration(10000);
                        animation.setRepeatCount(-1);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setFillAfter(true);
                        rou.startAnimation(animation);

                    }
                    break;
                case R.id.idPrevious:
                    binder.playPrevious();
                    mLrcView.setLrcRows(getLrcRows());
                    break;
                case R.id.idNext:
                    binder.playNext();
                     mLrcView.setLrcRows(getLrcRows());
                    break;
                case R.id.backbtn:
                    tiaozhuan();
                    break;
            }

        }
    }

    private void tiaozhuan() {
        Intent intent=new Intent(otherActivity.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 广播接收者
     */
    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (SERVICE_PLAYER_PLAY.equals(action))
            {
                //获得当前正在播放的歌曲
                currentId = app.getCurrentMusicIndex();
                //获得播放歌曲的名字
                tvTitle.setText(musics.get(currentId).getMusicName());
                //获得播放歌曲的歌手
                tvArtist.setText(musics.get(currentId).getSingerName());
                //获得播放歌曲的专辑
            }else  if (SERVICE_PLAYER_PLAY1.equals(action))
            {
                mLrcView.setLrcRows(getLrcRows());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       unregisterReceiver(receiver);
    }
    /**
     * 重写返回键，将其变为home键
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //判断返回键
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//            return true;
//        }
//       return super.onKeyDown(keyCode, event);
//        //return false;
//    }

}
