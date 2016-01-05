package jthd.trumpeter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmitTrumpetActivity extends AppCompatActivity {

    private final int MAX_CHAR = 160;

    private ParseUser mUser;

    private Button mSubmitTrumpetButton;
    private EditText mTrumpetEditText;
    private ImageButton mBackButton;
    private ImageView mProfilePictureView;
    private TextView mUsernameTextView;
    private TextView mCharCountTextView;

    private String mTrumpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trumpet);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mUser = ParseUser.getCurrentUser();
        mUsernameTextView.setText(mUser.getUsername());
        setProfilePicture();
        mTrumpetEditText.addTextChangedListener(mTextEditorWatcher);
        mSubmitTrumpetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // submit Trumpet and process
                submitTrumpet();
            }
        });
    }

    /**
     * Creates a ParseObject of class "Trumpet" with the user-provided information and submitting ParseUser. Submission date automatically saved in
     * "createdAt" field.
     */
    private void submitTrumpet(){
        mTrumpet = mTrumpetEditText.getText().toString();
        ParseObject trumpet = new ParseObject("Trumpet");
        trumpet.put("text", mTrumpet);
        trumpet.put("user", mUser);
        trumpet.put("retrumpet", false);
        trumpet.put("retrumpets", 0);
        trumpet.put("likes", 0);
        trumpet.saveInBackground();
    }

    /**
     * Sets the user's profile picture at the top of the layout. If no profile picture uploaded, use default.
     */
    private void setProfilePicture(){
        // if profilePicture is not null (a profile picture has been uploaded), use it. Otherwise, use default
        if (mUser.get("profilePicture") != null){
            ParseFile profilePicture = (ParseFile) mUser.get("profilePicture");
            profilePicture.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mProfilePictureView.setImageBitmap(bmp);
                }
            });
        } else {
            // mProfilePictureView.setImageResource(R.drawable.default_profile_picture); now defaults to default profile picture
        }
    }

    /**
     * Watches the trumpetEditText and updates the charCountTextView with the remaining number of allowed characters whenever text is entered
     */
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // sets TextView to 160 - current length of EditText
            mCharCountTextView.setText(String.valueOf(MAX_CHAR - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

}
