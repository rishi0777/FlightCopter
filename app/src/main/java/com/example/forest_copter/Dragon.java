package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.forest_copter.GameView1.fire_out_of_mouth;
import static com.example.forest_copter.GameView1.screenRatioX;
import static com.example.forest_copter.GameView1.screenRatioY;
import static com.example.forest_copter.GameView1.dragon_fire_on_screen;

public class Dragon {
        int[] resID;
        int x,y,i;
        private int j=-1;
        final int max_Health_point=100;
        int health=100;
        int width,height;
        Bitmap[] dragon;

        Dragon(Resources res,Stage1Activity activity){

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

        width=dragon[1].getWidth();
        height=dragon[1].getHeight();
        width=(int)(900*screenRatioX);
        height=(int)(700*screenRatioY);

        for(i=0;i<60;i++){
            dragon[i]= Bitmap.createScaledBitmap(dragon[i],width,height,false);
        }

    }

     Bitmap getDragon() {

            if(j==59){
                j=-1;
            }

            else {
                while (j != 59) {
                    if(j==21){
                        dragon_fire_on_screen = true;
                        fire_out_of_mouth=true;
                    }
                    j++;
                    return dragon[j];
                }
            }

       return dragon[59];
     }

    Rect getCollisionShape(){
        return new Rect(x,y+height/2-(int)(50*screenRatioY),x+width,y+height/2+(int)(200*screenRatioY));
    }
}
