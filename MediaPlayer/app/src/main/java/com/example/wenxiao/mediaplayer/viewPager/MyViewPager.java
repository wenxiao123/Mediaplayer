package com.example.wenxiao.mediaplayer.viewPager;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by ZhangYang on 2017/5/27.
 */

public class MyViewPager extends ViewPager {

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent p_event)
//    {
//        return true;
//    }

//    private PointF sp = new PointF();
//    @Override
//    public boolean onTouchEvent(MotionEvent arg0) {
//        // TODO Auto-generated method stub
//        int action = arg0.getAction();
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                sp = new PointF(arg0.getX(), arg0.getY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                PointF ep = new PointF(arg0.getX(), arg0.getY());
//                float move_x = sp.x - ep.x;
//                if(!(move_x < 0 && getCurrentItem() == 0) && getParent() != null
//                        && !(move_x > 0 && getCurrentItem() == getAdapter().getCount() - 1)
//                        && sp.x > 50){
//                    Log.i("DEBUG", "intercept move event");
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//
//        }
//
//        return super.onTouchEvent(arg0);
//    }
//



}
