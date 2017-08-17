package com.example.wenxiao.mediaplayer.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.wenxiao.mediaplayer.R;
import com.nineoldandroids.view.ViewHelper;


/**
 * Created by ZhangYang on 2017/5/25.
 */

public class SlidingMenu extends HorizontalScrollView{
    private LinearLayout mWapper;
    private ViewGroup mMenu,mContent;
    private int mScreenWidth;
    private int mMenuWidth;
    //px
    private int mMenuRigthPadding = 50;
    private boolean isOpen;
    private boolean once;
    /**
     * 未使用自定义属性时，调用此构造(系统默认调用的是此构造)
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 当使用了自定义属性时，会调用此方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取我们定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i = 0;i<n;i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.SlidingMenu_rightPadding:
                 mMenuRigthPadding =  a.getDimensionPixelSize(attr,
                         (int) TypedValue.applyDimension(
                                 TypedValue.COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        //获得控件的宽度（px）
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    /**
     * 设置子View的宽和高，设置自己的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once)
        {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRigthPadding;
            Log.e("-->", "---->"+mMenuWidth );
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 通过设置偏移量，隐藏Menu
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed)
        {
            //改变x轴
            scrollTo(mMenuWidth,0);
        }
    }

    /**
     * 打开菜单
     */
    public void openMenu()
    {
        if (isOpen)
        {
            return;
        }
        smoothScrollTo(0,0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     * @return
     */
   public void closeMenu()
   {
       if (!isOpen)
       {
           return;
       }
       smoothScrollTo(mMenuWidth,0);
       isOpen = false;
   }

    /**
     * 切换菜单
     * @return
     */
    public void toggle()
    {
        if (isOpen)
        {
            closeMenu();
        }else {
            openMenu();
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                //隐藏在左边的宽度
                int scollX = getScrollX();
                if (scollX>=mMenuWidth/2)
                {
                    smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }else
                {
                    smoothScrollTo(0,0);
                    isOpen = true;
                }
                return  true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动发生时
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     * 改变Menu和Content的大小、透明度
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float scale = l*1.0f/mMenuWidth;//1~0
        Log.e("-->", "---->mMenuWidth"+mMenuWidth );
        Log.e("-->", "---->scale"+scale );
        /**
         * 区别1：内容区域1.0~0.7   scale:  1.0~.0.0  0.7+0.3*scale
         * 区别2：菜单的偏移量需要修改
         * 区别3：菜单显示时有缩放以及透明度变化 缩放：0.7~1.0   1.0-scale*0.3
         *          透明度 0.6~1.0；  0.6+0.4*（1-scale）;
         */
        float rightScale = 0.7f+0.3f*scale;
        float rightAlpha = 1.0f-0.5f*(1-scale);
        float leftScale = 1.0f-scale*0.3f;
        float leftAlpha = 0.6f+0.4f*(1-scale);

        //调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu,mMenuWidth*scale*0.8f);

        ViewHelper.setScaleX(mMenu,leftScale);
        ViewHelper.setScaleY(mMenu,leftScale);
        ViewHelper.setAlpha(mMenu,leftAlpha);
        //设置content的缩放中心点
        ViewHelper.setPivotX(mContent,0);
        ViewHelper.setPivotY(mContent,mContent.getHeight()/2);
        ViewHelper.setScaleX(mContent,rightScale);
        ViewHelper.setScaleY(mContent,rightScale);
        ViewHelper.setAlpha(mContent,rightAlpha);

    }
}
