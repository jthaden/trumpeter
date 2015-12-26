package jthd.trumpeter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    protected void onResume(){
        super.onResume();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();

            }
        });
    }



    private void logIn(){
        mEmail = mEmailEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        View focusView = checkInputs();
        if (focusView != null) {
            // There was an input error; don't attempt to log in, and focus view that is source of error
            focusView.requestFocus();
        } else {
            ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        // switch to Feed activity with logged in user info (ParseUser param?)
                        //Intent feedIntent = new Intent(LoginActivity.this, FeedActivity.class);
                        //feedIntent.putExtra("user", user);
                        //LoginActivity.this.startActivity(feedIntent);
                    } else {
                        // Login failed due to validation errors. Don't be specific about why.
                        mEmailEditText.setError("Login failed.");
                        mEmailEditText.requestFocus();
                        mEmailEditText.setText("");
                        mPasswordEditText.setText("");
                    }
                }
            });
        }


    }

    private void createAccount(){
        Intent createAccIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        LoginActivity.this.startActivity(createAccIntent);
    }

    private View checkInputs(){
        View focusView = null;

        // check email for valid input
        if (TextUtils.isEmpty(mEmail)){
            mEmailEditText.setError(getString(R.string.errorFieldRequiredString));
            focusView = mEmailEditText;
        } else if (!mEmail.contains("@")){
            mEmailEditText.setError(getString(R.string.errorInvalidEmailString));
            focusView = mEmailEditText;
        }

        // check password for valid input
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(getString(R.string.errorFieldRequiredString));
            focusView = mPasswordEditText;
        }
        return focusView;

    }

}
