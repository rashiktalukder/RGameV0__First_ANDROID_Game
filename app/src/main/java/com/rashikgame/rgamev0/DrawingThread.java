package com.rashikgame.rgamev0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class DrawingThread extends Thread {

    private Canvas canvas;
    private GameView gameView;
    private Context context;

    boolean ThreadFlag=false;
    Bitmap backgroundBitmap;

    int displayX,displayY;

    public DrawingThread( GameView gameView, Context context) {
        //this.canvas = canvas;
        this.gameView = gameView;
        this.context = context;

        initializeAll();
    }

    private void initializeAll() {
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay=windowManager.getDefaultDisplay();

        Point displayDimension=new Point();
        defaultDisplay.getSize(displayDimension);
        displayX=displayDimension.x;
        displayY=displayDimension.y;

        backgroundBitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
        backgroundBitmap=Bitmap.createScaledBitmap(backgroundBitmap,displayX,displayY,true);

    }


    @Override
    public void run() {
        ThreadFlag=true;

        while(ThreadFlag)
        {
            canvas=gameView.surfaceHolder.lockCanvas();
            try {
                synchronized (gameView.surfaceHolder)
                {
                    updateDisplay();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(canvas!=null)
                {
                    gameView.surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }

            try {
                Thread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateDisplay() {

        canvas.drawBitmap(backgroundBitmap,0,0,null);
    }

    public void stopThread()
    {
        ThreadFlag=false;
    }
}
