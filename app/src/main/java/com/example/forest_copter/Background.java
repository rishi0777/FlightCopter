package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x=0;
    int y=0;
    Bitmap stage1_background;
    Bitmap stage2_background;
    public Background(int screenX,int screenY,Resources res){
        stage1_background= BitmapFactory.decodeResource(res,R.drawable.background_st1);
        stage2_background=BitmapFactory.decodeResource(res,R.drawable.background_st2);

        stage1_background=Bitmap.createScaledBitmap(stage1_background,screenX,screenY,false);
        stage2_background=Bitmap.createScaledBitmap(stage2_background,screenX,screenY,false);
    }
}
