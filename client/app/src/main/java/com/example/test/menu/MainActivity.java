package com.example.test.menu;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final public String tag = "MainActivity";

    private ImageView clickToLogin;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                infoPush(getAdressMacByInterface());
                try {
                    AMapLocation aMapLocation = aMapLocationClient.getLastKnownLocation();

                    testActivity temp = new testActivity();

                    postStructList listData = new postStructList();
                    listData.add("latitude" , aMapLocation.getLatitude());
                    listData.add("longitude" , aMapLocation.getLongitude());


                    infoPush(aMapLocation);
                    temp.post("index/sign" , listData );
                }catch (Throwable e){
                    infoPush((e));
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//                Intent intent = new Intent(MainActivity.this , testActivity.class);
//                startActivity(intent);
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(MainActivity.this , testActivity.class);
////                startActivity(intent);
//
//
//
//            }
//        });

        try {
            clickToLogin = (ImageView) findViewById(R.id.imageView);
//        clickToLogin.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this , testActivity.class);
//                startActivity(intent);
//            }
//        });
            aMapLocationClient = new AMapLocationClient(this.getApplicationContext());
            aMapLocationClientOption = setDefaultOption();
            aMapLocationClient.setLocationOption(aMapLocationClientOption);

            aMapLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if(aMapLocation != null){
                        if(aMapLocation.getErrorCode() == 0){
                            infoPush((aMapLocation.getAltitude()));


                        }else{
                            infoPush(aMapLocation.getErrorInfo());
                        }


                    }
                    aMapLocationClient.stopLocation();
                }
            });

            aMapLocationClient.startLocation();
        }catch (Throwable e){
//            e.printStackTrace();
            infoPush((e));
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.fab) {
            infoPush("sign up");

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            infoPush("test");
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, testActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
//            locationClient.setLocationOption();


//            AMapLocation aMapLocation = aMapLocationClient.getLastKnownLocation();
//            infoPush(String.valueOf(aMapLocation.getLatitude()));
//            infoPush(String.valueOf(aMapLocation == null) );



        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {


        }else if(id == R.id.drawer_layout){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean infoPush(Object msg){
        String temp = (msg == null) ? "null" : msg.toString();
        new  AlertDialog.Builder(this)

                .setMessage(temp)
                .show();



        return true;
    }




    public boolean test(){

        getApplicationContext().startService(new Intent(this , locationServer.class));






        return true;
    }
    public boolean startAlarm(){

        Intent intent = new Intent("LOCATION_CLOCK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this , 0  , intent , 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        long second = 60*1000;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP , System.currentTimeMillis() , second , pendingIntent);


        return  true;
    }

    public boolean setTextView( String string ){
        TextView textView = (TextView) findViewById(R.id.mainText);
        textView.setText(string);

        return true;
    }
    private AMapLocationClientOption setDefaultOption(){
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setGpsFirst(false);
        option.setHttpTimeOut(33333);
        int interval = 2*1000;
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






}
