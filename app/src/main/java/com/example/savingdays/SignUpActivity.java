package com.example.savingdays;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        //findViewById(R.id.gotoLoginButton).setOnClickListener(OnClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    Log.d("클릭", "onClick: ");
                    break;
                case R.id.gotoLoginButton:
                    //myStartActivity(LoginActivity.class);
                    break;
            }
        }
    };
}