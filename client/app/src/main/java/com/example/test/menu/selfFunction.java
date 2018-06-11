package com.example.test.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

    private AlertDialog dialog;

    public Context selfFunctionContext ;


    public     SharedPreferences.Editor editor;
    public     SharedPreferences sharedPreferences;
    private Transition transition;



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
                Context context = (Context) msg.obj;

                final MainActivity mainActivity = (MainActivity) context;
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.imageButton.setVisibility(View.GONE);
                    }
                });
                dialog.cancel();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view1 = View.inflate(context , R.layout.loading , null);

        builder.setView(view1);

        dialog = builder.create();




        dialog.show();


        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.alpha = 0.3f;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        dialog.getWindow().setAttributes(layoutParams);

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

}
