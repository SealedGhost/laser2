package com.laserGun;



import android.util.Log;

import com.uidata.CommonData;

/**
 * Created by summersunshine on 2015/11/12.
 * to detect whether wifi is on and socket is connected
 */
public class DetectThread extends Thread {
    public void run()
    {
        while(CommonData.isRunning)
        {
            if(!CommonData.wifiAdmin.isConnected()) {
                Log.e("wifi","disconnected");
                CommonData.wifiAdmin.openWifi();
                CommonData.wifiAdmin.addNetwork(CommonData.wifiAdmin.CreateWifiInfo(CommonData.SSID, CommonData.PSWD, CommonData.NETTYPE));
            }
            String ssid = "\""+CommonData.SSID+"\"";
            String s = CommonData.wifiAdmin.getSSID();
            if( !CommonData.wifiAdmin.getSSID().equals(ssid))
            {
                CommonData.wifiAdmin.addNetwork(CommonData.wifiAdmin.CreateWifiInfo(CommonData.SSID, CommonData.PSWD, CommonData.NETTYPE));
                CommonData.dataProcess.stopConn();
                try
                {
                    Thread.sleep(3000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(CommonData.dataProcess.socket == null || !CommonData.dataProcess.socket.isConnected() || CommonData.dataProcess.socket.isClosed())
            {
                CommonData.dataProcess.startConn();
            }
            try
            {
                Thread.sleep(1500);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
         }
    }
}
