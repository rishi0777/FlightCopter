package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Boolean isMute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        SharedPreferences pref=getSharedPreferences("Game",MODE_PRIVATE);
        isMute=pref.getBoolean("isMute",false);
       SharedPreferences.Editor editor=pref.edit();

       TextView name1=findViewById(R.id.name1);
       TextView name2=findViewById(R.id.name2);
       TextView name3=findViewById(R.id.name3);
       TextView score1=findViewById(R.id.score1);
       TextView score2=findViewById(R.id.score2);
       TextView score3=findViewById(R.id.score3);
       TextView current_name=findViewById(R.id.cur_user);
       TextView current_score=findViewById(R.id.cur_score);

       name1.setText(pref.getString("d_name1","-"));
       name2.setText(pref.getString("d_name2","-"));
       name3.setText(pref.getString("d_name3","-"));

       score1.setText(String.valueOf(pref.getInt("score1", 0)));
       score2.setText(String.valueOf(pref.getInt("score2", 0)));
       score3.setText(String.valueOf(pref.getInt("score3", 0)));
       current_name.setText(pref.getString("Last_User_Name", "-"));
       current_score.setText(String.valueOf(pref.getInt("Last_User_Score", 0)));


    }

    private void start_Player(){
        if(mediaPlayer==null){
            mediaPlayer= MediaPlayer.create(this,R.raw.high_background);
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
        if(mediaPlayer!=null) {
            mediaPlayer.pause();
        }
    }
    private void stop_Player(){
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isMute) {
            start_Player();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isMute) {
            pause_Player();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isMute) {
            stop_Player();
        }
        finish();
        startActivity(new Intent(HighScore.this,MainActivity.class));
    }
}