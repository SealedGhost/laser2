package com.HitMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.laserGun.MainActivity;
import com.laserGun.R;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;

public class Hit_Activity extends Activity {
    private TextView tv_mode1;
    private TextView tv_mode2;
    private TextView tv_mode3;
    private TextView tv_return;
    private TextView tv_set;
    private TextView tvmode1time;
    private TextView tvmode2time;
    private TextView tvmode3time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoot_main);
        tv_mode1 = (TextView)findViewById(R.id.tv_common);
        tv_mode2 = (TextView)findViewById(R.id.tv_progress);
        tv_mode3 = (TextView)findViewById(R.id.tv_challenge);
        tv_return = (TextView)findViewById(R.id.tv_compete_return);
        tv_set = (TextView)findViewById(R.id.tv_set);
        tvmode1time =(TextView)findViewById(R.id.tvmode1time);
        tvmode2time =(TextView)findViewById(R.id.tvmode2time);
        tvmode3time =(TextView)findViewById(R.id.tvmode3time);

        tvmode1time.setText(PreferenceUtils.getPrefInt(this,PreferenceConstants.Mode1,30)+"s");
        tvmode2time.setText(PreferenceUtils.getPrefInt(this,PreferenceConstants.Mode2,15)+"s");
        tvmode3time.setText(PreferenceUtils.getPrefInt(this,PreferenceConstants.Mode3,10)+"s");
        TouchListener touchListener = new TouchListener();
        tv_mode1.setOnTouchListener(touchListener);
        tv_mode2.setOnTouchListener(touchListener);
        tv_mode3.setOnTouchListener(touchListener);
        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Intent intent = new Intent(Hit_Activity.this, MainActivity.class);
                    startActivity(intent);
                    Hit_Activity.this.finish();
                }
                return false;
            }
        });
        tv_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startActivity(new Intent(Hit_Activity.this, HitSet_Activity.class));
                    Hit_Activity.this.finish();
                }
                return false;
            }
        });

    }
    private class TouchListener implements View.OnTouchListener
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                Intent intent = new Intent(Hit_Activity.this, HitModeActivity.class);
                TextView tvView = (TextView)v;
                String strModeName = tvView.getText().toString();
                int iTime = PreferenceUtils.getPrefInt(Hit_Activity.this, strModeName, 0);
                intent.putExtra("ModeName", strModeName);
                intent.putExtra("Time", iTime);
                startActivity(intent);
            }
            return false;
        }
    }
    public void onBackPressed() {
        Log.i("HitActivity", "backpress");
        Intent intent = new Intent(Hit_Activity.this, MainActivity.class);
        startActivity(intent);
        Hit_Activity.this.finish();
    }
}
