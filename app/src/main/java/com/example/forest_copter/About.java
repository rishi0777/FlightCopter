package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;

public class About extends AppCompatActivity {

    private SharedPreferences pref;
    private MediaPlayer mediaPlayer;
    private Boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        pref=getSharedPreferences("Game",MODE_PRIVATE);
        isMute=pref.getBoolean("isMute",false);
        if(!isMute) {
            start_Player();
        }
    }


    private void start_Player(){
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(this,R.raw.about_background);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop_Player();
                    start_Player();
                }
            });
        }
        mediaPlayer.start();
    }
    private void pause_Player(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }
    private void stop_Player(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isMute) {
            stop_Player();
        }
        finish();
        startActivity(new Intent(About.this,MainActivity.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isMute) {
            pause_Player();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isMute) {
            start_Player();
        }
    }
}