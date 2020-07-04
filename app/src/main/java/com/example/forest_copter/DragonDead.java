package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.forest_copter.GameView1.dragon_fire_on_screen;
import static com.example.forest_copter.GameView1.fire_out_of_mouth;
import static com.example.forest_copter.GameView1.screenRatioX;
import static com.example.forest_copter.GameView1.screenRatioY;

public class DragonDead {

    Bitmap[] dragon;
    int[] resID;
    int i,j=-1;
    int  width,height;

    DragonDead(Resources res,Stage1Activity activity) {
        dragon = new Bitmap[72];
        resID = new int[72];

        String[] str = {"dragon_dead1", "dragon_dead2", "dragon_dead3", "dragon_dead4", "dragon_dead5", "dragon_dead6", "dragon_dead7",
                "dragon_dead8", "dragon_dead9", "dragon_dead10", "dragon_dead11", "dragon_dead12", "dragon_dead13", "dragon_dead14",
                "dragon_dead15", "dragon_dead16", "dragon_dead17", "dragon_dead18", "dragon_dead19", "dragon_dead20", "dragon_dead21",
                "dragon_dead22", "dragon_dead23", "dragon_dead24", "dragon_dead25", "dragon_dead26", "dragon_dead27", "dragon_dead28",
                "dragon_dead29", "dragon_dead30", "dragon_dead31", "dragon_dead32", "dragon_dead33", "dragon_dead34", "dragon_dead35",
                "dragon_dead36", "dragon_dead37", "dragon_dead38", "dragon_dead39", "dragon_dead40", "dragon_dead41", "dragon_dead42",
                "dragon_dead43", "dragon_dead44", "dragon_dead45", "dragon_dead46", "dragon_dead47", "dragon_dead48", "dragon_dead49",
                "dragon_dead50", "dragon_dead51", "dragon_dead52", "dragon_dead53", "dragon_dead54", "dragon_dead54", "dragon_dead55",
                "dragon_dead56", "dragon_dead57", "dragon_dead58", "dragon_dead59", "dragon_dead60","dragon_dead61","dragon_dead62"
                ,"dragon_dead63","dragon_dead64","dragon_dead65","dragon_dead66","dragon_dead67","dragon_dead68","dragon_dead69",
                "dragon_dead70","dragon_dead71"};


        for (i = 0; i < 72; i++) {
            resID[i] = res.getIdentifier(str[i], "drawable", activity.getPackageName());
        }

        for (i = 0; i < 72; i++) {
            dragon[i] = BitmapFactory.decodeResource(res, resID[i]);
        }

        width = dragon[1].getWidth();
        height = dragon[1].getHeight();
        width = (int) (900 * screenRatioX);
        height = (int) (700 * screenRatioY);

        for (i = 0; i < 72; i++) {
            dragon[i] = Bitmap.createScaledBitmap(dragon[i], width, height, false);
        }

    }

    Bitmap getDragonDead() {

        if(j!=70) {
            j++;
            return dragon[j];
        }
            //  j=-1;
        return dragon[71];
    }
}
