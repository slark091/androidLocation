package com.example.test.menu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by test on 18-6-1.
 */

public class testServer extends Service {

    private static final String tag = "testServer" ;

    @Nullable

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent , int flags , int startId){
        Log.d(tag , "onStartCommand");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            Log.d("testLogPrint" , "TTTTTTT");


        return super.onStartCommand(intent , flags , startId);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(tag , "onDestory");
    }







    @Override


    public IBinder onBind(Intent intent) {



        return null;

    }
}
