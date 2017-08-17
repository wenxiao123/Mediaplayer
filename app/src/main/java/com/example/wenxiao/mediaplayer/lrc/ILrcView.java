package com.example.wenxiao.mediaplayer.lrc;

import java.util.List;

public interface ILrcView {
	/**
	 * 初始化画笔，颜色，字体大小等设置
	 */
	void init();

	/***
	 * 设置数据源
	 * @param lrcRows
	 */
	void setLrcRows(List<LrcRow> lrcRows);

	/***
	 * 指定时间
	 * @param progress 毫秒时间
	 * @param fromUser 是否由用户触发
	 */
	void seekTo(int progress, boolean fromSeekBar, boolean fromSeekBarByUser);
	/**
	 * 歌词放大
	 */
	boolean zoomIn();

	/**
	 * 歌词缩小
	 */
	boolean zoomOut();

	/**
	 * 重置
	 */
	void reset();
}
