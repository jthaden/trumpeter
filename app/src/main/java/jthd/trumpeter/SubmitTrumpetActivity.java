package jthd.trumpeter;

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

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class SubmitTrumpetActivity extends AppCompatActivity {

    private final int MAX_CHAR = 160;

    private ParseUser user;

    private Button submitTrumpetButton;
    private EditText trumpetEditText;
    private ImageButton backButton;
    private ImageView profilePictureImageView;
    private TextView usernameTextView;
    private TextView charCountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_trumpet);
        profilePictureImageView = (ImageView) findViewById(R.id.profilePictureImageView);
        submitTrumpetButton = (Button) findViewById(R.id.submitTrumpetButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        charCountTextView = (TextView) findViewById(R.id.charCountTextView);
        trumpetEditText = (EditText) findViewById(R.id.trumpetEditText);
    }

    @Override
    protected void onResume(){
        super.onResume();
        user = ParseUser.getCurrentUser();
        usernameTextView.setText(user.getUsername());
        setProfilePicture();
        trumpetEditText.addTextChangedListener(mTextEditorWatcher);
        submitTrumpetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // submit Trumpet and process
                SubmitTrumpetManager.submitNewTrumpet(trumpetEditText.getText().toString(), user);
            }
        });
    }



    /**
     * Sets the user's profile picture at the top of the layout. If no profile picture uploaded, use default.
     */
    private void setProfilePicture(){
        // if profilePicture is not null (a profile picture has been uploaded), use it. Otherwise, use default
        if (user.getParseFile("profilePicture") != null){
            // Asynchronously optimizes and loads the user's profile picture. Loads the default profile picture as a placeholder.
            ParseFile profilePicture = user.getParseFile("profilePicture");
            Picasso.with(App.getAppContext()).load(profilePicture.getUrl()).placeholder(R.drawable.default_profile_picture).resize(180, 180).into(profilePictureImageView);
        } else {
            // Asynchronously optimizes and loads the default profile picture.
            // TODO Acceptable way to get context here? Is there an easier way (it is passed in). Does it matter?
            Picasso.with(App.getAppContext()).load(R.drawable.default_profile_picture).resize(180, 180).into(profilePictureImageView);

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
            charCountTextView.setText(String.valueOf(MAX_CHAR - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

}
