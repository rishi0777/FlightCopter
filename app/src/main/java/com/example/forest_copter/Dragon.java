package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Dragon {
    int x,y;
    int health=10;
    int width,height;
    int dragon_counter=1;
    Bitmap dragon1,dragon2,dragon3,dragon_fire1,dragon_fire2,dragon_fire3,dragon_fire4,dragon_fire5;
    Dragon(Resources res){
        dragon1= BitmapFactory.decodeResource(res,R.drawable.d1);
        dragon2= BitmapFactory.decodeResource(res,R.drawable.d2);
        dragon3= BitmapFactory.decodeResource(res,R.drawable.d3);

        dragon_fire1= BitmapFactory.decodeResource(res,R.drawable.dragon_fire1);
        dragon_fire1= BitmapFactory.decodeResource(res,R.drawable.dragon_fire2);
        dragon_fire1= BitmapFactory.decodeResource(res,R.drawable.dragon_fire3);
        dragon_fire1= BitmapFactory.decodeResource(res,R.drawable.dragon_fire4);
        dragon_fire1= BitmapFactory.decodeResource(res,R.drawable.dragon_fire5);

        width=dragon1.getWidth();
        height=dragon1.getWidth();

    }
     Bitmap getDragon(){
        if(dragon_counter==1){
            dragon_counter++;
            return dragon1;
        }
        else if(dragon_counter==2){
            dragon_counter++;
            return dragon2;
        }

        dragon_counter=1;
        return dragon3;
    }
    Rect getCollisionShape(){
        return new Rect(x,y,x+width,y+height);
    }
}
