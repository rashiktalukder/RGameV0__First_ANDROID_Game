package com.rashikgame.rgamev0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

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
            gameView.drawingThread.pauseFlag=true;
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
        gameView.drawingThread.allRobots.clear();
    }
    public void stopGame(View view)
    {
        this.finish();
    }
}