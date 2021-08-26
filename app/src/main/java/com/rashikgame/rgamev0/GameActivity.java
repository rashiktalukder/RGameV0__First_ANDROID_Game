package com.rashikgame.rgamev0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends Activity {

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    GameView gameView;
    Sensor accelerometerSensor;

    public static float getgX() {
        return gX;
    }

    public static void setgX(float gX) {
        GameActivity.gX = gX;
    }

    public static float getgY() {
        return gY;
    }

    public static void setgY(float gY) {
        GameActivity.gY = gY;
    }

    private static float gX,gY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       // gameView=new GameView(this);
        initializeSensors();


        setContentView(R.layout.activity_game);
        gameView=(GameView) findViewById(R.id.myGameView);
        initializeButton();
    }

    private void initializeButton() {

        Button moveLeftButton=findViewById(R.id.leftButton);
        moveLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        gameView.drawingThread.dock.startMovingLeft();
                        moveLeftButton.getBackground().setAlpha(100);

                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.drawingThread.dock.stopMovingLeft();
                        moveLeftButton.getBackground().setAlpha(255);

                        break;
                    default:
                        break;
                }

                return false;
            }
        });
        Button moveRightButton=findViewById(R.id.rigntButton);

        moveRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        gameView.drawingThread.dock.startMovingRight();
                        moveRightButton.getBackground().setAlpha(100);

                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.drawingThread.dock.stopMovingRight();
                        moveRightButton.getBackground().setAlpha(255);

                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        /*Button moveRightButton=findViewById(R.id.rigntButton);
        moveRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/

    }

    private void initializeSensors() {
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
                {
                    gX=-event.values[0];
                    gY=event.values[1];
                    if(gY<0)
                    {
                        stopUsingSensors();
                        gameView.drawingThread.animationThread.stopThread();
                        gameView.drawingThread.scoreCounterThread.stopThread();

                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(GameActivity.this);
                        alertBuilder.setTitle("No Cheating!!");
                        alertBuilder.setIcon(R.drawable.warning);
                        alertBuilder.setMessage("থামেন ভাই! মোবাইল ধরা শিখেন নি নাকি?");

                        alertBuilder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                restartGame(null);

                            }
                        });
                        alertBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopGame(null);

                            }
                        });
                        alertBuilder.show();
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        startUsingSensors();
    }

    private void startUsingSensors() {
        sensorManager.registerListener(sensorEventListener,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopUsingSensors()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        startUsingSensors();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startUsingSensors();
        super.onResume();
    }

    @Override
    protected void onStop() {
        stopUsingSensors();
        super.onStop();
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    public void pauseGame(View view)
    {
        if(gameView.drawingThread.pauseFlag==false)
        {

            gameView.drawingThread.animationThread.stopThread();
            gameView.drawingThread.pauseFlag=true;stopUsingSensors();
            view.setBackgroundResource(R.drawable.unlock);
        }
        else
        {
            gameView.drawingThread.animationThread=new AnimationThread(gameView.drawingThread);
            gameView.drawingThread.animationThread.start();
            view.setBackgroundResource(R.drawable.stop);
            gameView.drawingThread.pauseFlag=false;
        }
    }
    public void restartGame(View view)
    {
        stopUsingSensors();
        startUsingSensors();

        gameView.drawingThread.stopThread();
        gameView.drawingThread=new DrawingThread(gameView,this);
        gameView.drawingThread.start();

        Toast.makeText(this,"Game Restarted",Toast.LENGTH_SHORT).show();

        //gameView.drawingThread.allRobots.clear();
    }
    public void stopGame(View view)
    {
        this.finish();
    }
}