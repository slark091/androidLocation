package com.example.test.menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.MapView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    final public String tag = "MainActivity";


    private ImageView clickToLogin;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;
    private selfFunction func;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        func = new selfFunction(MainActivity.this) ;



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = func.sharedPreferences.getString("phone" , "");
                if(!phone.equals("")){
                    initCalendarDialog(MainActivity.this);
                    postStructList listData = new postStructList();
                    listData.add("phone" , phone );

                    func.post("time/getCalendar" , listData  );
                }else {
                    func.toast("需要登录");
                    Intent intent = new Intent(MainActivity.this , loginActivity.class);
                    startActivity(intent);
                }

                
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

            if((ContextCompat.checkSelfPermission(this ,
                    (Manifest.permission.ACCESS_COARSE_LOCATION )))!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this , new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                } , 1);

            }

            initAmapLocation();

        }catch (Throwable e){
//            e.printStackTrace();
            func.infoPush((e) );
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

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, loginActivity.class);



            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {


            func.infoPush(func.sharedPreferences.getString("phone" , "null") );


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {


        }else if(id == R.id.drawer_layout){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







    private void initAmapLocation(){

        aMapLocationClient = new AMapLocationClient(this.getApplicationContext());

        AMapLocationClientOption option = new AMapLocationClientOption();

        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);

        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        option.setOnceLocation(true);

        option.setHttpTimeOut(10*1000);


        if(null != aMapLocationClient){
            aMapLocationClient.setLocationOption(option);
            aMapLocationClient.stopLocation();
            aMapLocationClient.startLocation();

        }
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){


                    }else{
                        func.infoPush(aMapLocation);
                    }


                }
                aMapLocationClient.stopLocation();
            }
        });
    }


    public AlertDialog calendarDialog;
    private MapView mapView = null;
    public MaterialCalendarView materialCalendarView ;
    private CalendarDay calendarDay;
    public ImageButton imageButton;



    private void initCalendarDialog(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.dialog);

        View view = View.inflate(context , R.layout.calender , null);

        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.mcv);
        imageButton = (ImageButton) view.findViewById(R.id.sign_submit);

        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func.loading();

                try {
                    AMapLocation aMapLocation = aMapLocationClient.getLastKnownLocation();

                    if(aMapLocation!=null){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date current = new Date();
                        String currentStr = simpleDateFormat.format(current);

                        postStructList listData = new postStructList();
                        listData.add("lat" , aMapLocation.getLatitude());
                        listData.add("lng" , aMapLocation.getLongitude());
                        listData.add("time" , currentStr);
                        listData.add("phone" , func.sharedPreferences.getString("phone" , "") );


                        func.post("edit/index" , listData  );
                    }else {
                        func.infoPush("定位失败,需要定位权限");
                        func.endLoading();
                    }


                }catch (Throwable e){
                    func.infoPush(e);
                }


            }
        });



        builder.setView(view);

        calendarDialog = builder.create();



    }



}
