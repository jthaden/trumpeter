package jthd.trumpeter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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
            toFeed();
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
            // Retrieve username of account with provided email for login
            if (mEmail.contains("@")) {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", mEmail);
                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        if (object != null) {
                            String username = object.getUsername();
                            ParseUser.logInInBackground(username, mPassword, new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        Log.d("LoginActivity", "Login success");
                                        toFeed();
                                    } else {
                                        // Login failed due to validation errors. Don't be specific about why.
                                        loginFailure(e);
                                    }
                                }
                            });
                        } else {
                            // Login failed because account doesn't exist (email has no matching username).
                            loginFailure(e);
                            Log.d("LoginActivity", "no username found");
                        }
                    }
                });

            } else {
                // Username has been provided, login with username
                ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.d("LoginActivity", "Login success");
                            toFeed();
                        } else {
                            // Login failed due to validation errors. Don't be specific about why.
                            loginFailure(e);
                        }
                    }
                });
            }
        }

        }

    private View checkInputs(){
        View focusView = null;

        // check email/username for valid input
        if (TextUtils.isEmpty(mEmail)){
            mEmailEditText.setError(getString(R.string.errorFieldRequiredString));
            focusView = mEmailEditText;
        }

        // check password for valid input
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordEditText.setError(getString(R.string.errorFieldRequiredString));
            focusView = mPasswordEditText;
        }
        return focusView;

    }

    private void loginFailure(ParseException e){
        mEmailEditText.setError("Login failed.");
        mEmailEditText.requestFocus();
        mEmailEditText.setText("");
        mPasswordEditText.setText("");
        Log.d("LoginActivity", Integer.toString(e.getCode()));
    }

    private void toFeed(){
        // switch to Feed activity
        Intent feedIntent = new Intent(LoginActivity.this, FeedActivity.class);
        LoginActivity.this.startActivity(feedIntent);
    }



    private void createAccount(){
        Intent createAccIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        LoginActivity.this.startActivity(createAccIntent);
    }



}
