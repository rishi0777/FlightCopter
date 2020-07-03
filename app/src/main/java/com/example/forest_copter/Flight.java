package com.example.forest_copter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import static com.example.forest_copter.GameView1.screenRatioX;


public class Flight {

    public int x,y;
    public boolean isGoingUp =false;
    public int toShoot=0;
    public int width;
    public int height;
    private int shootCounter=1;
    private GameView1 gameView1;
    private int wingCounter =0;
    private Bitmap shoot1,shoot2,shoot3,shoot4,shoot5;
    private Bitmap flight1,flight2,dead;

    Flight(GameView1 gameView1,int screenY ,Resources res){
        this.gameView1=gameView1;
        flight1=BitmapFactory.decodeResource(res, R.drawable.fly1);//initializing bitmap files
        flight2=BitmapFactory.decodeResource(res,R.drawable.fly2);
        dead=BitmapFactory.decodeResource(res,R.drawable.dead);
        shoot1=BitmapFactory.decodeResource(res,R.drawable.shoot1);
        shoot2=BitmapFactory.decodeResource(res,R.drawable.shoot2);
        shoot3=BitmapFactory.decodeResource(res,R.drawable.shoot3);
        shoot4=BitmapFactory.decodeResource(res,R.drawable.shoot4);
        shoot5=BitmapFactory.decodeResource(res,R.drawable.shoot5);
       // this.gameView1=gameView;

        width=flight1.getWidth();// taking height and width of flights(as flight1 and flight 2 have same size)
        height=flight1.getHeight();//height and width of shoot will also be same since they are also the images of flight

        width/=5;//Reducing the size since size of flights initially are two big
        height/=5;

        //width=(int)(width*screenRatioX); //Making its size compatible to every screen
        //height=(int)(height*screenRatioY);

        flight1=Bitmap.createScaledBitmap(flight1,width,height,false);// Now here we resized the image
        flight2=Bitmap.createScaledBitmap(flight2,width,height,false);
        dead=Bitmap.createScaledBitmap(dead,width,height,false);
        shoot1=Bitmap.createScaledBitmap(shoot1,width,height,false);
        shoot2=Bitmap.createScaledBitmap(shoot2,width,height,false);
        shoot3=Bitmap.createScaledBitmap(shoot3,width,height,false);
        shoot4=Bitmap.createScaledBitmap(shoot4,width,height,false);
        shoot5=Bitmap.createScaledBitmap(shoot5,width,height,false);



        y=(screenY/2)-(height/2);//setting position of flight in middle of y axis
        //x=(int)(64*screenRatioX);//creating some space between extreme right corner and flight.
        x= (int) (30*screenRatioX);

    }

    public Bitmap getFlight() {
        if(toShoot!=0){
            if(shootCounter==1) {
                shootCounter++;
                return shoot1;
            }
            if(shootCounter==2) {
                shootCounter++;
                return shoot2;
            }
            if(shootCounter==3) {
                shootCounter++;
                return shoot3;
            }
            if(shootCounter==4) {
                shootCounter++;
                return shoot4;
            }
            shootCounter=1;
            toShoot--;
            gameView1.newBullet();
            return shoot5;//if we paste above three lines below then it becomes unreachable

        }

        if(wingCounter==0) {
            wingCounter++;
            return flight1;
        }
        else {
            wingCounter--;
            return flight2;
        }
    }
    Rect getCollisionShape(){

        return new Rect(x,y,x+width,y+height);
    }
    Bitmap getDead(){
        return dead;
    }
}

