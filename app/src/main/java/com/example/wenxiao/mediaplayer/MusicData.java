package com.example.wenxiao.mediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class MusicData {
    /**
     * 获取本地音乐文件
     * @return
     */
    public static List<Music> getMusicData(Context context)
    {
        ContentResolver resolver = context.getContentResolver();
        List<Music> list = new ArrayList<>();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //cursor不为空，moveToFirst()为true说明有数据
        if (cursor!=null&&cursor.moveToFirst())
        {
            do{
                //音乐id
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                //歌曲名字
                String musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //专辑名
                String albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //歌手名
                String singerName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲播放时长
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                //歌曲文件的大小
                String duration1= new SimpleDateFormat("mm:ss").format(duration);
                int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                //判断是否是音乐文件
                int isMusic = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.IS_MUSIC));
                //URL 歌曲的文件路劲
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //将音乐对象存入集合
                list.add(new Music(id,musicName,singerName,albumName,duration1,duration,path,size));
            }while(cursor.moveToNext());
        }
        return list;
    }

}

