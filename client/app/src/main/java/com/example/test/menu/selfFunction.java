package com.example.test.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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







        try {
            ;
        }catch (Throwable e ){

            Log.d(tag , e.toString());
        }

        editor = selfFunctionContext.getSharedPreferences("login" , MODE_PRIVATE).edit();
        sharedPreferences = selfFunctionContext.getSharedPreferences("login" , MODE_PRIVATE);

        HandlerThread thread = new HandlerThread(tag);
        thread.start();
        handler = new Handler(thread.getLooper()){

            public void  handleMessage(Message msg){
                final Context context = (Context) msg.obj;
                String what = msg.getData().getString("name");
                final String data = msg.getData().getString("data");

                try{
                    switch (what){
                        default:break;
                        case "time/getCalendar":{
                            final MainActivity mainActivity = (MainActivity) context;

                            JSONArray jsonArray = new JSONArray(data);
//                            jsonArray.get(0);
                            final int len = jsonArray.length();

                            final Date[] calendarDate = new Date[len];
                            Date current = new Date();
                            judgeSignButton = false;
                            for(int i = 0 ; i < len ; i ++ ){
                                JSONObject jsonObject = new JSONObject( jsonArray.getString(i));
                                String time = jsonObject.get("time").toString();

                                SimpleDateFormat simpleDateFormat =
                                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                ParsePosition parsePosition = new ParsePosition(0);
                                calendarDate[i] = simpleDateFormat.parse(time , parsePosition);
                                if(isSameDay(current , calendarDate[i])){
                                    judgeSignButton = true;
                                }

                            }


                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int i = 0; i < len ; i++){
                                        mainActivity.materialCalendarView.
                                                setDateSelected(calendarDate[i] , true);
                                    }
                                    if(!judgeSignButton){
                                        mainActivity.imageButton.setVisibility(View.VISIBLE);
                                    }




                                    mainActivity.calendarDialog.show();
//                                    mainActivity.imageButton.setVisibility(View.GONE);

                                }
                            });
                            break;
                        }
                        case "edit/index" :{
                            final MainActivity mainActivity = (MainActivity) context;

                            if(data.equals("outOfArea")){
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        infoPush("不在规定区域" , context);

                                    }
                                });
                            }else {
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("信息已登记");
                                        SimpleDateFormat simpleDateFormat =
                                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        ParsePosition parsePosition = new ParsePosition(0);
                                        Date date= simpleDateFormat.parse(data , parsePosition);
                                        mainActivity.materialCalendarView.setDateSelected(date , true);
                                        mainActivity.imageButton.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }



                            loadingDialog.cancel();


                            break;
                        }
                        case "index/getMsg":{
//                        infoPush("验证码")

                            if(data.equals("OK") ){
                                final loginActivity login = (loginActivity) context;
                                login.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        login.sign_up1.setVisibility(View.GONE);
                                        login.sign_up2.setVisibility(View.VISIBLE);
                                    }
                                });


                            }else{
                                infoPush(data, selfFunctionContext);
                            }
                            loadingDialog.cancel();

                            break;
                        }
                        case "index/sign":{
                            final loginActivity login = (loginActivity) context;

                            if(data.equals("success")){
                                login.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        login.sign_up2.setVisibility(View.GONE);
                                        login.sign_up3.setVisibility(View.VISIBLE);


                                    }
                                });

                            }else {
                                infoPush(data, selfFunctionContext);
                                login.verificationCodeView.clearInputContent();

                            }
                            loadingDialog.cancel();

                            break;
                        }
                        case "index/signUp":{
                            final loginActivity login = (loginActivity) context;

                            if(data.equals("dataBaseError")){

                                infoPush(data , context);

                            }else{
                                login.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("注册成功");

                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            String phone = jsonObject.getString("phone");
                                            String code = jsonObject.getString("code");

                                            editor.putString("phone" , phone );
                                            editor.putString("code" , code );
                                            login.userNmaeInput.setText(phone);
                                            login.codeInput.setText(code);
                                            editor.apply();

                                            login.signDialog.cancel();
                                            login.finish();
                                            return;

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            infoPush(e , context);
                                            login.signDialog.cancel();

                                        }




                                    }
                                });
                            }
                            loadingDialog.cancel();


                            break;
                        }


                        case "index/login":{
                            final loginActivity login = (loginActivity) context;
                            if(!data.equals("failed")){

                                login.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("登录成功");

                                        try {
                                            JSONObject jsonObject = new JSONObject(data);
                                            String phone = jsonObject.getString("phone");
                                            String code = jsonObject.getString("code");

                                            editor.putString("phone" , phone );
                                            editor.putString("code" , code );
                                            login.userNmaeInput.setText(phone);
                                            login.codeInput.setText(code);
                                            editor.apply();

                                            login.finish();
                                            return;

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            infoPush(e , context);

                                        }




                                    }
                                });

                            }else {

                                login.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {



                                    }
                                });
                                infoPush("账号密码错误", context);
                            }

                            login.recovery();

                            break;
                        }


                    }
                }catch (Throwable e ){
                    Log.d(tag , e.toString());
                    e.printStackTrace();
                }







            }
        };
    }

    //init selfFunction

    public static String md5(String string) {
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

    class  selfFunctionThread extends Thread{
        private Looper looper;
        public  void  run(){
            Looper.prepare();
            looper = Looper.myLooper();
            Looper.loop();
        }

    }





    public String post(final String method  , postStructList list , final Context context ){

        final  String info = handleInfo(list);
//        String serverAddress = "location.unix8.net";
        String serverAddress = "192.168.1.100";

        final String path = "http://" +
                serverAddress +
//                "/public/index/" +
                "/test/phpServer/public/index/" +
                method +
                "";
        new Thread() {
            public void run() {
//                while (true)
                {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        return "test";
    }

    public String post(final  String method , postStructList list){

        return post(method , list  , selfFunctionContext);
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
        new  AlertDialog.Builder(context)
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


}
