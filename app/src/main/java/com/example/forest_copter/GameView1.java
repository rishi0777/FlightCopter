package  com.example.forest_copter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView1 extends SurfaceView implements Runnable {

    private Thread thread;

    private boolean isPlaying, isGameOver = false;
    private boolean isMute=false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private int shoot_Sound,game_Over_Sound;


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private SoundPool soundPool;
    private Random random;
    private Paint paint;

    private List<Bullet> bullets;
    private Bird[] birds;
    private Flight flight;
    private Stage1Activity activity;
    private Background background1, background2;
    private GameOver gameOver;

    public GameView1(Stage1Activity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;

        //Initializing reference variable of Shared Preferences
        prefs = activity.getSharedPreferences("Game", Context.MODE_PRIVATE);
        isMute=prefs.getBoolean("isMute",false);
        //Initializing reference variable of Shared Preferences Editor
        editor=prefs.edit();

        //Creating object of Sound Pool class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        }
        else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        //Loading sound and then saving that reference in to int variables
        shoot_Sound = soundPool.load(activity, R.raw.bullet_shot_music, 1);
        game_Over_Sound = soundPool.load(activity, R.raw.gameover_music, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        //Creating object of background class and then initializing that object into the reference variable of background class
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;//placing background2 at the end of screen

        //Creating object of gameover class and then initializing that object into the reference variable of gameover class
        gameOver=new GameOver(getResources(),screenX,screenY);

        //Creating object of flight class and then initializing that object into the reference variable of flight class
        flight = new Flight(this, screenY, getResources());

        //Creating objects of bullet class and then initializing those object into the array of  reference variables of bullet class
        bullets = new ArrayList<>();

        //Creating object of paint class and then initializing that object into the reference variable of paint class
        paint = new Paint();
        paint.setTextSize(111);
        paint.setColor(Color.BLACK);

        //Creating object of random class and then initializing that object into the reference variable of random class
        random = new Random();

        birds = new Bird[4];
        String st1_bird1[]={"st1_bird1_1","st1_bird1_2"};
        String st1_bird2[]={"st1_bird2_1","st1_bird2_2","st1_bird2_3","st1_bird2_4"};
        int bird_size=random.nextInt(4);


        for (int i = 0;i < 4;i++) {
            Bird bird;
            if(bird_size!=0) {
                bird = new Bird(activity, getResources(), st1_bird1);
                bird_size--;
            }
            else{
                bird = new Bird(activity, getResources(), st1_bird2);
            }
            birds[i] = bird;
        }



    }

    @Override
    public void run() {

        while (isPlaying) {

            update ();
            draw ();
            sleep ();

        }

    }

    private void update () {

        //updating the position of background
        background1.x -= 10 ;
        background2.x -= 10 ;

        if (background1.x + background1.stage1_background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.stage1_background.getWidth() < 0) {
            background2.x = screenX;
        }

        //updating position of flight
        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        //updating position of bullet and checking if bullet intersects with bird
        for (Bullet bullet : bullets) {
            if (bullet.x > screenX)
                trash.add(bullet);
            bullet.x += 50 * screenRatioX;
            for (Bird bird : birds) {
                if (Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {
                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;
                }
            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        //placing bird on the screen when it goes off the screen and checking if bird intersects with the flight or not
        for (Bird bird : birds) {
            bird.x -= bird.speed;
            if (bird.x  < 0) {
                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);

                bird.wasShot = false;
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.stage1_background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.stage1_background, background2.x, background2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                if(!isMute)
                    soundPool.play(game_Over_Sound,1,1,1,0,1);
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                canvas.drawBitmap(gameOver.gameover, gameOver.x, gameOver.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                //saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

       /* if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
*/
    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count=event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;

            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                break;

            case MotionEvent.ACTION_POINTER_2_DOWN:
                if (event.getX(1) < screenX / 2) {
                    flight.isGoingUp = true;
                }
                if (event.getX(1) > screenX / 2) {
                    flight.toShoot++;
                }
                break;
            case MotionEvent.ACTION_POINTER_2_UP:
                if (event.getX(1) < screenX / 2) {
                    flight.isGoingUp = false;
                }

        }


        return true;
    }


    public void newBullet() {

        if (!isMute)
            soundPool.play(shoot_Sound, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}