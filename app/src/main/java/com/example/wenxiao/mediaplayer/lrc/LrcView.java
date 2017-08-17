package com.example.wenxiao.mediaplayer.lrc;

import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.AlphabetIndexer;
import android.widget.Scroller;
import android.widget.SectionIndexer;

/***
 * 1:歌词的上下平移用什么实现？
 *    用Scroller实现
 *    在ViewGroup里面 scrollTo等方法移动的是里面的内容，
 *    在View里面scrollTo等方法移动的也是里面的内容
 * @author Administrator
 *
 */
public class LrcView extends View implements ILrcView{
	/**所有的歌词***/
	private List<LrcRow> mLrcRows;
	/**无歌词数据的时候 显示的默认文字**/
	private static final String DEFAULT_TEXT = "学习使我快乐";
	/**默认文字的字体大小**/
	private static final float SIZE_FOR_DEFAULT_TEXT = 25;

	/**画高亮歌词的画笔***/
	private Paint mPaintForHighLightLrc;
	/**高亮歌词的默认字体大小***/
	private static final float DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC = 35;
	/**高亮歌词当前的字体大小***/
	private float mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC;
	/**高亮歌词的默认字体颜色**/
	private static final int DEFAULT_COLOR_FOR_HIGHT_LIGHT_LRC = 0xffEE4000;
	/**高亮歌词当前的字体颜色**/
	private int mCurColorForHightLightLrc = DEFAULT_COLOR_FOR_HIGHT_LIGHT_LRC;

	/**画其他歌词的画笔***/
	private Paint mPaintForOtherLrc;
	/**其他歌词的默认字体大小***/
	private static final float DEFAULT_SIZE_FOR_OTHER_LRC = 30;
	/**其他歌词当前的字体大小***/
	private float mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC;
	/**高亮歌词的默认字体颜色**/
	private static final int DEFAULT_COLOR_FOR_OTHER_LRC = Color.WHITE;
	/**高亮歌词当前的字体颜色**/
	private int mCurColorForOtherLrc = DEFAULT_COLOR_FOR_OTHER_LRC;


	/**画时间线的画笔***/
	private Paint mPaintForTimeLine;
	/***时间线的颜色**/
	private static final int COLOR_FOR_TIME_LINE = 0xffffffff;
	/**时间文字大小**/
	private static final int SIZE_FOR_TIME = 18;
	/**是否画时间线**/
	private boolean mIsDrawTimeLine = false;

	/**歌词间默认的行距**/
	private static final float DEFAULT_PADDING = 20;
	/**歌词当前的行距**/
	private float mCurPadding = DEFAULT_PADDING;

	/**歌词的最大缩放比例**/
	public static final float MAX_SCALING_FACTOR = 1.5f;
	/**歌词的最小缩放比例**/
	public static final float MIN_SCALING_FACTOR = 0.5f;
	/**默认缩放比例**/
	public static final float DEFAULT_SCALING_FACTOR = 1.0f;
	/**当前歌词的缩放比例**/
	private float mCurScalingFactor = DEFAULT_SCALING_FACTOR;

	/**实现歌词竖直方向平滑滚动的辅助对象**/
	private Scroller mScroller;
	/***移动一句歌词的持续时间**/
	private static final int DURATION_FOR_LRC_SCROLL = 1500;
	/***停止触摸时 如果需要滚动 时的持续时间**/
	private static final int DURATION_FOR_ACTION_UP = 400;

	/**控制文字缩放的因子**/
	private float mCurFraction = 0;
	private int mTouchSlop;



	public LrcView(Context context) {
		super(context);
		init();
	}
	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	@Override
	public void init() {
		mScroller = new Scroller(getContext(), new LinearInterpolator());
		mPaintForHighLightLrc = new Paint();
		mPaintForHighLightLrc.setColor(mCurColorForHightLightLrc);
		mPaintForHighLightLrc.setTextSize(mCurSizeForHightLightLrc);

		mPaintForOtherLrc = new Paint();
		mPaintForOtherLrc.setColor(mCurColorForOtherLrc);
		mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);

