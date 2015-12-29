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

    private void submitTrumpet(){
        mTrumpet = mTrumpetEditText.getText().toString();
        ParseObject trumpet = new ParseObject("Trumpet");
        trumpet.put("text", mTrumpet);
        trumpet.put("user", mUser);
        trumpet.saveInBackground();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit_trumpet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
