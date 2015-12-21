package jthd.trumpeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mCreateAccountButton;
    //private Button mLoginWithFacebookButton;

    private String mEmail;
    private String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // if user login is cached, skip login
            //Intent feedIntent = new Intent(LoginActivity.this, FeedActivity.class);
            //feedIntent.putExtra("user", currentUser);
            //LoginActivity.this.startActivity(feedIntent);
        }


        setContentView(R.layout.activity_login);
        mEmailEditText = (EditText)findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText)findViewById(R.id.passwordEditText);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mCreateAccountButton = (Button)findViewById(R.id.createAccountButton);
        // login with facebook button here

    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }



    private void logIn(){
        // assuming EditTexts have been changed; TODO deal with empty/default EditTexts
        mEmail = mEmailEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // switch to Feed activity with logged in user info (ParseUser param?)
                    //Intent feedIntent = new Intent(LoginActivity.this, FeedActivity.class);
                    //feedIntent.putExtra("user", user);
                    //LoginActivity.this.startActivity(feedIntent);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });


    }

}