		mPaintForTimeLine = new Paint();
		mPaintForTimeLine.setColor(COLOR_FOR_TIME_LINE);
		mPaintForTimeLine.setTextSize(dip2px(this,SIZE_FOR_TIME));

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	private int mTotleDrawRow = 15;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mLrcRows == null || mLrcRows.size() == 0){
			//画默认的显示文字
			mPaintForOtherLrc.setTextSize(dip2px(this,SIZE_FOR_DEFAULT_TEXT));
			float textWidth = mPaintForOtherLrc.measureText(DEFAULT_TEXT);
			float textX = (getWidth()-textWidth)/2;
			canvas.drawText(DEFAULT_TEXT, textX, getHeight()/2, mPaintForOtherLrc);
			return;
		}
		int minRaw = mCurRow - (mTotleDrawRow-1)/2;
		int maxRaw = mCurRow + (mTotleDrawRow-1)/2;
		minRaw = Math.max(minRaw, 0);
		maxRaw = Math.min(maxRaw, mLrcRows.size()-1);
		float rowY = getHeight()/2 + minRaw*(mCurSizeForOtherLrc + mCurPadding);
		for (int i = minRaw; i <= maxRaw; i++) {

			if(i == mCurRow){//画高亮歌词
				//因为有缩放效果，所有需要动态设置歌词的字体大小
				float textSize = mCurSizeForOtherLrc + (mCurSizeForHightLightLrc - mCurSizeForOtherLrc)*mCurFraction;
				mPaintForHighLightLrc.setTextSize(textSize);

				//因为有水平滚动效果，所以需要动态的设置歌词的x轴起始坐标

				//				mCurTextXForHighLightLrc = Math.min(textX, mCurTextXForHighLightLrc);

				String text = mLrcRows.get(i).getContent();
				float textWidth = mPaintForHighLightLrc.measureText(text);
				if(textWidth > getWidth()){
					canvas.drawText(text, mCurTextXForHighLightLrc, rowY, mPaintForHighLightLrc);
				}else{
					float textX =  Math.max((getWidth()-textWidth)/2, 0);
					canvas.drawText(text, textX, rowY, mPaintForHighLightLrc);
				}
			}else{
				if(i == mCurRow - 1){//画高亮歌词的上一句
					//因为有缩放效果，所有需要动态设置歌词的字体大小
					float textSize = mCurSizeForHightLightLrc - (mCurSizeForHightLightLrc - mCurSizeForOtherLrc)*mCurFraction;
					mPaintForOtherLrc.setTextSize(textSize);
				}else{//画其他的歌词

					mPaintForOtherLrc.setTextSize(mCurSizeForOtherLrc);

				}
				String text = mLrcRows.get(i).getContent();
				float textWidth = mPaintForOtherLrc.measureText(text);
				float textX = (getWidth()-textWidth)/2;
				textX = Math.max(textX, 0);
				canvas.drawText(text, textX, rowY, mPaintForOtherLrc);
			}
			rowY += mCurSizeForOtherLrc + mCurPadding;
		}


		if(mIsDrawTimeLine){
			canvas.drawText(mLrcRows.get(mCurRow).getTimeStr(), 0, getHeight()/2+getScrollY()-3, mPaintForTimeLine);
			canvas.drawLine(0, getHeight()/2+getScrollY(), getWidth(), getHeight()/2+getScrollY(), mPaintForTimeLine);
		}
	}
	/**代表能否拖动歌词**/
	private boolean canDrag = false;
	/**触摸事件的第一次Y坐标**/
	private float firstY;
	/**触摸事件上一次的Y坐标**/
	private float lastY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//记录下事件的首次Y坐标
				firstY = lastY = event.getRawY();

				break;
			case MotionEvent.ACTION_MOVE:
				if(!canDrag){
					//判断是否要设置为可拖动
					if(Math.abs(event.getRawY()-firstY) > mTouchSlop){
						canDrag = true;
						mIsDrawTimeLine = true;
						if(!mScroller.isFinished()){
							mScroller.forceFinished(true);
						}
					}
				}

				if(canDrag){//执行滚动效果
					//事件y坐标偏移量
					float offset = event.getRawY() - lastY;
					//处理上边界
					if(getScrollY()-offset < 0 ){
						offset = offset/3;
					}
					//处理下边界
					if(getScrollY() - offset > (mLrcRows.size()-1)*(mCurSizeForOtherLrc+mCurPadding)){
						offset = offset/3;
					}
					scrollBy(0, (int) -offset);

					int currentRow = (int) (getScrollY()/(mCurSizeForOtherLrc+mCurPadding));
					currentRow = Math.max(currentRow, 0);
					currentRow = Math.min(currentRow, mLrcRows.size()-1);
					seekTo(mLrcRows.get(currentRow).getTime(), false,false);
					lastY = event.getRawY();
				}

				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(canDrag ){
					if(onLrcSeekToListener != null){
						onLrcSeekToListener.onLrcSeekTo(mLrcRows.get(mCurRow).getTime());
					}
				}else{
					//回调单击监听
					if(onLrcClickListener != null){
						onLrcClickListener.onLrcClick();
					}
				}

				canDrag = false;
				if(getScrollY() < 0 ){
					smoothScrollTo(0, DURATION_FOR_ACTION_UP);
				}else if(getScrollY() > (mLrcRows.size()-1)*(mCurSizeForOtherLrc+mCurPadding)){
					smoothScrollTo((int) ((mLrcRows.size()-1)*(mCurSizeForOtherLrc+mCurPadding)), DURATION_FOR_ACTION_UP);
				}
				mIsDrawTimeLine = false;
				break;
		}
		return true;
	}

	@Override
	public void setLrcRows(List<LrcRow> lrcRows) {
		this.mLrcRows = lrcRows;
		invalidate();

	}

	private int mCurRow = 1;
	/***
	 *  400
	 *  700
	 *  1200
	 *  3500
	 *  5000
	 *
	 */
	@Override
	public void seekTo(int progress,boolean fromSeekBar,boolean fromSeekBarByUser) {
		if(mLrcRows == null || mLrcRows.size() == 0){
			return;
		}
		//如果是由seekbar的进度改变触发 并且这时候处于拖动状态
		if(fromSeekBar && canDrag){
			return;
		}

		for (int i = mLrcRows.size()-1; i >= 0; i--) {

			if(progress >= mLrcRows.get(i).getTime()){
				if(mCurRow != i){
					mCurRow = i;
					log("mCurRow=i="+mCurRow);
					if(fromSeekBarByUser){
						if(!mScroller.isFinished()){
							mScroller.forceFinished(true);
						}
						scrollTo(getScrollX(), (int) (mCurRow * (mCurSizeForOtherLrc+mCurPadding)));
					}else{
						smoothScrollTo( (int) (mCurRow * (mCurSizeForOtherLrc+mCurPadding)), DURATION_FOR_LRC_SCROLL);
					}

					//如果高亮歌词的宽度大于View的宽，就需要开启属性动画，让它水平滚动
					float textWidth = mPaintForHighLightLrc.measureText(mLrcRows.get(mCurRow).getContent());
					if(textWidth > getWidth()){
						startScrollLrc(getWidth()-textWidth, (long) (mLrcRows.get(mCurRow).getTotalTime()*0.6));
					}
					invalidate();
				}
				break;
			}
		}

	}

	/**控制歌词水平滚动的属性动画***/
	private ValueAnimator mAnimator;
	/**
	 * 开始水平滚动歌词
	 * @param endX 歌词第一个字的最终的x坐标
	 * @param duration 滚动的持续时间
	 */
	private void startScrollLrc(float endX,long duration){
		if(mAnimator == null){
			mAnimator = ValueAnimator.ofFloat(0,endX);
			mAnimator.addUpdateListener(updateListener);
		}else{
			mCurTextXForHighLightLrc = 0;
			mAnimator.cancel();
			mAnimator.setFloatValues(0,endX);
		}
		mAnimator.setDuration(duration);
		mAnimator.setStartDelay((long) (duration*0.3));
		mAnimator.start();
	}
	private float mCurTextXForHighLightLrc;
	/***
	 * 监听属性动画的数值值的改变
	 */
	AnimatorUpdateListener updateListener = new AnimatorUpdateListener() {

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			//TODO
			mCurTextXForHighLightLrc = (Float) animation.getAnimatedValue();
			invalidate();
		}
	};
	@Override
	public boolean zoomIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoomOut() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void log(Object o){
		Log.d("LrcView", o+"");
	}


	/**
	 * 平滑的移动到某处
	 * @param des
	 */
	private void smoothScrollTo(int des,int duration){
		int oldScrollY = getScrollY();
		int dx = des - oldScrollY;
		mScroller.startScroll(getScrollX(), oldScrollY, getScrollX(), dx, duration);
		invalidate();
	}
	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldY = getScrollY();
				int y = mScroller.getCurrY();
				if (oldY != y) {
					scrollTo(getScrollX(), y);
				}
				mCurFraction = mScroller.timePassed()*3f/DURATION_FOR_LRC_SCROLL;
				mCurFraction = Math.min(mCurFraction, 1F);
				invalidate();
			}
		}
	}

