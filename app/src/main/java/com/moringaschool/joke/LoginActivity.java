package com.moringaschool.joke;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moringaschool.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.registerTextView)
    TextView mRegisterTextView;

    @BindView(R.id.passwordLoginButton)
    Button mPasswordLoginButton;
    @BindView(R.id.emailEditText)
    EditText mEmailEditText;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;
    @BindView(R.id.firebaseProgressBar)
            //for progress bar
    ProgressBar mSignInProgressBar;
    @BindView(R.id.loadingTextView) TextView mLoadingSignUp;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseAuth mAuth;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this); //define mshared preference
        mEditor = mSharedPreferences.edit();//define mshared preference


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, JokeList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mRegisterTextView.setOnClickListener(this);
        mPasswordLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mRegisterTextView) {
            Intent intent = new Intent(LoginActivity.this, SignUp.class);
            startActivity(intent);
            finish();
        }
        if (view == mPasswordLoginButton) {
            String email = mEmailEditText.getText().toString().trim();//add mshared preference
            addToSharedPreferences(email);//add mshared preference
            loginWithPassword();
            showProgressBar();
        }

    }

    private void showProgressBar() {
        mSignInProgressBar.setVisibility(View.VISIBLE);
        mLoadingSignUp.setVisibility(View.VISIBLE);
        //mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mLoadingSignUp.setText("Log in you in");
    }
    private void hideProgressBar() {
        mSignInProgressBar.setVisibility(View.GONE);
        mLoadingSignUp.setVisibility(View.GONE);
    }

    private void loginWithPassword() {
        String email = mEmailEditText.getText().toString().trim();//add mshared preference
        addToSharedPreferences(email);//add mshared preference
        String password = mPasswordEditText.getText().toString().trim();
        if (email.equals("")) {
            mEmailEditText.setError("Please enter your email");
            return;
        }
        if (password.equals("")) {
            mPasswordEditText.setError("Password cannot be blank");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressBar();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
//constructor for mshared preference
    private void addToSharedPreferences(String email) {
        mEditor.putString(Constants.PREFERENCES_EMAIL_KEY, email).apply();
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }


    }
}