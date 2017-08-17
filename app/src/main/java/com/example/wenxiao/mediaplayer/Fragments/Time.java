package com.example.wenxiao.mediaplayer.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wenxiao.mediaplayer.MainActivity;
import com.example.wenxiao.mediaplayer.R;
import com.example.wenxiao.mediaplayer.application.PlayMusicApplication;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class Time extends AppCompatActivity {
    private PlayMusicApplication app;
    private ButtonClickListener buttonClickListener;
    private TextView t10,t20,t30,t60,t90,tsure;
    private TimePicker timePicker;
    private CountDownTimer countDownTimer;
    private int closeHour,closeMinutes;
    private Button  yesBtn,noBtn;
    Calendar c = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);
        app= (PlayMusicApplication) getApplication();
        t10= (TextView) findViewById(R.id.t10);
        t20= (TextView) findViewById(R.id.t20);
        t30= (TextView) findViewById(R.id.t30);
        t60= (TextView) findViewById(R.id.t60);
        t90= (TextView) findViewById(R.id.t90);
        tsure= (TextView) findViewById(R.id.surebtn);
        buttonClickListener = new ButtonClickListener();
        t10.setOnClickListener(buttonClickListener);
        t20.setOnClickListener(buttonClickListener);
        t30.setOnClickListener(buttonClickListener);
        t60.setOnClickListener(buttonClickListener);
        t90.setOnClickListener(buttonClickListener);
        tsure.setOnClickListener(buttonClickListener);
    }

    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.t10:
                    Toast.makeText(Time.this,"音乐将在10分钟后停止播放",Toast.LENGTH_SHORT).show();
                    final int closeTime = 10000;
                    final int closeTimes = closeTime/1000;
                    countDownTimer= new CountDownTimer(600000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {
                            Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                          app.getBinder().stop();
                        }
                    };
                    countDownTimer.start();
                    break;
                case R.id.t20:
                    Toast.makeText(Time.this,"音乐将在20分钟后停止播放",Toast.LENGTH_SHORT).show();
                    countDownTimer= new CountDownTimer(1200000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                            app.getBinder().stop();
                        }
                    };
                    countDownTimer.start();
                    break;
                case R.id.t30:
                    Toast.makeText(Time.this,"音乐将在30分钟后停止播放",Toast.LENGTH_SHORT).show();
                    countDownTimer= new CountDownTimer(1800000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                            app.getBinder().stop();
                        }
                    };
                    countDownTimer.start();
                    break;
                case R.id.t60:
                    Toast.makeText(Time.this,"音乐将在60分钟后停止播放",Toast.LENGTH_SHORT).show();
                    countDownTimer= new CountDownTimer(3600000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                            app.getBinder().stop();
                        }
                    };
                    countDownTimer.start();
                    break;
                case R.id.t90:
                    Toast.makeText(Time.this,"音乐将在90分钟后停止播放",Toast.LENGTH_SHORT).show();
                    countDownTimer= new CountDownTimer(5400000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                            app.getBinder().stop();
                        }
                    };
                    countDownTimer.start();
                    break;
                case R.id.surebtn:
                    alertDialog();
                    break;
            }
        }
    }

    private void alertDialog() {
        View myView = null;
        LayoutInflater inflater = LayoutInflater.from(Time.this);
        myView = inflater.inflate(R.layout.time_picker_alert_dialog,null);

        yesBtn = (Button) myView.findViewById(R.id.yes_Btn);
        noBtn = (Button) myView.findViewById(R.id.no_Btn);
        timePicker = (TimePicker) myView.findViewById(R.id.timePicker);
        //设置为24小时制
        timePicker.setIs24HourView(true);
        //初始化小时
        timePicker.setCurrentHour(0);
        //初始化分钟
        timePicker.setCurrentMinute(1);
        //创建弹窗,添加视图
        final AlertDialog dialog = new AlertDialog.Builder(Time.this).setView(myView).create();
        //显示弹窗
        dialog.show();
        //确定按钮执行的操作
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户的选定时间
                closeHour = timePicker.getCurrentHour()*3600000;
                closeMinutes = timePicker.getCurrentMinute()*60000;
                final int closeTime = closeHour+closeMinutes;
                final int closeTimes = closeTime/1000;
                countDownTimer = new CountDownTimer(closeTime,closeTimes) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(Time.this,"音乐停止播放",Toast.LENGTH_SHORT).show();
                       app.getBinder().stop();
                    }
                };
                countDownTimer.start();
                //隐藏弹窗
                dialog.dismiss();
            }
        });
        //取消按钮操作
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                dialog.dismiss();
            }
        });
    }
}
