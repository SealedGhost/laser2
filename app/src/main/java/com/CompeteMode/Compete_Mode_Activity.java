package com.CompeteMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.laserGun.R;
import com.uidata.CommonData;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;

import java.io.IOException;
import java.util.ArrayList;


public class Compete_Mode_Activity extends Activity {
    private TextView tv_resttime;
    private TextView tv_hitnum;
    private TextView tv_return;
    private TextView tv_start;
    private TextView tv_mode_name;
    private TextView tv_ring;
    int nRest_time;
    int nFrenquency;
    int nDifficult;
    boolean isRunning = false;//to signal whether is started
    boolean isOver = false;//to end sending data in advance;
    boolean isActive = true;//to signal whether activity is active
    int totalHitNum = 0;
    MyBroadCastReceiver myBroadcastReceiver;
    ArrayList list = new ArrayList();
    Handler timeHandler;
    Handler showHandler;

    Drawable dwPress;
    Drawable dwDisable;
    int Gray;
    int Black;

    boolean isHit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competemode);
        dwPress = getResources().getDrawable(R.drawable.pressed);
        dwDisable = getResources().getDrawable(R.drawable.disabled);
        Gray = getResources().getColor(R.color.gray);
        Black = getResources().getColor(R.color.black);
        Intent intent = getIntent();
        nFrenquency = intent.getIntExtra("Time", 0);//how frequent it falls
        String strModeName = intent.getStringExtra("ModeName");
        nDifficult = intent.getIntExtra("Difficult", 0);
        tv_resttime = (TextView) findViewById(R.id.tv_rest_time);
        tv_return = (TextView) findViewById(R.id.tv_compete_return);
        tv_start = (TextView) findViewById(R.id.tv_compete_start);
        tv_hitnum = (TextView) findViewById(R.id.tv_hit_num);
        tv_mode_name = (TextView) findViewById(R.id.tv_competemodename);
        tv_ring = (TextView)findViewById(R.id.tv_showring);
        tv_mode_name.setText(strModeName);
        nRest_time = PreferenceUtils.getPrefInt(this, PreferenceConstants.GameTime, 0);

        isRunning = false;
        isOver = false;
        isActive = true;

        isHit = false;

        if (nRest_time == 0) {
            nRest_time = 180;
            PreferenceUtils.setPrefInt(this, PreferenceConstants.GameTime, nRest_time);
        }
        tv_resttime.setText(nRest_time + "");
        tv_hitnum.setText(0 + "");
        tv_ring.setText("0");

        myBroadcastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
       // intentFilter.addAction("CompeteACK");
        registerReceiver(myBroadcastReceiver, intentFilter);

        //DetectTargetThread detectThread = new DetectTargetThread();
       // detectThread.start();

        timeHandler = new Handler() {
            public void handleMessage(Message msg) {
                int ntime = (Integer) msg.obj;
                tv_resttime.setText("" + ntime);
            }
        };

        showHandler = new Handler()
        {
            public void handleMessage(Message msg) {
                tv_start.setBackground(dwPress);
                tv_start.setTextColor(Black);
                tv_resttime.setText(""+nRest_time);
                tv_hitnum.setText("" + 0);
                tv_ring.setText("0");
            }
        };
        tv_start.setOnTouchListener(new TouchListener());
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isOver && isRunning) {
                        CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
                    }
                    isActive = false;
                    startActivity(new Intent(Compete_Mode_Activity.this, Compete_Activity.class));
                    Compete_Mode_Activity.this.finish();
                }
                return false;
            }
        });
    }

    class TouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            if (!isRunning) {
                tv_start.setBackground(dwPress);
                tv_start.setTextColor(Gray);
                totalHitNum = 0;
                tv_hitnum.setText(totalHitNum + "");
                tv_resttime.setText(nRest_time + "");
                isRunning = true;
                isOver = false;
                TimeThread timeThread = new TimeThread();
                timeThread.start();
                RunThread runThread = new RunThread();
                runThread.start();
            }
            return false;
        }
    }

    class RunThread extends Thread//to produce random number to control target stand
    {
        public void run() {

            while (!isOver && isActive) {
                int nSize = list.size();
                if (nSize != 0) {
                    CommonData.dataProcess.sendCmd(0x01, CommonData.COMPETECMD, CommonData.STARTSTT, nFrenquency / 1000, 0x00);
                    int nTime = nFrenquency / 1000 + 1;
                    isHit = false;
                    while (nTime > 0 && !isHit) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {

                        }
                        nTime--;
                    }
                }
            }
        }
    }

    class TimeThread extends Thread {
        public void run() {
            while (nRest_time > 0 && isActive) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                nRest_time--;
                if (nRest_time == 2)//let RunThread stop in advance
                {
                    isOver = true;
                    isHit = true;
                    CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
                }
                Message message = Message.obtain();
                message.obj = nRest_time;
                timeHandler.sendMessage(message);
            }
            if (nRest_time == 0) {
                isRunning = false;
                isOver = false;
                Message msg = Message.obtain();
                showHandler.sendMessage(msg);
                nRest_time = PreferenceUtils.getPrefInt(Compete_Mode_Activity.this, PreferenceConstants.GameTime, 0);
            }
        }
    }

    class DetectTargetThread extends Thread {//to know what targets are used

        public void run() {
            while (isActive) {
                CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.ACKSTT, 0x00, 0x00);
                try {
                    Thread.sleep(CommonData.DetectTime);
                } catch (Exception e) {
                }
            }
        }
    }

    class MyBroadCastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           /* if (action.equals("CompeteACK")) {
                int iTarget = intent.getIntExtra("TargetExist", 0);
                if (iTarget != 0) {
                    if (!list.contains(iTarget)) {
                        list.add(iTarget);
                    }
                }
            }*/
            if (action.equals("ReceiveData")) {
                int hitNum = intent.getIntExtra("HitNum", 0);
                int nRing = intent.getIntExtra("Ring" ,0);
                if (hitNum != 0 && isRunning ) {
                    isHit = true;
                    totalHitNum++;
                    tv_hitnum.setText("" + totalHitNum);
                    tv_ring.setText("" + nRing);
                }
            }
        }
    }

    public void onBackPressed() {
        if (!isOver && isRunning) {
            CommonData.dataProcess.sendCmd(0x00, CommonData.COMPETECMD, CommonData.STOPSTT, 0x00, 0x00);
        }
        isActive = false;
        isHit = true;
        startActivity(new Intent(Compete_Mode_Activity.this, Compete_Activity.class));
        Compete_Mode_Activity.this.finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}
