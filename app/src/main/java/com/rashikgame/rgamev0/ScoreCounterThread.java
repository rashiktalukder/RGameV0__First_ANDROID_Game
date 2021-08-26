package com.rashikgame.rgamev0;

public class ScoreCounterThread extends Thread{

    float maxScore;
    DrawingThread drawingThread;
    boolean threadRunningFlag=false;

    public ScoreCounterThread (DrawingThread drawingThread)
    {
        this.drawingThread=drawingThread;
    }

    @Override
    public void run() {
        //super.run();
        threadRunningFlag=true;

        while(threadRunningFlag)
        {
            float tempMax=0;
            for(Robot robot: drawingThread.allRobots)
            {
                if(robot.centerY<tempMax)
                {
                    tempMax= robot.centerY;
                }
            }
            drawingThread.maxScore = (drawingThread.maxScore > (-tempMax)) ? drawingThread.maxScore : (int) (-tempMax);


            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopThread()
    {
        threadRunningFlag=false;
    }
}
