package com.example.test.menu;

/**
 * Created by test on 18-6-3.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by test on 18-6-3.
 */

public class signUpActivity extends Activity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            default:{
                Toast.makeText(signUpActivity.this , "asdf" , Toast.LENGTH_LONG).show();

                break;
            }
            case  R.id.main_btn_login:{
                Toast.makeText(this , "test" , Toast.LENGTH_SHORT).show();
                break;
            }


        }

    }
    final  String tag = "signUpActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initView();
    }


    private void initView(){




    }


}
