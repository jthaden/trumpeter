package jthd.trumpeter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private TextView usernameTextView;
    private TextView trumpetTextView;
    private TextView retrumpetCountTextView;
    private TextView likeCountTextView;
    private TextView retrumpetTextView;
    private ImageView profilePictureImageView;
    private ImageButton replyButton;
    private ImageButton retrumpetButton;
    private ImageButton likeButton;



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
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        trumpetTextView = (TextView) findViewById(R.id.trumpetTextView);
        retrumpetCountTextView = (TextView) findViewById(R.id.retrumpetCountTextView);
        likeCountTextView = (TextView) findViewById(R.id.likeCountTextView);
        retrumpetTextView = (TextView) findViewById(R.id.retrumpetTextView);
        profilePictureImageView = (ImageView) findViewById(R.id.profilePictureImageView);
        replyButton = (ImageButton) findViewById(R.id.replyButton);
        retrumpetButton = (ImageButton) findViewById(R.id.retrumpetButton);
        likeButton = (ImageButton) findViewById(R.id.likeButton);
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
            retrumpetTextView.setVisibility(View.VISIBLE);
            retrumpeter = (String) trumpet.get("retrumpeter");
            retrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            retrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture();
        usernameTextView.setText(username);
        trumpetTextView.setText(text);
        retrumpetCountTextView.setText(Integer.toString(retrumpets));
        likeCountTextView.setText(Integer.toString(likes));
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        retrumpetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retrumpetCountTextView.setText(Integer.toString(retrumpets + 1));
                // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
            }
        });
        likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountTextView.setText(Integer.toString(likes + 1));
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
            retrumpetTextView.setVisibility(View.VISIBLE);
            retrumpeter = (String)trumpet.get("retrumpeter");
            retrumpetTextView.setText("Retrumpeted by " + retrumpeter);
        } else {
            retrumpetTextView.setVisibility(View.GONE);
        }
        setProfilePicture();
        usernameTextView.setText(username);
        trumpetTextView.setText(text);
        retrumpetCountTextView.setText(Integer.toString(retrumpets) + " Retrumpets");
        likeCountTextView.setText(Integer.toString(likes) + " Likes");
        replyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        retrumpetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retrumpetCountTextView.setText(Integer.toString(retrumpets + 1) + " Retrumpets");
                // Calls UpdateManager.updateRetrumpetCount and submits retrumpet
                SubmitTrumpetManager.submitRetrumpet(trumpet, ParseUser.getCurrentUser().getString("username"));
            }
        });
        likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCountTextView.setText(Integer.toString(likes + 1) + " Likes");
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
            Picasso.with(App.getAppContext()).load(profilePicture.getUrl()).placeholder(R.drawable.default_profile_picture).resize(180, 180).into(profilePictureImageView);
        } else {
            // Asynchronously optimizes and loads the default profile picture.
            // TODO Acceptable way to get context here? Is there an easier way (it is passed in). Does it matter?
            Picasso.with(App.getAppContext()).load(R.drawable.default_profile_picture).resize(180, 180).centerInside().into(profilePictureImageView);

        }
    }




}
