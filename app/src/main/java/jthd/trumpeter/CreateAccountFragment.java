package jthd.trumpeter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {

    private EditText mEmailEditText;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mCreateAccountButton;

    private String mEmail;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        mEmailEditText = (EditText) view.findViewById(R.id.emailEditText);
        mUsernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
        mPasswordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        mConfirmPasswordEditText = (EditText) view.findViewById(R.id.confirmPasswordEditText);
        mCreateAccountButton = (Button) view.findViewById(R.id.createAccountButton);

        return view;
    }


    @Override
    public void onResume(){
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
                                    //LoginActivity.this.startActivity(feedIntent);
                                } else {
                                    // this should never happen, since a user was just created successfully with this information
                                }
                            }
                        });
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        // entry already taken?
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
