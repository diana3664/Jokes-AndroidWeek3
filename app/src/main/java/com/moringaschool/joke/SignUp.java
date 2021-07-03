package com.moringaschool.joke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = SignUp.class.getSimpleName();
    @BindView(R.id.viewJokesButton) Button mViewJokesButton;
    @BindView(R.id.signEditText) EditText mSignEditText;
    @BindView(R.id.signEditText2) EditText mSignEditText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        mViewJokesButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v == mViewJokesButton) {
            if(mSignEditText.length()==0){
                mSignEditText.setError("Enter your name");
            }else if (mSignEditText2.length() == 0){
                mSignEditText2.setError("Enter email");
            }else {
                String name = mSignEditText.getText().toString();
                Log.d(TAG, name);
                Intent intent = new Intent(SignUp.this, JokeList.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        }

    }
}