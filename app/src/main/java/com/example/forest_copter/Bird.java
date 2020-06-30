package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;



public class Bird {

    int []resID;
    int how_many_bird_images;
    Stage1Activity activity;
    public int speed = 20;
    public boolean wasShot = true;
    boolean bird_got_hit_with_flight=false;
    int x=0 , y, width, height, birdCounter = 1;
    Bitmap bird1, bird2, bird3, bird4,bird_got_hit1;

    Bird (Stage1Activity activity,Resources res, String []bird) {
        this.activity=activity;

        how_many_bird_images=bird.length;
        resID=new int[how_many_bird_images];

        for(int i=0;i<how_many_bird_images;i++) {
            resID[i] = res.getIdentifier(bird[i], "drawable", activity.getPackageName());
        }

        if(how_many_bird_images==4){
            bird1 = BitmapFactory.decodeResource(res, resID[0]);
            bird2 = BitmapFactory.decodeResource(res, resID[1]);
            bird3 = BitmapFactory.decodeResource(res, resID[2]);
            bird4 = BitmapFactory.decodeResource(res, resID[3]);
            bird_got_hit1 = BitmapFactory.decodeResource(res, R.drawable.st1_bird2_got_dead1);
           // bird_got_hit2 = BitmapFactory.decodeResource(res, R.drawable.st1_bird2_got_dead2);
        }
        else{
            bird1 = BitmapFactory.decodeResource(res, resID[0]);
            bird2 = BitmapFactory.decodeResource(res, resID[1]);
            bird_got_hit1 = BitmapFactory.decodeResource(res, R.drawable.st1_bird1_got_dead1);
            //bird_got_hit2 = BitmapFactory.decodeResource(res, R.drawable.st1_bird1_got_dead2);
        }

        if(how_many_bird_images==2) {
            width = bird1.getWidth();
            height = bird1.getHeight();

            width /= 7;
            height /= 7;
        }
        else{
            width = bird1.getWidth();
            height = bird1.getHeight();

            width /= 11;
            height /= 11;
        }
       // width = (int) (width * screenRatioX);
        // height = (int) (height * screenRatioY);

        if(how_many_bird_images==4) {
            bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
            bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
            bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
            bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);
            bird_got_hit1 = Bitmap.createScaledBitmap(bird_got_hit1, width, height, false);
          //  bird_got_hit2 = Bitmap.createScaledBitmap(bird_got_hit2, width, height, false);
        }
        else {
            bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
            bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
            bird_got_hit1 = Bitmap.createScaledBitmap(bird_got_hit1, width, height, false);
            //bird_got_hit2 = Bitmap.createScaledBitmap(bird_got_hit2, width, height, false);
        }
        y = -height;
    }

    Bitmap getBird () {

        if (how_many_bird_images == 4) {
            if (birdCounter == 1) {
                birdCounter++;
                return bird1;
            }

            if (birdCounter == 2) {
                birdCounter++;
                return bird2;
            }

            if (birdCounter == 3) {
                birdCounter++;
                return bird3;
            }
            birdCounter = 1;
            return bird2;
        }
        else if(how_many_bird_images==1){
            return bird1;
        }
        else {
            if (birdCounter == 1) {
                birdCounter++;
                return bird1;
            }

            birdCounter = 1;
            return bird2;
        }
    }
    Bitmap getdead(){
        return bird_got_hit1;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
