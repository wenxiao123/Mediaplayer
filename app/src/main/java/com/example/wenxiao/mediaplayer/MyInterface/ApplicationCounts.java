package com.example.wenxiao.mediaplayer.MyInterface;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public interface ApplicationCounts {
    //Activity发出的广播
    /**
     * Activity发出 播放按钮被点击
     */
    final String ACTIVITY_PLAY_BUTTON_CLICK = "com.zy.PLAY";
    final String ACTIVITY_PLAY_BUTTON_CLICK1 = "com.zy.PLAY1";
    final String ACTIVITY_NEXT_BUTTON_CLICK = "com.zy.NEXT";
    final String ACTIVITY_PREVIOUS_BUTTON_CLICK = "com.zy.PREVIOUS";
    final String ACTIVITY_START_MAIN = "com.zy.START_MAIN";
    /**
     * Servicr发出的广播  播放歌曲
     */
    final String SERVICE_PLAYER_PLAY = "com.zy.PLAYER_PLAY";
    final String SERVICE_PLAYER_PLAY1= "com.zy.PLAYER_PLAY1";
    final String SERVICE_PLAYER_PLAY3= "com.zy.PLAYER_PLAY3";
    final String SERVICE_PLAYER_PAUSE = "com.zy.PLAYER_PAUSE";
    final String SERVICE_UPDATE_PROGRESS = "com.zy.UPDATE_PROGRESS";
    //
}
