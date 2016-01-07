package jthd.trumpeter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Custom View class that manages the layout for each Trumpet, or item in the FeedActivity ListView.
 */

public class TrumpetView extends RelativeLayout {

    private ParseUser mUser;

    private TextView mUsernameTextView;
    private TextView mTrumpetTextView;
    private TextView mRetrumpetCountTextView;
    private TextView mLikeCountTextView;
    private TextView mRetrumpetTextView;
    private ImageView mProfilePictureImageView;
    private ImageButton mReplyButton;
    private ImageButton mRetrumpetButton;
    private ImageButton mLikeButton;



    /** Inherited constructor. */
    public TrumpetView(Context context) {
        super(context);
    }

    /** Inherited constructor. */
    public TrumpetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Inherited constructor. */
    public TrumpetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Loads views in trumpetView.
     */
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        mUsernameTextView = (TextView) findViewById(R.id.usernameTextView);
        mTrumpetTextView = (TextView) findViewById(R.id.trumpetTextView);
        mRetrumpetCountTextView = (TextView) findViewById(R.id.retrumpetCountTextView);
        mLikeCountTextView = (TextView) findViewById(R.id.likeCountTextView);
        mRetrumpetTextView = (TextView) findViewById(R.id.retrumpetTextView);
        mProfilePictureImageView = (ImageView) findViewById(R.id.profilePictureImageView);
        mReplyButton = (ImageButton) findViewById(R.id.replyButton);
        mRetrumpetButton = (ImageButton) findViewById(R.id.retrumpetButton);
        mLikeButton = (ImageButton) findViewById(R.id.likeButton);
    }

    /**
     * Retrieves data from provided Trumpet ParseObject and loads it into trumpetView's Views. Displays or hides retrumpetTextView based on retrumpet status.
     * @param trumpet, the Trumpet ParseObject that contains all necessary information for a Trumpet to be displayed.
     */
    public void showTrumpet(ParseObject trumpet){
        mUser = (ParseUser)trumpet.get("user");
        String username = mUser.getUsername();
        String text = (String)trumpet.get("text");
        boolean retrumpet = (boolean)trumpet.get("retrumpet");
        int retrumpets = (int)trumpet.get("retrumpets");
        int likes = (int)trumpet.get("likes");
        // If this Trumpet is a Retrumpet, display relevant Retrumpet information. Otherwise, hide the Retrumpet TextView
        if (retrumpet){
            String retrumpeter = (String)trumpet.get("retrumpeter");
            mRetrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            mRetrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture();
        mUsernameTextView.setText(username);
        mTrumpetTextView.setText(text);
        mRetrumpetCountTextView.setText(retrumpets);
        mLikeCountTextView.setText(likes);



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
                    mProfilePictureImageView.setImageBitmap(bmp);
                }
            });
        } else {
            mProfilePictureImageView.setImageResource(R.drawable.default_profile_picture);
        }
    }

}
