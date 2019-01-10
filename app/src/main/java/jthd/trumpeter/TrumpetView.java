package jthd.trumpeter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.GetFileCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Custom View class that manages the layout for each Trumpet, or item in the FeedActivity ListView. Also manages the slightly modified "detailedTrumpetView"
 * layout in ViewTrumpetActivity. Performs strictly the "View" function while the Managers (SubmitTrumpetManager and UpdateTrumpetManager) handle the Model.
 */

public class TrumpetView extends RelativeLayout {

    private ParseObject trumpet;
    private ParseUser trumpetUser;
    private String username;
    private String text;
    private String retrumpeter;
    private Boolean retrumpet;
    private int retrumpets;
    private int likes;
    private int replies;

    private TextView usernameTextView;
    private TextView trumpetTextView;
    private TextView retrumpetCountTextView;
    private TextView likeCountTextView;
    private TextView replyCountTextView;
    private TextView retrumpetTextView;
    private TextView dateTextView;
    private ImageView profilePictureImageView;
    private ImageButton replyButton;
    private ImageButton retrumpetButton;
    private ImageButton likeButton;



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
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        trumpetTextView = (TextView) findViewById(R.id.trumpetTextView);
        retrumpetCountTextView = (TextView) findViewById(R.id.retrumpetCountTextView);
        likeCountTextView = (TextView) findViewById(R.id.likeCountTextView);
        retrumpetTextView = (TextView) findViewById(R.id.retrumpetTextView);
        replyCountTextView = (TextView) findViewById(R.id.replyCountTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        profilePictureImageView = (ImageView) findViewById(R.id.profilePictureImageView);
        replyButton = (ImageButton) findViewById(R.id.replyButton);
        retrumpetButton = (ImageButton) findViewById(R.id.retrumpetButton);
        likeButton = (ImageButton) findViewById(R.id.likeButton);
    }

