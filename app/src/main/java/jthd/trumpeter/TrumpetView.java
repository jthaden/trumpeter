package jthd.trumpeter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
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
import com.squareup.picasso.Picasso;


/**
 * Custom View class that manages the layout for each Trumpet, or item in the FeedActivity ListView. Performs strictly the "View" function
 * while the Managers (SubmitTrumpetManager and UpdateTrumpetManager) handle the Model.
 */

public class TrumpetView extends RelativeLayout {

    private Context context;

    private ParseObject trumpet;
    private ParseUser trumpetUser;
    private String username;
    private String text;
    private String retrumpeter;
    private Boolean retrumpet;
    private int retrumpets;
    private int likes;

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
        this.context = context;
    }

    /** Inherited constructor. */
    public TrumpetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /** Inherited constructor. */
    public TrumpetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
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
     * Sets listeners for buttons - TODO Hope this works; it SHOULD. If it doesn't, WILL work with listener in getView(); would need to use viewHolder model instead
     * @param showTrumpet, the Trumpet ParseObject that contains all necessary information for a Trumpet to be displayed.
     */
    public void showTrumpet(ParseObject showTrumpet) {
        trumpet = showTrumpet;
        trumpetUser = (ParseUser) trumpet.get("user");
        username = trumpetUser.getUsername();
        text = (String) trumpet.get("text");
        retrumpet = (boolean) trumpet.get("retrumpet");
        retrumpets = trumpet.getInt("retrumpets");
        likes = trumpet.getInt("likes");
        // If this Trumpet is a Retrumpet, display relevant Retrumpet information. Otherwise, hide the Retrumpet TextView
        if (retrumpet) {
            mRetrumpetTextView.setVisibility(View.VISIBLE);
            retrumpeter = (String) trumpet.get("retrumpeter");
            mRetrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            mRetrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture();
        mUsernameTextView.setText(username);
        mTrumpetTextView.setText(text);
        mRetrumpetCountTextView.setText(Integer.toString(retrumpets));
        mLikeCountTextView.setText(Integer.toString(likes));
        mReplyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRetrumpetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetrumpetCountTextView.setText(Integer.toString(retrumpets + 1));
                // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
            }
        });
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLikeCountTextView.setText(Integer.toString(likes + 1));
                UpdateTrumpetManager.updateLikeCount(trumpet.getInt("trumpetID"));
            }
        });
    }


    public void showDetailedTrumpet(ParseObject showTrumpet){
        trumpet = showTrumpet;
        trumpetUser = (ParseUser)trumpet.get("user");
        username = trumpetUser.getUsername();
        text = (String)trumpet.get("text");
        retrumpet = (boolean)trumpet.get("retrumpet");
        retrumpets = trumpet.getInt("retrumpets");
        likes = trumpet.getInt("likes");
        // If this Trumpet is a Retrumpet, display relevant Retrumpet information. Otherwise, hide the Retrumpet TextView
        if (retrumpet){
            mRetrumpetTextView.setVisibility(View.VISIBLE);
            retrumpeter = (String)trumpet.get("retrumpeter");
            mRetrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            mRetrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture();
        mUsernameTextView.setText(username);
        mTrumpetTextView.setText(text);
        mRetrumpetCountTextView.setText(Integer.toString(retrumpets) + " Retrumpets");
        mLikeCountTextView.setText(Integer.toString(likes) + " Likes");
        mReplyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRetrumpetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetrumpetCountTextView.setText(Integer.toString(retrumpets + 1) + " Retrumpets");
                // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
            }
        });
        mLikeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLikeCountTextView.setText(Integer.toString(likes + 1) + " Likes");
                UpdateTrumpetManager.updateLikeCount(trumpet.getInt("trumpetID"));
            }
        });

    }

    /**
     * Sets the user's profile picture. If no profile picture has been uploaded, use default.
     */
    private void setProfilePicture(){
        // if profilePicture is not null (a profile picture has been uploaded), use it. Otherwise, use default
        if (trumpetUser.getParseFile("profilePicture") != null){
            // Asynchronously optimizes and loads the user's profile picture. Loads the default profile picture as a placeholder.
            ParseFile profilePicture = trumpetUser.getParseFile("profilePicture");
            Picasso.with(App.getAppContext()).load(profilePicture.getUrl()).placeholder(R.drawable.default_profile_picture).resize(180, 180).into(mProfilePictureImageView);
        } else {
            // Asynchronously optimizes and loads the default profile picture.
            // TODO Acceptable way to get context here? Is there an easier way (it is passed in). Does it matter?
            Picasso.with(App.getAppContext()).load(R.drawable.default_profile_picture).resize(180, 180).centerInside().into(mProfilePictureImageView);

        }
    }




}
