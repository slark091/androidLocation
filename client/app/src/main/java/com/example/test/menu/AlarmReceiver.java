package com.example.test.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.autonavi.aps.amapapi.model.AMapLocationServer;

/**
 * Created by test on 18-6-1.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("LOCATION_CLOCK")){
            Log.d("alarmTest" , "onReceive location clock");
            Intent intent1 = new Intent(context , AMapLocationServer.class);
//            context.startService(MainActivity)



        }
    }
}
