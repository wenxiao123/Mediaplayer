package com.example.wenxiao.mediaplayer;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = drawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1-slideOffset;
                float rightScale = 0.6f+scale*0.4f;  //1~0.6
                float leftScrale = 0.5f+slideOffset*0.5f;
                //mMenu.setAlpha(leftScrale);
                mMenu.setScaleX(leftScrale);
                mMenu.setScaleY(leftScrale);
                mContent.setPivotX(0);
                mContent.setPivotY(mContent.getHeight()/2);
                mContent.setScaleX(rightScale);
                mContent.setScaleY(rightScale);
                mContent.setTranslationX(mMenu.getWidth()*slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断返回键
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
        //return false;
    }
}

