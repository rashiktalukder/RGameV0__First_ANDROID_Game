package com.rashikgame.rgamev0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button startButton= findViewById(R.id.androidButton);

        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                            startButton.getBackground().setAlpha(170);

                            break;
                    case MotionEvent.ACTION_UP:
                                Intent intent=new Intent(MainActivity.this,GameActivity.class);
                                startActivity(intent);
                                startButton.getBackground().setAlpha(255);

                                break;
                    default:
                        break;
                }


                return false;
            }
        });


    }
}