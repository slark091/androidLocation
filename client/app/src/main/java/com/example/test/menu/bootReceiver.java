package com.example.test.menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by test on 18-6-1.
 */

public class bootReceiver extends BroadcastReceiver {
    private   PendingIntent mAlarmSender ;
    @Override
    public void onReceive(Context context, Intent intent) {
        mAlarmSender = PendingIntent.getService(context , 0 ,
                new Intent(context , SwipeRefreshLayout.OnRefreshListener.class ) , 0);

        long firstTime = SystemClock.elapsedRealtime();
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);

        alarmManager.cancel(mAlarmSender);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME ,
                firstTime , 30*1000 , mAlarmSender);

    }
}
