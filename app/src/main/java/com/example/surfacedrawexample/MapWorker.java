package com.example.surfacedrawexample;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MapWorker {
    Bitmap [] textures; //массив текстур
    int [][] mapArray; // массив с картой уровня, 0 - трава, 1 - лава, 2 - вода, 3 - скала
    float wCanvas, hCanvas;
    Paint paint;
    final int TEXTURE_SIZE = 32;
    public MapWorker(float wCanvas, float hCanvas, Resources resources){
        this.hCanvas = hCanvas;
        this.wCanvas = wCanvas;
        paint = new Paint();
        //грузим текстуры
        textures = new Bitmap[4];
        textures[0] = BitmapFactory.decodeResource(resources, R.drawable.grass);
        textures[1] = BitmapFactory.decodeResource(resources, R.drawable.lava);
        textures[2] = BitmapFactory.decodeResource(resources, R.drawable.water);
        textures[3] = BitmapFactory.decodeResource(resources, R.drawable.rock);
        //генерация массива
        mapArray = new int[(int)hCanvas/TEXTURE_SIZE][(int)wCanvas/TEXTURE_SIZE];
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[i].length; j++) {
                if(j > mapArray[i].length/2 - 3 &&  j < mapArray[i].length/2 + 3)
                    mapArray[i][j] = 0;
                else{
                    mapArray[i][j] = 1;
                }
            }
        }
        // важно: генерация карты должна быть однократной!!
    }

    public void changeMap(){
        //сдвиг карты
        for (int i = mapArray.length - 1; i >= 1 ; i--) {
            mapArray[i] = mapArray[i - 1].clone();
        }
        //генерация нулевой строки
        for (int j = 0; j < mapArray[0].length; j++) {
            if(j > mapArray[0].length/2 - 3 &&  j < mapArray[0].length/2 + 3)
                mapArray[0][j] = 0;
            else{
                mapArray[0][j] = (int)(Math.random()*3);
            }
        }
    }

    public void draw(Canvas canvas){
        int x = 0, y = 0;
        for (int i = 0; i < mapArray.length; i++) {
            for (int j = 0; j < mapArray[i].length; j++) {
                canvas.drawBitmap(textures[mapArray[i][j]], x, y, paint);
                x += TEXTURE_SIZE;
            }
            x = 0;
            y += TEXTURE_SIZE;
        }
    }
}
