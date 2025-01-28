package com.example.surfacedrawexample;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread{
    SurfaceHolder holder;
    MySurfaceView mySurfaceView;
    boolean isRun;
    long prevTime;

    public DrawThread(SurfaceHolder holder, MySurfaceView mySurfaceView){
        this.holder = holder;
        this.mySurfaceView = mySurfaceView;
        isRun = false;
        prevTime = System.currentTimeMillis();
    }
    @Override
    public void run() {
        while (isRun){
            Canvas canvas = null;
            long nowTime = System.currentTimeMillis();
            long elapsedTime = nowTime - prevTime;
            if(elapsedTime > 30){
                prevTime = nowTime;
                canvas = holder.lockCanvas();
                synchronized (holder){
                    mySurfaceView.draw(canvas);
                }
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setRunning(boolean b) {
        isRun = b;
    }
}
