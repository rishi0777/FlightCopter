package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class Stage1Activity extends AppCompatActivity {
    private GameView1 gameView1;
    private Stage1Activity stage1Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        stage1Activity=this;
        Point point=new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.getDisplay().getRealSize(point);
            gameView1 = new GameView1(stage1Activity,point.x,point.y);
        }
        else {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            int height = Resources.getSystem().getDisplayMetrics().heightPixels;
            gameView1 = new GameView1(stage1Activity, width, height);
        }



        setContentView(gameView1);

    }
    @Override
    protected void onResume() {
        super.onResume();
        gameView1.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView1.pause();
    }
}