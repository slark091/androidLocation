package com.example.test.menu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.tuo.customview.VerificationCodeView;

/**
 * Created by test on 18-6-1.
 */

public class loginActivity extends Activity implements View.OnClickListener {

    private  String tag = "loginActivity";
    private selfFunction func ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.index);
        initView();


        String  username = func.sharedPreferences.getString("phone" , "");
        String code = func.sharedPreferences.getString("code", "");

        userNmaeInput.setText(username);
        codeInput.setText(code);

    }




    //edited by slark091
    //
    //
    //
    //
    private selfFunction sPost;

    private TextView  mTips ;

    private ImageButton mBtnLogin;

    public EditText userNmaeInput , codeInput;

    private ImageView loginTitleBack , loginTitleSignUp ;

    private View progress;

    private RelativeLayout loginPage;

    private View mInputLayout;

    private float mWidth, mHeight;
    private LinearLayout mName;
    private RelativeLayout mPsw;
    private CheckBox eyeSelector;




    public static final String regexMobile = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_PASSWORD = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]){6,20}$";
    private void initView() {

        func = new selfFunction(loginActivity.this) ;

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
                if(code.matches(REGEX_PASSWORD)){
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
        set.setDuration(200);
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
                mInputLayout.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);


                postStructList listData = new postStructList();
                listData.add("phone" , userNmaeInput.getText());
                listData.add("code" , codeInput.getText());

//                handleInfo(listData);


                func.post( "index/login" , (listData));




                break;
            }
            case R.id.login_title_back:{
                finish();
                break;
            }
            case R.id.login_title_sign_up:{
                AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.dialog);




                try{
                    builder.setView(initSignUp(this));

                }catch (Throwable e){

                    func.infoPush(e);
                }


                signDialog = builder.create();

                signDialog.setCanceledOnTouchOutside(false);
                signDialog.show();
                signDialog.getWindow();


                break;
            }

        }


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

    public void recovery() {

            progress.setVisibility(View.INVISIBLE);
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

    public LinearLayout sign_up1 ;
    public LinearLayout sign_up2 ;
    public LinearLayout sign_up3 ;
    public VerificationCodeView verificationCodeView;
    public AlertDialog signDialog;

    private View initSignUp(Context context){

        View view = View.inflate(context , R.layout.sign_up , null);

        final EditText phone = (EditText) view.findViewById(R.id.userNameInput1);
        final EditText code = (EditText) view.findViewById(R.id.userNameInput2);
        final ImageButton imageButton1 = (ImageButton) view.findViewById(R.id.main_btn_login1);
        final ImageButton imageButton3 = (ImageButton) view.findViewById(R.id.main_btn_login2);





        verificationCodeView = (VerificationCodeView) view.findViewById(R.id.icv);
        sign_up1 = (LinearLayout) view.findViewById(R.id.sign_up1);
        sign_up2 = (LinearLayout) view.findViewById(R.id.sign_up2);
        sign_up3 = (LinearLayout) view.findViewById(R.id.sign_up3);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String judge = String.valueOf(s);
                if (judge.matches(regexMobile)) {
                    imageButton1.setVisibility(View.VISIBLE);
                } else {
                    imageButton1.setVisibility(View.INVISIBLE);
                }
            }
        });

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = String.valueOf(s);
                if(code.matches(REGEX_PASSWORD)){
                        imageButton3.setVisibility(View.VISIBLE);
                    }else{
                        imageButton3.setVisibility(View.INVISIBLE);
                    }

            }
        });

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func.loading();
                postStructList listData = new postStructList();
                listData.add("phone" , phone.getText().toString());
                func.post("index/getMsg" , listData );

            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func.loading();
                postStructList listData = new postStructList();
                listData.add("phone" , phone.getText().toString());
                listData.add("code" , code.getText().toString());
                func.post("index/signUp" , listData );
            }
        });

        verificationCodeView.setInputCompleteListener(new VerificationCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                String temp = verificationCodeView.getInputContent();
                if(temp.length() == verificationCodeView.getEtNumber()){
                    func.loading();






                    postStructList listData = new postStructList();
                    listData.add("phone" , phone.getText().toString());
                    listData.add("verifyCode" , temp);

                    func.post("index/sign" , listData);



                }

            }

            @Override
            public void deleteContent() {

            }
        });









        return view;

    }


}
