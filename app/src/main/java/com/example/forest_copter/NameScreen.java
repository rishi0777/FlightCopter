package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

public class NameScreen extends AppCompatActivity {

    private SoundPool soundPool;
    private Animation shake;
    private int sound1;
    private boolean isPressed=false;
    private boolean isMute;
    private String is_name_entered;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);




        SharedPreferences pref=getSharedPreferences("Game",MODE_PRIVATE);
        isMute=pref.getBoolean("isMute",false);
        final SharedPreferences.Editor editor=pref.edit();

        final Toast toast1 = Toast.makeText(this, "First Enter Your Name", Toast.LENGTH_SHORT);
        final Toast toast2 = Toast.makeText(this, "You Already Clicked One Button", Toast.LENGTH_SHORT);
        final Toast toast3 = Toast.makeText(this, "Click On Stage 1 Image", Toast.LENGTH_SHORT);
        final Toast toast4 = Toast.makeText(this, "Click On Stage 2 Image", Toast.LENGTH_SHORT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).
                    setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build();
            soundPool = new SoundPool.Builder().setMaxStreams(2).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        sound1=soundPool.load(this,R.raw.button_press_music,1);

        shake= AnimationUtils.loadAnimation(NameScreen.this,R.anim.shake);
        final EditText editText=findViewById(R.id.write_name);


        handler=new Handler();
        findViewById(R.id.stage1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_name_entered=editText.getText().toString();
                if(is_name_entered.matches("")|is_name_entered.matches("NAME")){
                    toast1.show();
                }
                  else{
                      editor.putString("Temp_User_Name",is_name_entered);
                      editor.apply();
                      if (!isPressed) {
                        view.startAnimation(shake);
                        findViewById(R.id.label_stage1).startAnimation(shake);
                        isPressed = true;
                        if (!isMute) {
                            stop_Player();
                            soundPool.play(sound1, 1, 1, 1, 0, 1);
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(new Intent(NameScreen.this,Stage1Activity.class));

                            }
                        }, 1275);


                    } else {
                        toast2.show();
                    }
                }
            }
        });
        findViewById(R.id.stage2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_name_entered = editText.getText().toString();
                if (is_name_entered.matches("") | is_name_entered.matches("NAME")){
                    toast1.show();
                }
                else
                {
                    if (!isPressed) {
                        view.startAnimation(shake);
                        findViewById(R.id.label_stage2).startAnimation(shake);
                        editor.putString("Temp_User_Name",is_name_entered);
                        editor.apply();

                        isPressed = true;
                        if (!isMute) {
                            stop_Player();
                            soundPool.play(sound1, 1, 1, 1, 0, 1);
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                startActivity(new Intent(NameScreen.this,HighScore.class));
                            }
                        },1275);


                    }
                    else{
                        toast2.show();
                    }
                }

            }
        });
        findViewById(R.id.label_stage1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast3.show();
            }
        });
        findViewById(R.id.label_stage2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast4.show();
            }
        });


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
        startActivity(new Intent(NameScreen.this,MainActivity.class));
    }
}