package com.example.surfacedrawexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite {
    MySurfaceView mySurfaceView;
    Bitmap image;
    float x, y; //координаты кадра на экране
    float dx, dy; // смещения координат при движении
    final int IMAGE_ROWS = 4; //количество строк с кадрами
    final int IMAGE_COLUMN = 3; //количество столбцов с кадрами
    int direction = 1; //направление движения
    int currentFrame = 0; //номер текущего кадра
    float widthFrame, heightFrame;
    Paint paint;
    float touchX, touchY; //координаты точки касания
    float widthScreen, heightScreen;
    //конструктор
    public Sprite(Bitmap image, MySurfaceView mySurfaceView, float x, float y){
        this.image = image;
        this.mySurfaceView = mySurfaceView;
        this.x = x;
        this.y = y;
        paint = new Paint();
        widthFrame = this.image.getWidth()/(float)IMAGE_COLUMN;
        heightFrame = this.image.getHeight()/(float)IMAGE_ROWS;
    }
    //сеттеры для координат касания
    public void setTouchX(float touchX){
        this.touchX = touchX;
        calculate();
    }
    public void setTouchY(float touchY){
        this.touchY = touchY;
        calculate();
    }
    //перерасчёт смещений
    private void calculate(){
        double hypot = Math.sqrt((touchX - x)*(touchX - x) + (touchY - y)*(touchY - y));
        int speed = 20;
        dx = speed * (touchX - x)/(float) hypot;
        dy = speed * (touchY - y)/(float) hypot;
    }
    //рисование себя
    public void draw(Canvas canvas){
        heightScreen = canvas.getHeight();
        widthScreen = canvas.getWidth();
        //как определить номер кадра
        Rect src = new Rect((int)(currentFrame*widthFrame), (int)(direction*heightFrame),
                (int)((currentFrame+1)*widthFrame), (int)((direction+1)*heightFrame));
        Rect dst = new Rect((int)x, (int)y, (int)(x + widthFrame), (int)(y + heightFrame));
        canvas.drawBitmap(image, src, dst, paint);
        currentFrame = ++currentFrame % IMAGE_COLUMN;
        x += dx;
        y += dy;
        controlRoute();
    }
    //смена направления движения
    private void controlRoute(){
        if(y < 10 || y + heightFrame > heightScreen - 10 )
            dy = -dy;
        if(x < 10 || x + widthFrame > widthScreen - 10)
            dx = -dx;
    }

}
