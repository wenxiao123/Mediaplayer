package com.example.wenxiao.mediaplayer.MyInterface;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public interface IMusicPlay {
    /**
     * 播放
     */
    void play();
    void play(int a);
    /**
     * 下一曲
     */
    void playNext();
    /**
     * 上一曲
     */
    void playPrevious();
    /**
     * 停止
     */
    void stop();
    int getAudioSessionId();
    int posion();
    void zz(int a);
    MediaPlayer player();
    int getProgress();
}
