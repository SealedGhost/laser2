package com.HitMode;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.laserGun.R;

import java.util.ArrayList;

public class QueryGrade  extends Activity {

    ArrayList<String> arrGrade;
    int nHit;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    TextView tv_return;
    GradeAdapter gradeAdapter;
    MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grade);
        Intent intent = getIntent();
        arrGrade = intent.getStringArrayListExtra("gradeArrayList");
        nHit = intent.getIntExtra("HitPosition", 0);

        tv_return = (TextView) findViewById(R.id.tv_gradeReturn);
        recyclerView = (RecyclerView) findViewById(R.id.rv_grade);
        gridLayoutManager = new GridLayoutManager(this, 5);

        recyclerView.setLayoutManager(gridLayoutManager);
        gradeAdapter = new GradeAdapter(arrGrade);
        recyclerView.setAdapter(gradeAdapter);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReceiveData");
        registerReceiver(myBroadcastReceiver, intentFilter);

        tv_return.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    Intent intent = new Intent();
                    intent.putExtra("HitPosition" ,nHit);
                    intent.putExtra("gradeArrayList" ,arrGrade);
                    QueryGrade.this.setResult(1, intent);
                    finish();
                }
                return false;
            }
        });
    }
    public class MyBroadcastReceiver extends BroadcastReceiver
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action.equals("ReceiveData"))
            {
                int hitNum = intent.getIntExtra("HitNum", 0);
                int ring = intent.getIntExtra("Ring", 0);
                if( hitNum != 0)
                {
                    arrGrade.add("" + ring);
                    gradeAdapter.notifyItemInserted(arrGrade.size() - 1);
                }
            }
        }
    }
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("HitPosition" ,nHit);
        intent.putExtra("gradeArrayList" ,arrGrade);
        QueryGrade.this.setResult(1, intent);
        finish();
    }
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }

}

