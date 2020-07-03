package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.forest_copter.GameView1.screenRatioX;

public class DragonFire {

    int x,y;
    Bitmap dragon_Fire1,dragon_Fire2;
    int dragonFireCounter=1;
    int width,height;

   DragonFire(Resources res, int x ){
       dragon_Fire1= BitmapFactory.decodeResource(res,R.drawable.fireball1);
       dragon_Fire2= BitmapFactory.decodeResource(res,R.drawable.fireball2);
       width=dragon_Fire1.getWidth();
       height=dragon_Fire1.getWidth();
       width=(int)(width/15*screenRatioX);
       height=(int)(height/15*screenRatioX);;

       this.x=x-width/2;
       dragon_Fire1=Bitmap.createScaledBitmap(dragon_Fire1,width,height,false);
       dragon_Fire2=Bitmap.createScaledBitmap(dragon_Fire2,width,height,false);
   }

    Bitmap getDragon_Fire(){
       if(dragonFireCounter==1) {
           dragonFireCounter++;
           return dragon_Fire1;
       }
       dragonFireCounter=1;
       return dragon_Fire2;
    }
    Rect getCollisionShape(){

        return new Rect(x,y,x+width,y+height);
    }
}
