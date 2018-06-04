package com.example.test.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Created by test on 18-6-1.
 */

public class testActivity extends Activity implements View.OnClickListener {

    private  String tag = "testActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        initView();

    }




    //edited by slark091
    //
    //
    //
    //
    private selfFunction sPost;

    private TextView  mTips ;

    private ImageButton mBtnLogin;

    private EditText userNmaeInput , codeInput;

    private ImageView loginTitleBack , loginTitleSignUp ;

    private View progress;

    private RelativeLayout loginPage;

    private View mInputLayout;

    private float mWidth, mHeight;
    private LinearLayout mName;
    private RelativeLayout mPsw;
    private CheckBox eyeSelector;

    public static final String regexMobile = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_PASSWORD = "\"^(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9_]{6,20}$\"";

    private void initView() {


        mBtnLogin = (ImageButton) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (RelativeLayout) findViewById(R.id.input_layout_psw);
        mTips = (TextView) findViewById(R.id.loginTips);
        userNmaeInput = (EditText) findViewById(R.id.userNameInput);
        codeInput = (EditText) findViewById(R.id.codeInput);
        eyeSelector = (CheckBox) findViewById(R.id.eye_selector);
        loginTitleBack = (ImageView) findViewById(R.id.login_title_back);
        loginTitleSignUp = (ImageView) findViewById(R.id.login_title_sign_up);
        loginPage = (RelativeLayout) findViewById(R.id.login_page);


        loginTitleSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        loginTitleBack.setOnClickListener(this);
        loginPage.setOnClickListener(this);

        eyeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    codeInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else{
                    codeInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        final String[] recordUserNameInput = new String[1];
        recordUserNameInput[0] = "";
        userNmaeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                recordUserNameInput[0] = String.valueOf(s);

            }
        });
        codeInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    if(!recordUserNameInput[0].matches(regexMobile)){
                        mTips.setText("请正确填写电话号码");
                        mTips.setVisibility(View.VISIBLE);
                    }else{
                        mTips.setText("");
                        mTips.setVisibility(View.INVISIBLE);
                    }

                }


            }
        });

        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = String.valueOf(s);
                if(code.matches("^(?=.*?[a-z])(?=.*?[0-9])[a-zA-Z0-9_]{6,16}$")){
                    if(recordUserNameInput[0].matches(regexMobile)){
                        mBtnLogin.setVisibility(View.VISIBLE);
                    }else{
                        mBtnLogin.setVisibility(View.INVISIBLE);
                    }
                }else {
                    mBtnLogin.setVisibility(View.INVISIBLE);
                }
            }
        });





    }
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            default:{
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)  ;
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken() , 0);
//                inputMethodManager.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);

                break;
            }
            case R.id.main_btn_login:{
                // 计算出控件的高与宽



                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mTips.setVisibility(View.INVISIBLE);
                mBtnLogin.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);



                String tempValue = String.valueOf(codeInput.getText()) ;
                String tempName = String.valueOf(userNmaeInput.getText()) ;
                postStruct tempData ;

                List<postStruct> listData = new ArrayList<postStruct>();

                tempData = new postStruct();
                tempData.value = tempName;
                tempData.name = "phone";
                listData.add(tempData);
                tempData = new postStruct();
                tempData.value = md5(tempValue);
                tempData.name = "code";
                listData.add(tempData);


//                handleInfo(listData);
                post(handleInfo(listData));


                break;
            }
            case R.id.login_title_back:{
                finish();
                break;
            }
            case R.id.login_title_sign_up:{
                Intent intent = new Intent( testActivity.this , signUpActivity.class);
//                recovery();
                startActivity(intent);
                break;
            }

        }


    }
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
                            String result = temp.hasNext() ? temp.next() : "";
                            Log.d("testActivity" , result);
                            switch (result){
                                case "noAccount":{

                                    break;
                                }
                                case "rightCode":{

                                    break;
                                }
                                case "wrongCode":{

                                    break;
                                }


                            }


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
                info +=  infoArray.get(i).name + "=" + infoArray.get(i).value;
            }else{
                info += "&" + infoArray.get(i).name + "=" + infoArray.get(i).value;

            }

        }
        return info;


    }

    public class JellyInterpolator extends LinearInterpolator {
        private float factor;

        public JellyInterpolator() {
            this.factor = 0.15f;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input)
                    * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }

    private void recovery() {
        progress.setVisibility(View.GONE);
        mInputLayout.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mPsw.setVisibility(View.VISIBLE);
        mTips.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mInputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        mInputLayout.setLayoutParams(params);


        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();

//        finish();
    }

    //edited by slark091
    //
    //
    //
    //


}
