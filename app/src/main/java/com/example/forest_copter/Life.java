package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Life {
    int x=5,y=5;
    int height ,width;
    Bitmap heart;
    public int chance=3;
    Life(Resources res){
        heart= BitmapFactory.decodeResource(res,R.drawable.heart);
        height=heart.getHeight();
        width=heart.getWidth();
        height/=4;
        width/=4;
        heart=Bitmap.createScaledBitmap(heart,width,height,false);
    }
    Bitmap lifelines(){

        return heart;
    }
}
