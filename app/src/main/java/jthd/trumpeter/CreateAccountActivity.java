package jthd.trumpeter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity{

    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mCreateAccountButton;

    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mUsernameEditText = (EditText) findViewById(R.id.usernameEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        mCreateAccountButton = (Button) findViewById(R.id.createAccountButton);

    }


    @Override
    public void onResume(){
        super.onResume();
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();

            }
        });
    }

    /**
     * Creates a ParseUser account with the given information. If success, log in user and launch FeedActivity. If failure, display information
     * relevant to that failure.
     */
    private void createAccount(){
        mEmail = mEmailEditText.getText().toString();
        mUsername = mUsernameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
        mConfirmPassword = mConfirmPasswordEditText.getText().toString();
        View focusView = checkInputs();
        if (focusView != null) {
            // There was an input error; don't attempt to create account, and focus view that is source of error
            focusView.requestFocus();
        } else {
            // Log out potential current user to prevent invalid session token issues (error 209)
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();
            ParseUser user = new ParseUser();
            user.setUsername(mUsername);
            user.setPassword(mPassword);
            user.setEmail(mEmail);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Log in automatically and travel to feed
                        ParseUser.logInInBackground(mUsername, mPassword, new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    Log.d("CreateAccountActivity", "Login success");
                                    toFeed();
                                } else {
                                    // this should never happen, since a user was just created successfully with this information
                                }
                            }
                        });
                    } else {
                        // Email address or username are already taken
                        createAccountFailure(e);

                    }
                }
            });
        }

    }

    /**
     * Checks the EditText inputs for email, username, password, and confirm password for validity. If a field is invalid, displays error message and
     * returns appropriate View for focusing.
     * @return Returns the View that caused the input error.
     */
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
        } else if (mPassword.length() < 6){
            mPasswordEditText.setError(getString(R.string.errorPasswordTooShortString));
            focusView = mPasswordEditText;
        }

        //check confirm password for valid input
        if (TextUtils.isEmpty(mConfirmPassword)) {
            mConfirmPasswordEditText.setError(getString(R.string.errorFieldRequiredString));
            focusView = mConfirmPasswordEditText;
        } else if (mPassword != null && !mConfirmPassword.equals(mPassword)){
            mConfirmPasswordEditText.setError(getString(R.string.errorPasswordMismatchString));
            focusView = mConfirmPasswordEditText;
        }
        return focusView;

    }

    /**
     * If ParseUser creation fails, use associated ParseException to determine why and display this information to the user.
     * @param e ParseException associated with account creation failure.
     */
    private void createAccountFailure(ParseException e){
        if (e.getCode() == ParseException.USERNAME_TAKEN){
            mUsernameEditText.setError("Username is taken!");
            mUsernameEditText.requestFocus();
        } else if (e.getCode() == ParseException.EMAIL_TAKEN){
            mEmailEditText.setError("Email is taken!");
            mEmailEditText.requestFocus();
        } else {
            mEmailEditText.setError("Unknown error occurred!");
            mEmailEditText.requestFocus();
            Log.d("CreateAccountActivity", Integer.toString(e.getCode()));
        }
    }

    /**
     * Launches FeedActivity.
     */
    private void toFeed(){
        Intent feedIntent = new Intent(CreateAccountActivity.this, FeedActivity.class);
        CreateAccountActivity.this.startActivity(feedIntent);
    }




}
