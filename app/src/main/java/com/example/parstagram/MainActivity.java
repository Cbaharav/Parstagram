package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login Successful");
                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    // after logging in, take user to the home page
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void signup(String username, String password) {
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);

//        // create the new ParseUser
//        ParseUser user = new ParseUser();
//        // set core properties
//        user.setUsername(username);
//        user.setPassword(password);
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Toast.makeText(MainActivity.this, usernameInput.getText().toString() + " is successfully signed up!", Toast.LENGTH_LONG).show();
//                    // after signing up, take user to the home page
//                    final Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Log.e("SignupActivity", "Sign up failure");
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
