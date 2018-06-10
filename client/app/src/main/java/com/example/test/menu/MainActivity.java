package com.example.test.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.MapView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    final public String tag = "MainActivity";


    private ImageView clickToLogin;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private selfFunction func = new selfFunction() ;

    private MapView mapView = null;
    private MaterialCalendarView materialCalendarView ;
    private CalendarDay calendarDay;


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


                    postStructList listData = new postStructList();
                    listData.add("latitude" , aMapLocation.getLatitude());
                    listData.add("longitude" , aMapLocation.getLongitude());


//                    func.infoPush(aMapLocation , MainActivity.this);

//                    LatLng latLng = new LatLng(aMapLocation.getLatitude() , aMapLocation.getLongitude());

//                    Marker marker = new Marker();

                    func.post("index/sign" , listData , MainActivity.this );
                }catch (Throwable e){
//                    func.infoPush((e) , MainActivity.this);
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View view1 = View.inflate(MainActivity.this , R.layout.calender , null);

                materialCalendarView = (MaterialCalendarView) view1.findViewById(R.id.mcv);
                materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);

                Date testDate = new Date();
                testDate.setDate(12);

                CalendarDay temp =  CalendarDay.from(testDate);

                materialCalendarView.setDateSelected(Calendar.getInstance().getTime() , true);
                materialCalendarView.setDateSelected(testDate , true);
                




                builder.setView(view1);

                AlertDialog dialog = builder.create();

                dialog.show();
                dialog.getWindow();

//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();











                
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        try {
            clickToLogin = (ImageView) findViewById(R.id.imageView);
//        clickToLogin.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this , loginActivity.class);
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
//                            func.infoPush((aMapLocation.getAltitude()) , MainActivity.this);


                        }else{
                            func.infoPush(aMapLocation.getErrorInfo() , MainActivity.this);
                        }


                    }
                    aMapLocationClient.stopLocation();
                }
            });

            aMapLocationClient.startLocation();
        }catch (Throwable e){
//            e.printStackTrace();
            func.infoPush((e) , MainActivity.this);
        }


































        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);









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
            func.infoPush("sign up" , this);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            func.infoPush("test" , this);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, loginActivity.class);
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
