package  com.example.forest_copter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView1 extends SurfaceView implements Runnable {

    private Thread thread;

    private boolean isPlaying, isGameOver = false;
    private boolean dragon_On_Screen = false;
    public static boolean dragon_fire_on_screen = false, fire_out_of_mouth = false, dragon_incoming = false;
    private boolean isMute = false;
    private boolean stageIsClear = false;
    private boolean is_Stage_Clear_Sound_Played=false,battle_Music_Playing=false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private int bullet_Sound, game_Over_Sound, bird_Passed_Sound, dragon_got_hit_Sound, dragon_fire_on_screen_Sound
            ,stage_clear_sound;
    private int margin = 2;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private Random random;
    private Paint paint, paint_Health_Color, paint_Health_Border;

    private DragonDead dragon_dead;
    private DragonFire dragonFire;
    private Dragon dragon;
    private Dragon_Incoming dragon_incoming_obj;
    private Life lifeLine;
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
        isMute = prefs.getBoolean("isMute", false);
        //Initializing reference variable of Shared Preferences Editor
        editor = prefs.edit();

        //Creating object of Sound Pool class
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder().setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        //Loading sound and then saving that reference in to int variables
        bird_Passed_Sound = soundPool.load(activity, R.raw.bird_passed, 1);
        bullet_Sound = soundPool.load(activity, R.raw.bullet_shot_music, 1);
        game_Over_Sound = soundPool.load(activity, R.raw.gameover_music, 1);
        dragon_got_hit_Sound = soundPool.load(activity, R.raw.dragon_got_hit, 1);
        dragon_fire_on_screen_Sound = soundPool.load(activity, R.raw.dragon_fire_sound, 1);
        stage_clear_sound=soundPool.load(activity,R.raw.stage_clear,1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        //Creating object of background class and then initializing that object into the reference variable of background class
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;//placing background2 at the end of screen

        //Creating object of gameover class and then initializing that object into the reference variable of gameover class
        gameOver = new GameOver(getResources(), screenX, screenY);

        //Creating object of flight class and then initializing that object into the reference variable of flight class
        flight = new Flight(this, screenY, getResources());

        //Creating objects of bullet class and then initializing those object into the array of  reference variables of bullet class
        bullets = new ArrayList<>();

        //Creating object of paint class and then initializing that object into the reference variable of paint class
        paint = new Paint();
        paint_Health_Color = new Paint();
        paint_Health_Border = new Paint();

        //setting color of paint object
        paint_Health_Border.setColor(Color.GRAY);
        paint_Health_Color.setColor(Color.RED);
        paint.setColor(Color.BLACK);

        //setting font of paint object
        Typeface typeface1, typeface2;
        typeface1 = ResourcesCompat.getFont(activity, R.font.aladin);
        typeface2 = ResourcesCompat.getFont(activity, R.font.bangers);

        paint.setTextSize(100);
        paint_Health_Color.setTextSize(100);
        paint_Health_Color.setTypeface(typeface1);
        paint.setTypeface(typeface2);


        //Creating object of random class and then initializing that object into the reference variable of random class
        random = new Random();
        //Creating the object of Life class
        lifeLine = new Life(getResources());

        //Creating object of dragon_incoming class
        dragon_incoming_obj = new Dragon_Incoming(activity, getResources(), screenX, screenY);

        //Creating object of dragon class
        dragon = new Dragon(getResources(), activity);
        dragon.x = screenX - dragon.width;//setting initial position of dragon
        dragon.y = (screenY - dragon.height) / 2;

        //Creating an object of dragonFire
        dragonFire = new DragonFire(getResources(), dragon.x);

        //Creating ann object of DragonDead
        dragon_dead=new DragonDead(getResources(),activity);

        //Creating 4 objects of bird that will be displayed at a time on the screen
        birds = new Bird[4];
        String st1_bird1[] = {"st1_bird1_1", "st1_bird1_2"};
        String st1_bird2[] = {"st1_bird2_1", "st1_bird2_2", "st1_bird2_3", "st1_bird2_4"};
        int bird_size = random.nextInt(4);


        for (int i = 0; i < 4; i++) {
            Bird bird;
            if (bird_size != 0) {
                bird = new Bird(activity, getResources(), st1_bird1);
                bird_size--;
            } else {
                bird = new Bird(activity, getResources(), st1_bird2);
            }
            birds[i] = bird;

        }
    }

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        if (!stageIsClear) {
            //updating the position of background
            background1.x -= 10;
            background2.x -= 10;

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

            if (flight.y - lifeLine.height < 0)
                flight.y = lifeLine.height;

            if (flight.y >= screenY - flight.height - 30)
                flight.y = screenY - flight.height - 30;


            //updating position of dragon fire
            if (dragon_fire_on_screen) {
                dragonFire.x -= 50 * screenRatioX;
                fire_out_of_mouth = false;
                if (dragonFire.x < 0) {
                    dragon_fire_on_screen = false;
                    dragonFire.x = dragon.x - dragonFire.width / 2;
                }
                if (Rect.intersects(flight.getCollisionShape(), dragonFire.getCollisionShape())) {
                    dragon_fire_on_screen = false;
                    dragonFire.x = dragon.x - dragonFire.width / 2;
                    if (!isMute) {
                        soundPool.play(bird_Passed_Sound, 1, 1, 1, 0, 1);
                    }
                    lifeLine.chance--;
                    if (lifeLine.chance == 0) {
                        isGameOver = true;
                    }
                }
            }

            List<Bullet> trash = new ArrayList<>();


            if ((!dragon_On_Screen) && (!dragon_incoming)) {//dragon is not on screen and neither it is coming
                //updating position of bullet and checking if bullet intersects with bird
                for (Bullet bullet : bullets) {
                    if (bullet.x > screenX)
                        trash.add(bullet);
                    bullet.x += 50 * screenRatioX;
                    for (Bird bird : birds) {
                        if (Rect.intersects(bird.getCollisionShape(),
                                bullet.getCollisionShape())) {
                            score++;
                            if (score > 100) {
                                dragon_incoming = true;
                            }
                            bird.x = -500;
                            bullet.x = screenX + 500;
                            bird.wasShot = true;
                        }
                    }
                }
            } else if ((!dragon_On_Screen) && dragon_incoming) {// dragon is not on screen and is coming
                //deleting previously fired bullets by first adding it to the trash array list
                for (Bullet bullet : bullets) {
                    trash.add(bullet);
                }

            } else {//dragon came on screen and in not incoming
                for (Bullet bullet : bullets) {
                    if (bullet.x > screenX)
                        trash.add(bullet);
                    bullet.x += 50 * screenRatioX;
                    if (Rect.intersects(bullet.getCollisionShape(), dragon.getCollisionShape())) {
                        if (!isMute) {
                            soundPool.play(dragon_got_hit_Sound, (float) 0.5, (float) 0.5, 5, 0, 1);
                        }
                        score+=2;
                        dragon.health--;
                        bullet.x = screenX + 500;

                    }
                }
                //to destroy all the bullets when dragon died
                if(dragon.health==0) {
                    for (Bullet bullet : bullets) {
                        trash.add(bullet);
                    }
                }
            }

            //Here i deleted the bullet if it goes out of screen or if dragon is coming then deleting those bullets which i fired before its arrival
            for (Bullet bullet : trash)
                bullets.remove(bullet);


            if (!dragon_On_Screen && !dragon_incoming) {//dragon is not on screen and neither it is coming
                //placing bird on the screen when it goes off the screen and checking if bird intersects with the flight or not
                for (Bird bird : birds) {
                    bird.x -= bird.speed;
                    if (bird.x < 0) {
                        if (!bird.wasShot) {
                            lifeLine.chance--;
                            if (!isMute) {
                                soundPool.play(bird_Passed_Sound, 1, 1, 1, 0, 1);
                            }
                            if (lifeLine.chance == 0) {
                                isGameOver = true;
                                return;
                            }
                        }

                        int bound = (int) (30 * screenRatioX);
                        bird.speed = random.nextInt(bound);

                        if (bird.speed < 10 * screenRatioX)
                            bird.speed = (int) (10 * screenRatioX);

                        bird.x = screenX + bird.width;
                        bird.y = random.nextInt(screenY - bird.height - lifeLine.height) + lifeLine.height;

                        bird.wasShot = false;
                    }

                    if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                        isGameOver = true;
                        bird.bird_got_hit_with_flight = true;
                        return;
                    }
                }
            } else if ((!dragon_On_Screen) && dragon_incoming) {//dragon is coming
                dragon_incoming_obj.x -= 30 * screenRatioX;
                if (dragon_incoming_obj.x < screenX - dragon_incoming_obj.width_dragon) {
                    dragon_incoming = false;
                    dragon_On_Screen = true;
                }
            } else {//dragon came on screen and is not incoming
                //making dragon to move on screen
                if (dragon.y >= screenY - dragon.height) {
                    dragon.y = screenY - dragon.height;
                }

                if (dragon.y < 0) {
                    dragon.y = 0;
                }

                if (flight.isGoingUp) {
                    dragon.y -= 30 * screenRatioY;
                } else {
                    dragon.y += 30 * screenRatioY;
                }

            }
        } else {//stage is cleared here we have to update the position of flight and dragon
            //flight will move in the right direction and dragon will die
            flight.x += 20 * screenRatioX;
            background1.x = 0;
            background1.y = 0;
            background2.x = 0;
            background2.y = 0;

        }

    }








    private void draw() {

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            //drawing background
            canvas.drawBitmap(background1.stage1_background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.stage1_background, background2.x, background2.y, paint);

            //drawing lifelines
            if (lifeLine.chance == 3) {
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x, lifeLine.y, paint);
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x + lifeLine.width, lifeLine.y, paint);
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x + 2 * lifeLine.width, lifeLine.y, paint);
            } else if (lifeLine.chance == 2) {
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x, lifeLine.y, paint);
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x + lifeLine.width, lifeLine.y, paint);
            } else if (lifeLine.chance == 1) {
                canvas.drawBitmap(lifeLine.lifelines(), lifeLine.x, lifeLine.y, paint);
            }
            if (!stageIsClear) {
                if ((!dragon_On_Screen) && (!dragon_incoming)) {//dragon is not on screen and neither it is coming
                    //drawing score
                    canvas.drawText(score + "", screenX - 200, 120, paint);

                    //drawing birds
                    if (isGameOver) {//game gets over
                        for (Bird bird : birds) {
                            if (!bird.bird_got_hit_with_flight)
                                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
                            else {
                                canvas.drawBitmap(bird.getdead(), bird.x, bird.y, paint);
                            }

                        }
                        isPlaying = false;
                        if (!isMute)
                            soundPool.play(game_Over_Sound, 1, 1, 1, 0, 1);
                        canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                        canvas.drawBitmap(gameOver.gameover, gameOver.x, gameOver.y, paint);
                        getHolder().unlockCanvasAndPost(canvas);
                        saveIfHighScore();
                        waitBeforeExiting();
                        return;
                    } else {//game does not get over
                        for (Bird bird : birds)
                            canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
                    }
                    for (Bullet bullet : bullets)
                        canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
                }

                // dragon is not on screen and is coming
                else if ((!dragon_On_Screen) && dragon_incoming) {
                    if(!isMute && !battle_Music_Playing){
                        start_Player();
                        battle_Music_Playing=true;
                    }
                    canvas.drawBitmap(dragon_incoming_obj.dragon_incoming_danger_image, dragon_incoming_obj.x_dr_incoming_danger_image
                            , dragon_incoming_obj.y_dr_incoming_danger_image, paint);
                    canvas.drawText("DRAGON",dragon_incoming_obj.x_dr_incoming_danger_image-50,dragon_incoming_obj.
                                    y_dr_incoming_danger_image+dragon_incoming_obj.height+100,paint_Health_Color);
                    canvas.drawBitmap(dragon_incoming_obj.getIncomingDragon(), dragon_incoming_obj.x, dragon_incoming_obj.y, paint);
                }

                //dragon came on screen
                else {
                    //drawing score
                    canvas.drawText(score + "", screenX - 200, 120, paint);

                    //drawing dragon
                    canvas.drawBitmap(dragon.getDragon(), dragon.x, dragon.y, paint);

                    //health bar of dragon
                    float percent = (float) dragon.health / dragon.max_Health_point;
                    canvas.drawRect(10, screenY - 35, dragon.width, screenY - 5, paint_Health_Border);

                    if (dragon.health != 0) {
                        canvas.drawRect(10 + 4 * margin, screenY - 35 + 4 * margin,
                                percent * dragon.width - 4 * margin, screenY - 5 - 4 * margin, paint_Health_Color);
                    } else {
                        stageIsClear = true;
                    }
                    if (isGameOver) {
                        isPlaying = false;
                        if (!isMute)
                            soundPool.play(game_Over_Sound, 1, 1, 1, 0, 1);
                        canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                        canvas.drawBitmap(gameOver.gameover, gameOver.x, gameOver.y, paint);
                        getHolder().unlockCanvasAndPost(canvas);
                        saveIfHighScore();
                        waitBeforeExiting();
                        return;
                    }

                    if (dragon_fire_on_screen) {
                        if (fire_out_of_mouth) {
                            dragonFire.y = (int) (dragon.y + dragon.height / 2 - 45 * screenRatioY);
                            if (!isMute) {
                                soundPool.play(dragon_fire_on_screen_Sound, 1, 1, 1, 0, 1);
                            }
                        }
                        canvas.drawBitmap(dragonFire.getDragon_Fire(), dragonFire.x, dragonFire.y, paint);
                    }
                    for (Bullet bullet : bullets)
                        canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
                }
            }

            else {//dragon got dead and stage is cleared
                if(!isMute && battle_Music_Playing){
                    stop_Player();
                    battle_Music_Playing=false;
                }
                if(!isMute && !is_Stage_Clear_Sound_Played){
                    soundPool.play(stage_clear_sound,1,1,1,1,1);
                    is_Stage_Clear_Sound_Played=true;
                }
                canvas.drawText(score + "", screenX - 300, 120, paint);
                canvas.drawRect(20, screenY - 35, dragon.width, screenY - 5, paint_Health_Border);
                canvas.drawBitmap(gameOver.stageClear, gameOver.x_stage, gameOver.y_stage, paint);
                canvas.drawBitmap(dragon_dead.getDragonDead(),dragon.x,dragon.y,paint);
            }
            //drawing flight
            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);
            getHolder().unlockCanvasAndPost(canvas);

            if (flight.x > screenX + flight.width) {// when flight gets off of the screen, end the game
                saveIfHighScore();
                //isPlaying = false;
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
            }
        }

    }











    private void waitBeforeExiting() {

        try {
            Thread.sleep(4000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {
        String username = prefs.getString("Temp_User_Name", "");

        if (prefs.getInt("score1", 0) <= score) {

            String name1 = prefs.getString("d_name1", "-");
            int score1 = prefs.getInt("score1", 0);
            String name2 = prefs.getString("d_name2", "-");
            int score2 = prefs.getInt("score2", 0);

            editor.putString("d_name1", username);
            editor.putInt("score1", score);
            editor.apply();

            if (prefs.getInt("score2", 0) <= score1) {
                editor.putString("d_name2", name1);
                editor.putInt("score2", score1);
                editor.apply();
            }
            if (prefs.getInt("score3", 0) <= score2) {
                editor.putString("d_name3", name2);
                editor.putInt("score3", score2);
                editor.apply();
            }
        } else if (prefs.getInt("score2", 0) <= score) {
            String name2 = prefs.getString("d_name2", "-");
            int score2 = prefs.getInt("score2", 0);
            editor.putString("d_name2", username);
            editor.putInt("score2", score);
            editor.apply();

            if (prefs.getInt("score3", 0) <= score2) {
                editor.putString("d_name3", name2);
                editor.putInt("score3", score2);
                editor.apply();
            }
        } else if (prefs.getInt("score3", 0) <= score) {
            editor.putString("d_name3", username);
            editor.putInt("score3", score);
            editor.apply();
        }

        editor.putString("Last_User_Name", username);
        editor.putInt("Last_User_Score", score);
        editor.apply();

    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause() {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragon.health!=0 &&!dragon_incoming && !stageIsClear) {
            int count = event.getPointerCount();
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
        } else if(dragon.health!=0 && dragon_incoming && !stageIsClear) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getX() < screenX / 2) {
                        flight.isGoingUp = true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    flight.isGoingUp = false;
                    break;

            }
        }
        return true;
    }
    private void start_Player(){
        if(mediaPlayer==null){
            mediaPlayer=MediaPlayer.create(activity,R.raw.battle);
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

    public void newBullet() {

        if (!isMute)
            soundPool.play(bullet_Sound, 1, 1, 1, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}