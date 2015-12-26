package jthd.trumpeter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
            ParseUser user = new ParseUser();
            user.setEmail(mEmail);
            user.setUsername(mUsername);
            user.setPassword(mPassword);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // log in automatically and travel to feed
                        ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // switch to Feed activity with logged in user info (ParseUser param?)
                                    //Intent feedIntent = new Intent(LoginActivity.this, FeedActivity.class);
                                    //feedIntent.putExtra("user", user);
                                    //getActivity().startActivity(feedIntent);
                                } else {
                                    // this should never happen, since a user was just created successfully with this information
                                }
                            }
                        });
                    } else {
                        // Email address or username are already taken
                        View focusView = null;
                        if (e.getCode() == ParseException.USERNAME_TAKEN){
                            mUsernameEditText.setError("Username is taken!");
                            focusView = mUsernameEditText;
                        } else if (e.getCode() == ParseException.EMAIL_TAKEN){
                            mEmailEditText.setError("Email is taken!");
                            focusView = mEmailEditText;
                        }
                    }
                }
            });
        }

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

}
