package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class Stage1Activity extends AppCompatActivity {
    private GameView1 gameView1;
    private Stage1Activity stage1Activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        stage1Activity=this;
        gameView1 = new GameView1(stage1Activity, point.x, point.y);

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