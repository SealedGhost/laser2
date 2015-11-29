package com.laserGun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.CompeteMode.Compete_Activity;
import com.ExerciseMode.Exercise_Activity;
import com.HitMode.Hit_Activity;
import com.uidata.CommonData;

import static java.lang.System.exit;


public class MainActivity extends Activity {

    private TextView tv_exercise;
    private TextView tv_hit;
    private TextView tv_compete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_exercise = (TextView)findViewById(R.id.tv_exercise);
        tv_hit = (TextView)findViewById(R.id.tv_hit);
        tv_compete = (TextView)findViewById(R.id.tv_compete);
        tv_hit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                   startActivity(new Intent(MainActivity.this, Hit_Activity.class));
                }
                return false;
            }
        });
        tv_exercise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startActivity(new Intent(MainActivity.this, Exercise_Activity.class));
                }
                return false;
            }
        });
        tv_compete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(MainActivity.this, Compete_Activity.class));
                }
                return false;
            }
        });
   }
    private long firstTime;
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            Log.i("MainActivity","destroy");
            CommonData.isRunning = false;
            CommonData.dataProcess.stopConn();
            getApplication().onTerminate();
            this.finish();
            exit(0);

        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(this, R.string.try_again, Toast.LENGTH_LONG);
        }
    }
 }


