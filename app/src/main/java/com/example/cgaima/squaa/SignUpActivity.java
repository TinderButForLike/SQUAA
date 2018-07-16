package com.example.cgaima.squaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText newUser;
    private EditText newPass;
    private EditText newEmail;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        newUser = findViewById(R.id.etNewUser);
        newPass = findViewById(R.id.etNewPass);
        newEmail= findViewById(R.id.etNewEmail);
        signupBtn = findViewById(R.id.btSignUp);


        signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // create a new parse user
                ParseUser user = new ParseUser();
                user.setUsername(newUser.getText().toString());
                user.setPassword(newPass.getText().toString());
                user.setEmail(newEmail.getText().toString());
                // invoke signup in background

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}

