package com.rashikgame.rgamev0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

public class DrawingThread extends Thread {

    private Canvas canvas;
    private GameView gameView;
    private Context context;

    boolean ThreadFlag=false;
    Bitmap backgroundBitmap;

    int displayX,displayY;

    ArrayList<Robot> allRobots;
    ArrayList<Bitmap> allPossibleRobots;

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

        initializeAllPossibleRobots();

    }

    private void initializeAllPossibleRobots() {
        allRobots=new ArrayList<Robot>();
        allPossibleRobots=new ArrayList<Bitmap>();

        allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot0));
        allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot9));
        allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot10));
        allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot3));
        allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot4));
        //allPossibleRobots.add(giveResizedRobotBitmap(R.drawable.robot9));
    }

    private Bitmap giveResizedRobotBitmap(int resourceID)
    {
        Bitmap tempBitmap=BitmapFactory.decodeResource(context.getResources(),resourceID);
        tempBitmap=Bitmap.createScaledBitmap(tempBitmap,displayX/5,
                (tempBitmap.getHeight()/tempBitmap.getWidth())*(displayX/5),true);
        return tempBitmap;
    }


    @Override
    public void run() {
        ThreadFlag=true;
        AnimationThread animationThread=new AnimationThread(this);
        animationThread.start();

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
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        animationThread.stopThread();
    }

    private void updateDisplay() {

        canvas.drawBitmap(backgroundBitmap,0,0,null);
        for (int i=0;i<allRobots.size();i++)
        {
            Robot tempRobot=allRobots.get(i);
            canvas.drawBitmap(tempRobot.robotBitmap,tempRobot.centerX-(tempRobot.width/2),
                    tempRobot.centerY-(tempRobot.height/2),tempRobot.robotPaint);
        }
        //drawSensorData();
    }

    public void stopThread()
    {
        ThreadFlag=false;
    }

   /* private void drawSensorData()
    {
        Paint paint=new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(displayX/10);

        canvas.drawText("X axis: "+GameActivity.getgX(),0,displayY/3,paint);
        canvas.drawText("Y axis: "+GameActivity.getgY(),0,displayY/3+displayX/5,paint);
    }*/
}
