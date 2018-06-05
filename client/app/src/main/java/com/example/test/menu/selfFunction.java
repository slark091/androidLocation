package com.example.test.menu;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;




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

    public String post(final String  info){
        final String path = "http://192.168.1.100/test/phpServer/public/index/index/test";
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
                        String pathIn = path + info;
                        URL url = new URL(pathIn);
                        HttpURLConnection conn = (HttpURLConnection) url
                                .openConnection();
                        conn.setRequestMethod("POST");
                        conn.setReadTimeout(5000);
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        String data = "postTestData";
                        conn.setRequestProperty("Content-Length",String.valueOf(data.length()));
                        conn.setDoOutput(true);
                        conn.getOutputStream().write(data.getBytes());
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            InputStream is = conn.getInputStream();
                            Scanner temp = new Scanner(is).useDelimiter("\\A");
                            String result = temp.hasNext() ? temp.next() : "";
                            Log.d("post sucess" , result);
//                        String result = StreamTools.ReadStream(is);
//                        Message msg = Message.obtain();
//                        msg.what = SUCCESS;
//                        msg.obj = result;
//                        handler.sendMessage(msg);
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

    public String handleInfo( List<postStruct> infoArray){
        String info = "";

        for(int i = 0 ; i < infoArray.size() ; i++){
            if(info.equals("")){
                info += "?" + infoArray.get(i).name + "=" + infoArray.get(i).value;
            }else{
                info += "&" + infoArray.get(i).name + "=" + infoArray.get(i).value;

            }

        }
        return info;


    }

    public void testLog() throws InterruptedException {
        while (true){
            Thread.sleep(3000);
            Log.d("testLogPrint" , "TTTTTTT");

        }


    }
    public boolean setTextView(String string , R r){



        return true;
    }

}
