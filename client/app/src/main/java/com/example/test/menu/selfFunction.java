package com.example.test.menu;

import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;




/**
 * Created by test on 18-6-1.
 */








public class selfFunction {

    public void load(){
        final String path = "http://192.168.1.100/test/phpServer/public/index/index/test";
        new Thread() {
            public void run() {
                while (true){
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
