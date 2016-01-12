package jthd.trumpeter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class SubmitTrumpetActivity extends AppCompatActivity {

    private final int MAX_CHAR = 160;

    private ParseUser mUser;

    private Button mSubmitTrumpetButton;
    private EditText mTrumpetEditText;
    private ImageButton mBackButton;
    private ImageView mProfilePictureImageView;
    private TextView mUsernameTextView;
    private TextView mCharCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trumpet);
        mProfilePictureImageView = (ImageView) findViewById(R.id.profilePictureImageView);
        mSubmitTrumpetButton = (Button) findViewById(R.id.submitTrumpetButton);
        mBackButton = (ImageButton) findViewById(R.id.backButton);
        mUsernameTextView = (TextView) findViewById(R.id.usernameTextView);
        mCharCountTextView = (TextView) findViewById(R.id.charCountTextView);
        mTrumpetEditText = (EditText) findViewById(R.id.trumpetEditText);
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
                SubmitTrumpetManager.submitNewTrumpet(mTrumpetEditText.getText().toString(), mUser);
            }
        });
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
                    // TODO Optimizing and loading of non-default profile picture is currently done on UI thread; need to do as is done with resource
                    Bitmap optimizedImage = ImageManager.decodeSampledBitmapFromByteArray(data, 60, 60);
                    mProfilePictureImageView.setImageBitmap(optimizedImage);
                }
            });
        } else {
            // Asynchronously optimizes and loads the default profile picture. Automatically performs commented code off of the UI thread.
            // Provide desired pixel density for appropriate optimization.
            ImageManager.loadBitmap(R.drawable.default_profile_picture, mProfilePictureImageView, 60, 60);
            //Bitmap optimizedDefaultImage = ImageManager.decodeSampledBitmapFromResource(getResources(), R.drawable.default_profile_picture, 60, 60);
            //mProfilePictureImageView.setImageBitmap(optimizedDefaultImage);
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
