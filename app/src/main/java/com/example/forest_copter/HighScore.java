package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        SharedPreferences pref=getSharedPreferences("Game",MODE_PRIVATE);
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
       score1.setText(pref.getString("score1","-"));
       score2.setText(pref.getString("score2","-"));
       score3.setText(pref.getString("score3","-"));
       current_name.setText(pref.getString("Last_User_Name","-"));
       current_score.setText(pref.getString("Last_User_Score","-"));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(HighScore.this,MainActivity.class));
    }

}