    /**
     * Retrieves data from provided Trumpet ParseObject and loads it into trumpetView's Views. Displays or hides retrumpetTextView based on retrumpet status.
     * Sets listeners for buttons.
     * Reply button behavior: After replying to a regular ListView TrumpetView, load ViewTrumpetActivity for replied-to Trumpet.
     * @param showTrumpet, the Trumpet ParseObject that contains all necessary information for a Trumpet to be displayed.
     */
    public void showTrumpet(ParseObject showTrumpet) {
        trumpet = showTrumpet;
        trumpetUser = trumpet.getParseUser("user");
        /*
        try {
            trumpetUser =  trumpet.fetchIfNeeded().getParseUser("user");

        } catch (ParseException e){

        }
        */
        try {
            username = trumpetUser.fetchIfNeeded().getUsername();
        } catch (ParseException e){

        }
        //username = trumpetUser.getUsername();
        text = (String) trumpet.get("text");
        retrumpet = (boolean) trumpet.get("retrumpet");
        retrumpets = trumpet.getInt("retrumpets");
        likes = trumpet.getInt("likes");
        replies = trumpet.getInt("replies");
        // If this Trumpet is a Retrumpet, display relevant Retrumpet information. Otherwise, hide the Retrumpet TextView
        if (retrumpet) {
            retrumpetTextView.setVisibility(View.VISIBLE);
            retrumpeter = (String) trumpet.get("retrumpeter");
            retrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            retrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture(250, 250);
        usernameTextView.setText(username);
        trumpetTextView.setText(text);
        DateFormat df = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        dateTextView.setText(df.format(trumpet.getCreatedAt()));
        retrumpetCountTextView.setText(Integer.toString(retrumpets));
        likeCountTextView.setText(Integer.toString(likes));
        replyCountTextView.setText(Integer.toString(replies));
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Picasso.with(v.getContext()).load(R.drawable.reply_arrow_pressed).into(replyButton);
                toSubmitTrumpetActivityReply(false);
            }
        });
        retrumpetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Picasso.with(v.getContext()).load(R.drawable.retrumpet_pressed).into(retrumpetButton);
                retrumpetCountTextView.setText(Integer.toString(retrumpets + 1));
                // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
            }
        });
        likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Picasso.with(v.getContext()).load(R.drawable.like_pressed).into(likeButton);
                likeCountTextView.setText(Integer.toString(likes + 1));
                UpdateTrumpetManager.updateLikeCount(trumpet.getInt("trumpetID"));
            }
        });
    }

    /**
     * Retrieves data from showTrumpet and loads it into a more detailed version of the TrumpetView layout designed for the specific
     * Trumpet being viewed in ViewTrumpetActivity. Exact differences are visible in ViewTrumpetActivity layout file.
     * Reply button behavior: After replying to a detailedTrumpet (Trumpet already loaded in ViewTrumpetActivity), finish() and refresh reply data.
     * @param showTrumpet, the Trumpet ParseObject that contains all necessary information for a Trumpet to be displayed.
     */
    public void showDetailedTrumpet(ParseObject showTrumpet) {
        trumpet = showTrumpet;
        trumpetUser = (ParseUser)trumpet.get("user");
        try {
            username = trumpetUser.fetchIfNeeded().getUsername();
            text = (String) trumpet.get("text");
            retrumpet = (boolean) trumpet.get("retrumpet");
            retrumpets = trumpet.getInt("retrumpets");
            likes = trumpet.getInt("likes");
            replies = trumpet.getInt("replies");
            // If this Trumpet is a Retrumpet, display relevant Retrumpet information. Otherwise, hide the Retrumpet TextView
            if (retrumpet) {
                retrumpetTextView.setVisibility(View.VISIBLE);
                retrumpeter = (String) trumpet.get("retrumpeter");
                retrumpetTextView.setText("Retrumpeted by " + retrumpeter);
            } else {
                retrumpetTextView.setVisibility(View.GONE);
            }
            setProfilePicture(400, 400);
            usernameTextView.setText(username); // TODO @ here?
            trumpetTextView.setText(text);
            DateFormat df = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
            dateTextView.setText(df.format(trumpet.getCreatedAt()));
            retrumpetCountTextView.setText(Integer.toString(retrumpets) + " Retrumpets");
            likeCountTextView.setText(Integer.toString(likes) + " Likes");
            replyCountTextView.setText(Integer.toString(replies) + " Replies");
            replyButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Picasso.with(v.getContext()).load(R.drawable.reply_arrow_pressed).into(replyButton);
                    toSubmitTrumpetActivityReply(true);
                }
            });
            retrumpetButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    retrumpetCountTextView.setText(Integer.toString(retrumpets + 1) + " Retrumpets");
                    //Picasso.with(v.getContext()).load(R.drawable.retrumpet_pressed).into(retrumpetButton);
                    // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                    SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
                }
            });
            likeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeCountTextView.setText(Integer.toString(likes + 1) + " Likes");
                    //Picasso.with(v.getContext()).load(R.drawable.like_pressed).into(likeButton);
                    UpdateTrumpetManager.updateLikeCount(trumpet.getInt("trumpetID"));
                }
            });
        } catch (ParseException e) {
            Log.d("TrumpetView", "Failed to fetch trumpet's parse user");
        }

    }

    /**
     * Increments the replyCount of the detailedTrumpet in ViewTrumpetActivity by 1 to represent a new reply Trumpet locally submitted.
     */
    public void incrementDetailedReplyCount(){
        int newReplyCount = (replyCountTextView.getText().charAt(0) - '0') + 1;
        replyCountTextView.setText(Integer.toString(newReplyCount) + " Replies");
    }

    /**
     * Sets the user's profile picture asynchronously with Picasso. If no profile picture has been uploaded, use default.
     */
    private void setProfilePicture(final int x, final int y){
        // if profilePicture is not null (a profile picture has been uploaded), use it. Otherwise, use default
        if (trumpetUser.getParseFile("profilePicture") != null) {
            Log.d("TrumpetView", "Loading custom profile picture");
            // Asynchronously optimizes and loads the user's profile picture. Loads the default profile picture as a placeholder.
            ParseFile profilePicture = trumpetUser.getParseFile("profilePicture");
            profilePicture.getFileInBackground(new GetFileCallback() {
                @Override
                public void done(File file, ParseException e) {
                    Picasso.with(getContext()).load(file).placeholder(R.drawable.default_profile_picture).resize(x, y).into(profilePictureImageView);
                }
            });
        } else {
            Log.d("TrumpetView", "Loading default profile picture");
            // Asynchronously optimizes and loads the default profile picture.
            Picasso.with(getContext()).load(R.drawable.default_profile_picture).resize(x, y).centerInside().into(profilePictureImageView);
        }
    }

    /**
     * Launches SubmitTrumpetActivity with intent data from this Trumpet which is being replied to.
     * @param inView, indicates whether or not to load a new ViewTrumpetActivity for replied-to Trumpet after submission. True if user is
     *                replying to a Trumpet from within its own ViewTrumpetActivity (can just finish() and refresh) and false if user is
     *                replying to a Trumpet in one of the ListViews, in which case the user needs to load the ViewTrumpetActivity of the
     *                Trumpet being replied to for the reply to be visible.
     */
    private void toSubmitTrumpetActivityReply(boolean inView){
        Intent intent = new Intent(getContext(), SubmitTrumpetActivity.class);
        intent.putExtra("trumpetUsername", username);
        intent.putExtra("trumpetID", trumpet.getInt("trumpetID"));
        intent.putExtra("objectID", trumpet.getObjectId());
        intent.putExtra("inView", inView);
        ((Activity)getContext()).startActivityForResult(intent, 0); // TODO pray to GOD that this context is FeedActivity
    }




}
