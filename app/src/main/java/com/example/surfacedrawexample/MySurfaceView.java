package com.example.surfacedrawexample;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap image;
    float x, y, touchX, touchY; //координаты рисунка и точки касания
    float dx, dy;// изменения координат при движении
    Paint paint;
    float speed;
    Resources resources;
    SurfaceHolder holder;
    DrawThread drawThread;//поток рисования

    ArrayList<Sprite> sprites = new ArrayList<>();
    Sprite character;

    boolean isMapGenerate = false;
    MapWorker mapWorker;

    public MySurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);//"активируем" интерфейс SurfaceHolder.Callback
        paint = new Paint();
        x = 400;
        y = 1100;
        resources = getResources();
        image = BitmapFactory.decodeResource(resources, R.drawable.sprites);
        speed = 20;//коэффициент скорости
        character = new Sprite(image, this, x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawBitmap(image, x, y, paint);
        //x += dx;
        //y += dy;
       /* for (Sprite s : sprites) {
            s.draw(canvas);
        }*/
        if(!isMapGenerate){
            mapWorker = new MapWorker(canvas.getWidth(), canvas.getHeight(), resources);
            isMapGenerate = true;
        }
        mapWorker.draw(canvas);
        character.draw(canvas);

        mapWorker.changeMap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchX = event.getX();
            touchY = event.getY();
            //calculate();
            /*for (Sprite s: sprites) {
                s.setTouchX(touchX);
                s.setTouchY(touchY);
            }
            Sprite sprite = new Sprite(image, this, touchX, touchY);
            sprites.add(sprite);*/
            character.setTouchX(touchX);
            character.setTouchY(touchY);
        }
        return true;
    }

    private void calculate() {
        double hypot = Math.sqrt((touchX - x)*(touchX - x) + (touchY - y)*(touchY - y));
        dx = speed * (touchX - x)/(float) hypot;
        dy = speed * (touchY - y)/(float) hypot;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(holder, this);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
