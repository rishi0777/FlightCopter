package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.forest_copter.GameView1.screenRatioX;
import static com.example.forest_copter.GameView1.screenRatioY;


public class GameOver {
    int x,y,x_stage,y_stage;
    int width,height,width_stage,height_stage;
    Bitmap gameover,stageClear;

    GameOver(Resources res, int screenX, int screenY){
        gameover=BitmapFactory.decodeResource(res,R.drawable.gameover);
        stageClear=BitmapFactory.decodeResource(res,R.drawable.stage_clear);

        width=gameover.getWidth();
        height=gameover.getHeight();
        width_stage=stageClear.getWidth();
        height_stage=stageClear.getHeight();

        width_stage/=5*screenRatioX;
        height_stage/=5*screenRatioY;
        width/=4*screenRatioX;
        height/=3*screenRatioY;
        //width=(int)(width*screenRatioX);
        //height=(int)(height*screenRatioX);
        gameover=Bitmap.createScaledBitmap(gameover,width,height,false);
        stageClear=Bitmap.createScaledBitmap(stageClear,width_stage,height_stage,false);

        x_stage=screenX/2-width_stage/2; //placing stagecClear in the center of the screen
        y_stage=screenY/2-height_stage/2-(int)(50*screenRatioY);
        x=screenX/2-width/2;//placing the gameover image in the middle
        y=screenY/2-height/2;
    }

    Bitmap getGameover() {

        return gameover;
    }
    Bitmap getStageClear(){
        return  stageClear;
    }
}

