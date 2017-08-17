package com.example.wenxiao.mediaplayer;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class Music {
    //音乐id
    private long id;
    //歌曲名
    private String musicName;
    //歌手名
    private String singerName;
    //专辑名
    private String albumName;
    //歌曲文件的播放时长
    private long duration;

    public String getDuration1() {
        return duration1;
    }

    public void setDuration1(String duration1) {
        this.duration1 = duration1;
    }

    private String duration1;
    //URL  歌曲文件存放的路劲
    private String path;
    //音乐文件大小
    private int size;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Music(long id, String musicName, String singerName, String albumName, String duration1,long duration, String path, int size) {
        this.id = id;
        this.musicName = musicName;
        this.singerName = singerName;
        this.albumName = albumName;
        this.duration1 = duration1;
        this.duration = duration;
        this.path = path;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", musicName='" + musicName + '\'' +
                ", singerName='" + singerName + '\'' +
                ", albumName='" + albumName + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
