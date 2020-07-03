package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.forest_copter.GameView1.dragon_fire_on_screen;
import static com.example.forest_copter.GameView1.fire_out_of_mouth;
import static com.example.forest_copter.GameView1.screenRatioX;
import static com.example.forest_copter.GameView1.screenRatioY;

public class Dragon_Incoming {

    int x,y;
    int x_dr_incoming_danger_image, y_dr_incoming_danger_image;
    int width, height,i,width_dragon,height_dragon;
    int j=-1;
    Bitmap dragon_incoming_danger_image;
    Bitmap[] dragon;
    private int resID[];

    Dragon_Incoming(Stage1Activity  activity,Resources res, int screenX, int screenY) {

        dragon=new Bitmap[60];
        resID=new int[60];

        String[] str={"dragon1","dragon2","dragon3","dragon4","dragon5","dragon6","dragon7","dragon8","dragon9"
                ,"dragon10","dragon11","dragon12","dragon13","dragon14","dragon15","dragon16","dragon17","dragon18"
                ,"dragon19","dragon20","dragon21","dragon22","dragon23","dragon24","dragon25","dragon26","dragon27",
                "dragon28","dragon29","dragon30","dragon31","dragon32","dragon33","dragon34","dragon35","dragon36",
                "dragon37","dragon38","dragon39","dragon40","dragon41","dragon42","dragon43","dragon44","dragon45",
                "dragon46","dragon47","dragon48","dragon49","dragon50","dragon51","dragon52","dragon53","dragon54","dragon54"
                ,"dragon55","dragon56","dragon57","dragon58","dragon59","dragon60"};



        for(i=0;i<60;i++){
            resID[i]=res.getIdentifier(str[i],"drawable",activity.getPackageName());
        }
        for(i=0;i<60;i++){
            dragon[i]= BitmapFactory.decodeResource(res,resID[i]);
        }

        width_dragon=dragon[1].getWidth();
        height_dragon=dragon[1].getHeight();
        width_dragon=(int)(900*screenRatioX);
        height_dragon=(int)(700*screenRatioY);

        for(i=0;i<60;i++){
            dragon[i]= Bitmap.createScaledBitmap(dragon[i],width_dragon,height_dragon,false);
        }

        x=2*screenX+width_dragon;//setting initial position of dragon
        y=(screenY-height_dragon)/2;


        //setting danger image
        dragon_incoming_danger_image = BitmapFactory.decodeResource(res, R.drawable.dragon_incoming);
        width = dragon_incoming_danger_image.getWidth();
        height = dragon_incoming_danger_image.getHeight();
        width /= 4;
        height /= 4;

        dragon_incoming_danger_image = Bitmap.createScaledBitmap(dragon_incoming_danger_image, width, height, false);
        x_dr_incoming_danger_image = screenX / 2 - width / 2;
        y_dr_incoming_danger_image = screenY / 2 - height / 2;

    }
    Bitmap getIncomingDragon() {
        if (j == 59) {
            j = -1;
        } else {
            while (j != 59) {
                j++;
                return dragon[j];
            }
        }

        return dragon[59];
    }
}


