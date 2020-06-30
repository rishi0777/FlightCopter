package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class GameOver {
    int x,y;
    int width,height;
    Bitmap gameover;

    GameOver(Resources res, int screenX, int screenY){
        gameover=BitmapFactory.decodeResource(res,R.drawable.gameover);

        width=gameover.getWidth();
        height=gameover.getHeight();
        width/=3;
        height/=3;
        //width=(int)(width*screenRatioX);
        //height=(int)(height*screenRatioX);
        gameover=Bitmap.createScaledBitmap(gameover,width,height,false);

        x=screenX/2-width/2;//placing the image in the middle
        y=screenY/2-height/2;
    }

    Bitmap getGameover() {

        return gameover;
    }
}

