package com.laserGun;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.uidata.CommonData;
import com.uidata.PreferenceConstants;
import com.uidata.PreferenceUtils;
import com.uidata.WifiAdmin;

/**
 * Created by summersunshine on 2015/11/13.
 */
public class MyApplication  extends Application {
    private static Context context;


    public void onCreate()
    {
        super.onCreate();
        CommonData.wifiAdmin = new WifiAdmin(this);
        CommonData.wifiAdmin.openWifi();
        DetectThread detectThread = new DetectThread();
        detectThread.start();
        context = getApplicationContext();
        Log.i("MyApplication","start");

        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode1,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode1, 20);

        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode2,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode2, 15);
        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.Mode3,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.Mode3,10);
        if(PreferenceUtils.getPrefInt(this, PreferenceConstants.GameTime,0) == 0)
            PreferenceUtils.setPrefInt(this, PreferenceConstants.GameTime,180);
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
    public void onTerminate() {
        super.onTerminate();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
