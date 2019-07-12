package com.example.parstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public String username;
    public String password;
    public EditText etUser;
    public EditText etPassword;
    public EditText etHandle;
    public EditText etEmail;
    public Button btnSignup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // getting any preliminary username & password info entered
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        // getting item references
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        etHandle = findViewById(R.id.etHandle);
        etEmail = findViewById(R.id.etEmail);
        btnSignup = findViewById(R.id.btnSignup);

        etUser.setText(username);
        etPassword.setText(password);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create the new ParseUser
                ParseUser user = new ParseUser();
                // set core properties
                user.setUsername(etUser.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.put("Handle", etHandle.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(SignupActivity.this, username + " is successfully signed up!", Toast.LENGTH_LONG).show();
                            Log.d("SignupActivity", "successful signup");
                            // after signing up, take user to the home page
                        } else {
                            Log.e("SignupActivity", "Sign up failure");
                            Log.d("Stack Trace", Log.getStackTraceString(e));
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
            }
        });
    }
}
