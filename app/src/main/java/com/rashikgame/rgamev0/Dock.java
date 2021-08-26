package com.rashikgame.rgamev0;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Dock {

    Bitmap dockBitmap;
    int dockWidth,dockHeight;
    int leftMostPoint,rightMostPoint;


    Point topLeftPoint=new Point(0,0),bottomCenterPoint;
    DrawingThread drawingThread;
    boolean movingLeftFlag=false;
    boolean movingRightFlag=false;

    public Dock(DrawingThread drawingThread,int bitmapID)
    {
        this.drawingThread=drawingThread;
        Bitmap tempBitmap= BitmapFactory.decodeResource(drawingThread.context.getResources(),bitmapID);
        tempBitmap=Bitmap.createScaledBitmap(tempBitmap,drawingThread.displayX/2,
                drawingThread.displayX*tempBitmap.getHeight()/4/tempBitmap.getWidth(),true);

        dockBitmap=tempBitmap;
        dockHeight= dockBitmap.getHeight();
        dockWidth= dockBitmap.getWidth();


        bottomCenterPoint=new Point(drawingThread.displayX/2,drawingThread.displayY);
        topLeftPoint.y=bottomCenterPoint.y-dockHeight;
        updateInfo();
    }

    private void updateInfo() {

        leftMostPoint= bottomCenterPoint.x-dockWidth/2;
    rightMostPoint=bottomCenterPoint.x+dockWidth/2;

        topLeftPoint.x=leftMostPoint;
    }
    public void moveDockLeft()
    {
        bottomCenterPoint.x-=4;
        updateInfo();

    }
    public void moveDockRight()
    {
        bottomCenterPoint.x+=4;
        updateInfo();

    }

    public void startMovingLeft()
    {
        Thread thread=new Thread()
        {
            @Override
            public void run() {
                //super.run();
                movingLeftFlag=true;
                while (movingLeftFlag)
                {
                    moveDockLeft();
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    public void stopMovingLeft()
    {
        movingLeftFlag=false;
    }

    public void startMovingRight()
    {
        Thread thread=new Thread()
        {
            @Override
            public void run() {
                //super.run();
                movingRightFlag=true;
                while (movingRightFlag)
                {
                    moveDockRight();
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    public void stopMovingRight()
    {
        movingRightFlag=false;
    }





}