//单位换算
	public static int dip2px(LrcView context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics()
				.density;
		return (int) (dipValue*scale+0.5f);
	}
	public static int px2dip(Context context,float pxValue){
		final float scale = context.getResources().getDisplayMetrics()
				.density;
		return (int) (pxValue/scale+0.5f);
	}
	private OnLrcSeekToListener onLrcSeekToListener;
	public void setOnLrcSeekToListener(OnLrcSeekToListener onLrcSeekToListener) {
		this.onLrcSeekToListener = onLrcSeekToListener;
	}
	public interface OnLrcSeekToListener{
		void onLrcSeekTo(int progress);
	}
	public float getmCurScalingFactor() {
		return mCurScalingFactor;
	}
	public void setmCurScalingFactor(float mCurScalingFactor) {
		this.mCurScalingFactor = mCurScalingFactor;
		//根据缩放比例 设置歌词文字和行距的大小
		mCurSizeForHightLightLrc = DEFAULT_SIZE_FOR_HIGHT_LIGHT_LRC * mCurScalingFactor;
		mCurSizeForOtherLrc = DEFAULT_SIZE_FOR_OTHER_LRC * mCurScalingFactor;
		mCurPadding = DEFAULT_PADDING * mCurScalingFactor;
		mTotleDrawRow = (int) (getHeight()/(mCurSizeForOtherLrc+mCurPadding))+3;
		mScroller.forceFinished(true);
		scrollTo(getScrollX(), (int) (mCurRow*(mCurSizeForOtherLrc+mCurPadding)));
		invalidate();
	}

	private OnLrcClickListener onLrcClickListener;
	public void setOnLrcClickListener(OnLrcClickListener onLrcClickListener) {
		this.onLrcClickListener = onLrcClickListener;
	}
	public interface OnLrcClickListener{
		void onLrcClick();
	}

}
