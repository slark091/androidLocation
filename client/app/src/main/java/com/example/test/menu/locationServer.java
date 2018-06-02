package com.example.test.menu;

/**
 * Created by test on 18-6-1.
 */

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.app.AlarmManager;



public class locationServer
{
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationClientOption = null;


//    private boolean startAlarm(){
//
//        Intent intent = new Intent("LOCATION_CLOCK");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this , 0  , intent , 0);
//
//
//        return  true;
//    }




    private AMapLocationClientOption getDefaultOption(){
     AMapLocationClientOption option = new AMapLocationClientOption();
     option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
     option.setGpsFirst(false);
     option.setHttpTimeOut(33333);
     int interval = 10*60*1000;
     option.setInterval(interval);
     option.setNeedAddress(true);
     option.setOnceLocation(false);
     option.setOnceLocationLatest(false);
     AMapLocationClientOption.setLocationProtocol(
             AMapLocationClientOption.AMapLocationProtocol.HTTP);
     option.setSensorEnable(false);
     option.setWifiScan(true);
     option.setLocationCacheEnable(true);
     return option;



    }






    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(null != aMapLocation){

                if(aMapLocation.getErrorCode() == 0){

//                    Log.d("aMapLocation" , "no error");

                    Log.d("aMapLocation" ,  String.valueOf(aMapLocation.getLongitude())) ;


                }
            }
            Log.d("aMapLocation" , "error");
        }
    };

}
