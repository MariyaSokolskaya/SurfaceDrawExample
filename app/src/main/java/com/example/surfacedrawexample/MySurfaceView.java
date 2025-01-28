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

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap image;
    float x, y, touchX, touchY; //координаты рисунка и точки касания
    float dx, dy;// изменения координат при движении
    Paint paint;
    float speed;
    Resources resources;
    SurfaceHolder holder;
    DrawThread drawThread;//поток рисования

    public MySurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);//"активируем" интерфейс SurfaceHolder.Callback
        paint = new Paint();
        x = 400;
        y = 300;
        resources = getResources();
        image = BitmapFactory.decodeResource(resources, R.drawable.cathead);
        speed = 20;//коэффициент скорости
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(image, x, y, paint);
        x += dx;
        y += dy;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            touchX = event.getX();
            touchY = event.getY();
            calculate();
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
