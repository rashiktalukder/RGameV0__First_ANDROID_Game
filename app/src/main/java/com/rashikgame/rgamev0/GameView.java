package com.rashikgame.rgamev0;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    Context context;
    SurfaceHolder surfaceHolder;
    DrawingThread drawingThread;

    public GameView(Context context) {
        super(context);
        this.context=context;
        surfaceHolder=getHolder();
        surfaceHolder.addCallback(this);

        drawingThread=new DrawingThread(this,context);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        try {
            drawingThread.start();
        } catch (Exception e) {
            restartThread();
        }
    }

    private void restartThread() {
        drawingThread.stopThread();
        drawingThread=null;
        drawingThread=new DrawingThread(this,context);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        drawingThread.stopThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touchPoint=new Point((int) event.getX(),(int) event.getY());
        Random random=new Random();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawingThread.allRobots.add(new Robot(drawingThread.allPossibleRobots.get(random.nextInt(5)), touchPoint));

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                drawingThread.allRobots.get(drawingThread.allRobots.size()-1).setCenter(touchPoint);

                break;

            default:
                break;
        }

        return super.onTouchEvent(event);
    }
}
