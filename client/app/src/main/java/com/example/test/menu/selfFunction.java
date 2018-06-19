package com.example.test.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by test on 18-6-1.
 */




class postStructList{
    private List<postStruct> dataList = new ArrayList<>() ;
    private postStruct postItem;
    private boolean loginJudge = true;
    postStructList(){

    }
    public void add(String name , Object value ){

        postItem = new postStruct();
        postItem.name = name;
        postItem.value = String.valueOf(value);
        dataList.add(postItem);
    }
    public List<postStruct> getDataList(){
        return  dataList;
    }
    public void setLoginJudge(boolean value){
        loginJudge = value;
    }
    public boolean getLoginJudge(){
        return loginJudge;
    }

}

class postStruct{
    public String name;
    public  String value;
}





public class selfFunction {



    selfFunction(Context context){
        selfFunctionContext = context;
        init();
    }

    //init selfFunction

    private String tag = "selfFunction";
    public Handler handler;

    private AlertDialog loadingDialog;

    public Context selfFunctionContext ;


    public     SharedPreferences.Editor editor;
    public     SharedPreferences sharedPreferences;
    private Transition transition;

    private  boolean judgeSignButton = false;



    private void init(){

        editor = selfFunctionContext.getSharedPreferences("login" , MODE_PRIVATE).edit();
        sharedPreferences = selfFunctionContext.getSharedPreferences("login" , MODE_PRIVATE);

        HandlerThread thread = new HandlerThread(tag);
        thread.start();
        handler = new Handler(thread.getLooper()){

            public void  handleMessage(Message msg){
                final Context context = (Context) msg.obj;
                final String what = msg.getData().getString("name");
                final String data = msg.getData().getString("data");
                Activity activity = (Activity)context;
                try{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    if(data.contains("loginJudge/")){
                        String judge = data.substring(11);
                        switch (judge){
                            default:break;

                            case  "findError":{
                                toast("账号异常,请重新注册");
                                break;
                            }
                            case  "codeError":{
                                toast("密码已修改,请重新登录");
                                break;
                            }
                            case  "macError":{
                                toast("账号物理地址异常,请重新登录");
                                break;
                            }
                            case  "statusError":{
                                toast("账号已登出,请重新登录");
                                break;
                            }
                            case  "timeOut":{
                                toast("太久没有登录,请重新登录");
                                break;
                            }
                        }
                        Intent intent = new Intent(context , loginActivity.class);
                        context.startActivity(intent);

                        return;

                    }




                    switch (what){
                        default:{
                            break;
                        }
                        case "map/getPolygons":{
                            MainActivity mainActivity = (MainActivity) context;

                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(data);

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
//                            jsonArray.get(0);

                            int len = jsonArray.length();
                            for(int i = 0 ; i < len ; i ++ ){
                                JSONObject jsonObject = null;
                                double lat  , lng;
                                try {
                                    jsonObject = new JSONObject( jsonArray.getString(i));
                                    JSONArray jsonArray1 ;
                                    jsonArray1 = new JSONArray(jsonObject.getString("fence_array"));
                                    List<LatLng> latLngs = new ArrayList<>();

                                    int len1 = jsonArray1.length();
                                    for(int j = 0 ; j < len1 ; j++ ){
                                       JSONObject jsonObject1 ;
                                       jsonObject1 = new JSONObject(jsonArray1.getString(j));
                                       lat = jsonObject1.getDouble("lat");
                                       lng = jsonObject1.getDouble("lng");
                                        latLngs.add(new LatLng(lat , lng));
                                    }
                                    mainActivity.aMap.addPolygon(new PolygonOptions()
                                            .addAll(latLngs).fillColor(0x75638233)
                                            .strokeColor(0x75638233).strokeWidth(1));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        }

                        case "time/getCalendar":{
                            final MainActivity mainActivity = (MainActivity) context;

                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            jsonArray.get(0);
                            final int len = jsonArray.length();

                            final Date[] calendarDate = new Date[len];
                            Date current = new Date();
                            judgeSignButton = false;
                            for(int i = 0 ; i < len ; i ++ ){
                                JSONObject jsonObject = null;
                                String time = null;

                                try {
                                    jsonObject = new JSONObject( jsonArray.getString(i));
                                    time = jsonObject.get("time").toString();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                ParsePosition parsePosition = new ParsePosition(0);
                                calendarDate[i] = simpleDateFormat.parse(time , parsePosition);
                                if(isSameDay(current , calendarDate[i])){
                                    judgeSignButton = true;
                                }

                            }


                                    for(int i = 0; i < len ; i++){
                                        mainActivity.materialCalendarView.
                                                setDateSelected(calendarDate[i] , true);
                                    }
                                    if(!judgeSignButton){
                                        mainActivity.imageButton.setVisibility(View.VISIBLE);
                                    }




                                    mainActivity.calendarDialog.show();
//                                    mainActivity.imageButton.setVisibility(View.GONE);

                            break;
                        }
                        case "edit/index" :{
                            final MainActivity mainActivity = (MainActivity) context;

                            if(data.equals("outOfArea")){
                                        infoPush("不在规定区域" , context);

                            }else {
                                        toast("信息已登记");
                                        SimpleDateFormat simpleDateFormat =
                                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        ParsePosition parsePosition = new ParsePosition(0);
                                        Date date= simpleDateFormat.parse(data , parsePosition);
                                        mainActivity.materialCalendarView.setDateSelected(date , true);
                                        mainActivity.imageButton.setVisibility(View.INVISIBLE);
                            }



                            loadingDialog.cancel();


                            break;
                        }
                        case "index/getMsg":{
//                        infoPush("验证码")

                            if(data.equals("OK") ){
                                final loginActivity login = (loginActivity) context;
                                        toast("短信已发送");
                                        login.sign_up1.setVisibility(View.GONE);
                                        login.sign_up2.setVisibility(View.VISIBLE);
                            }else{
                                infoPush(data, selfFunctionContext);
                            }
                            loadingDialog.cancel();

                            break;
                        }
                        case "index/sign":{
                            final loginActivity login = (loginActivity) context;

                            if(data.equals("success")){
                                        login.sign_up2.setVisibility(View.GONE);
                                        login.sign_up3.setVisibility(View.VISIBLE);



                            }else {
                                        login.verificationCodeView.clearInputContent();
                                        infoPush(data, selfFunctionContext);




                            }
                            loadingDialog.cancel();

                            break;
                        }
                        case "index/signUp":{
                            final loginActivity login = (loginActivity) context;

                            if(data.equals("dataBaseError")){

                                infoPush(data , context);

                            }else{
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String phone = "" , code = "";

                                try {
                                    phone = jsonObject.getString("phone");
                                    code = jsonObject.getString("code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                editor.putString("phone" , phone );
                                editor.putString("code" , code );
                                editor.apply();
                                        toast("注册成功");
                                            login.userNmaeInput.setText(phone);
                                            login.codeInput.setText(code);

                                            login.signDialog.cancel();
                                            login.finish();
                                            return;

                            }
                            loadingDialog.cancel();


                            break;
                        }


                        case "index/login":{
                            final loginActivity login = (loginActivity) context;
                            if(!data.equals("failed")){
                                JSONObject jsonObject = null;
                                String phone = null;
                                String code = null;

                                try {
                                    jsonObject = new JSONObject(data);
                                    phone = jsonObject.getString("phone");
                                    code = jsonObject.getString("code");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                editor.putString("phone" , phone );
                                editor.putString("code" , code );
                                editor.apply();
                                        toast("登录成功");

                                        login.finish();

//                                            TimerTask timerTask = new TimerTask() {
//                                                @Override
//                                                public void run() {
//                                                    try {
//
//                                                    }catch (Throwable e){
//                                                        toast(e);
//                                                    }
//
//                                                }
//                                            };
//                                            Timer timer = new Timer();
//                                            timer.schedule(timerTask , 800);
                                            return;


                            }else {
                                infoPush("账号密码错误", context);
                            }
                                    login.recovery();
                            break;
                        }


                    }
                        }
                    });
                }catch (Throwable e ){
                    Log.d(tag , e.toString());
                    e.printStackTrace();
                }







            }
        };
    }

    //init selfFunction

    public String md5(String string) {
        string += "test";

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error";
    }


    public void post(final String method  , postStructList list , final Context context ){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            infoPush("没有网络连接");
            return ;
        }

        final  String info = handleInfo(list);
        final String path = context.getString(R.string.postTarget) + method;
        new Thread() {
            public void run() {
//                while (true)
                {

                    try {
                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
                        conn.setRequestMethod("POST");
                        conn.setReadTimeout(5000);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                        conn.setRequestProperty("Content-Length",String.valueOf(info.length()));
                        conn.setDoOutput(true);
                        conn.getOutputStream().write(info.getBytes());

                        int code = conn.getResponseCode();
                        if (code == 200) {


                                InputStream is = conn.getInputStream();
                                Scanner temp = new Scanner(is).useDelimiter("\\A");
                                final String result = temp.hasNext() ? temp.next() : "";


                                Message msg = Message.obtain();

                                msg.what = 1;
                                msg.obj = context;

                                Bundle bundle = new Bundle();
                                bundle.putString("data" , result);
                                bundle.putString("name" , method );

                                msg.setData(bundle);


                                handler.sendMessage(msg);





                        } else {

                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();


                    }
                }

            }
        }.start();
    }

    public void post(final  String method , postStructList list){
        post(method , list  , selfFunctionContext);
    }

    public void  loading(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context , R.style.dialog);

        View view1 = View.inflate(context , R.layout.loading , null);

        builder.setView(view1);
        loadingDialog = builder.create();


        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();


    }
    public void loading(){
        loading(selfFunctionContext);
    }

    public String handleInfo(postStructList list){


        if(list.getLoginJudge()){
            list.add("phone" , sharedPreferences.getString("phone" , "") );
            list.add("code" , sharedPreferences.getString("code" , "") );
            list.add("mac"  , getAdressMacByInterface() );
        }
        List<postStruct> infoArray = list.getDataList();

        String info = "";

        for(int i = 0 ; i < infoArray.size() ; i++){
            if(info.equals("")){
                info +=  infoArray.get(i).name + "=" + infoArray.get(i).value;
            }else{
                info += "&" + infoArray.get(i).name + "=" + infoArray.get(i).value;

            }

        }
        return info;


    }

    public boolean infoPush(Object msg , Context context   ){

        String temp = (msg == null) ? "null" : msg.toString();
        new  AlertDialog.Builder(context , R.style.infoPush)

                .setMessage(temp)
                .show();



        return true;
    }
    public boolean infoPush(Object msg){

        return infoPush(msg , selfFunctionContext);

    }

    public void toast(Object msg){

        Toast.makeText(selfFunctionContext ,
                (msg == null) ? "null" : msg.toString() , Toast.LENGTH_LONG ).show();

    }
    public void endLoading(){
        loadingDialog.cancel();
    }

    public boolean isSameDay(Date date1 , Date date2){

        int year1 = date1.getYear();
        int year2 = date2.getYear();
        int month1 = date1.getMonth();
        int month2 = date2.getMonth();
        int day1 = date1.getDate();
        int day2 = date2.getDate();

        return year1 == year2 && month1 == month2 && day1 == day2;


    }

    public String getAdressMacByInterface() {
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

}
