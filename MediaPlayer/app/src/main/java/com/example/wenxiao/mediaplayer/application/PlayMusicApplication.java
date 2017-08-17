package com.example.wenxiao.mediaplayer.application;

import android.app.Application;
import android.util.Log;


import com.example.wenxiao.mediaplayer.Music;
import com.example.wenxiao.mediaplayer.MusicData;
import com.example.wenxiao.mediaplayer.MyInterface.IMusicPlay;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class PlayMusicApplication extends Application {
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    private  boolean isPlaying;
    /**
     * 歌曲的List集合
     */
    private IMusicPlay binder;
    private List<Music> misics;

    public IMusicPlay getBinder() {
        return binder;
    }

    public void setBinder(IMusicPlay binder) {
        this.binder = binder;
    }

    /**
     * 当前播放的歌曲
     */
    private int currentMusicIndex = 0;
    //正在播放歌曲的索引
    private int AudioSessionId;

    public int getAudioSessionId() {
        return AudioSessionId;
    }

    public void setAudioSessionId(int audioSessionId) {
        AudioSessionId = audioSessionId;
    }

    /**
     * 获取正在播放歌曲的索引
     * @return
     */
    public int getCurrentMusicIndex() {
        return currentMusicIndex;
    }

    /**
     * 设置当前播放的歌曲索引
     * @param currentMusicIndex
     */
    public void setCurrentMusicIndex(int currentMusicIndex) {
        this.currentMusicIndex = currentMusicIndex;
    }

    /**
     * 获取歌曲的List集合
     * @return
     */
    public List<Music> getMisics() {
        return misics;
    }

    public void setMisics(List<Music> misics) {
        this.misics = misics;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("", "PlayMusicAplication onCreate");
        //获取歌曲列表
        misics = MusicData.getMusicData(this);
    }
   private int Sort=2;

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
    String menu="单曲";

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private int singernum;

    public int getSingernum() {
        return singernum;
    }

    public void setSingernum(int singernum) {
        this.singernum = singernum;
    }
}
