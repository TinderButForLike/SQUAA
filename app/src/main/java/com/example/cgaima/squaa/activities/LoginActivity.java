package com.example.cgaima.squaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cgaima.squaa.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button SignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btLogin);
        SignupBtn = findViewById(R.id.btSignUp);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
            }
        });

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // to avoid ParseUser Exception: invalid session token
                if (ParseUser.getCurrentUser() != null ) {
                    ParseUser.logOut();
                }
                final Intent j = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(j);
            }
        });

        // persist user
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        Log.d("LoginActivity", "Current user is " + currentUser);
    }

    private void login(String username, String password) {
        // to avoid ParseUser Exception: invalid session token
        if (ParseUser.getCurrentUser() != null ) {
            ParseUser.logOut();
        }
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");
                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("LoginActivity", "Login failure");
                    Toast.makeText(getApplicationContext(), "Login failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


