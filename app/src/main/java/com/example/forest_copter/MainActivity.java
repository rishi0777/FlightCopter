package com.example.forest_copter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ListMenuPresenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
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
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isPressed = false;
    private boolean isMute = false;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).
                    setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build();
            soundPool = new SoundPool.Builder().setMaxStreams(2).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        final Toast toast = Toast.makeText(this, "You Already Pressed One Button", Toast.LENGTH_SHORT);
        final Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        final Animation shake_b = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_back);
        final Handler handler = new Handler();

        final int sound1 = soundPool.load(this, R.raw.button_press_music, 1);
        final SharedPreferences pref = getSharedPreferences("Game", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        //editor.clear();
        //editor.apply();

        final ImageView vol_ctrl = findViewById(R.id.vol_control);
        isMute = pref.getBoolean("isMute", false);

        if (isMute) {
            vol_ctrl.setImageResource(R.drawable.ic_baseline_volume_off_24);

        }
        else {
            vol_ctrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
            start_Player();
        }


        //MUTE Button
        vol_ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;
                if (isMute) {
                    vol_ctrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    pause_Player();
                }
                else {
                    vol_ctrl.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    start_Player();
                }

                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });


        //PLAY Button
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed) {
                    findViewById(R.id.button_box).startAnimation(shake_b);
                    isPressed = true;
                    if (!isMute) {
                        soundPool.play(sound1, 1, 1, 1, 0, 1);
                        stop_Player();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(new Intent(MainActivity.this,NameScreen.class));
                        }
                    },1275);


                }
                else {
                    toast.show();
                }
            }
        });

        //HighScore Button
        findViewById(R.id.highscore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed) {
                    isPressed = true;
                    if (!isMute) {// agar mute nahi hai to song chal raha hoga
                        soundPool.play(sound1, 1, 1, 1, 0, 1);
                        stop_Player();
                    }
                    view.startAnimation(shake);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(new Intent(MainActivity.this, HighScore.class));

                        }
                    }, 1275);
                } else {
                    toast.show();
                }
            }
        });

        //About Button
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isPressed) {
                    isPressed = true;
                    if (!isMute) {
                        soundPool.play(sound1, 1, 1, 1, 0, 1);
                        stop_Player();
                    }
                    view.startAnimation(shake);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(new Intent(MainActivity.this, About.class));

                        }
                    }, 1275);
                } else {
                    toast.show();
                }
            }
        });


        //Exit Button
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPressed) {
                    isPressed = true;
                    if (!isMute){
                        soundPool.play(sound1, 1, 1, 1, 0, 1);
                        stop_Player();
                    }
                    view.startAnimation(shake);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1275);
                } else {
                    toast.show();
                }
            }
        });


    }



    private void start_Player(){
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(this,R.raw.main_background);
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

    @Override//When activity stops
    protected void onStop() {
        super.onStop();
        stop_Player();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isMute) {
            start_Player();
        }
    }
}