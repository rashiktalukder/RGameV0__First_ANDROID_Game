package com.rashikgame.rgamev0;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

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
}
