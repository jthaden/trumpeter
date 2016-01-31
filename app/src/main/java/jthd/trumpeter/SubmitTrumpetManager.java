package jthd.trumpeter;


import android.os.Handler;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Static class in charge of all Trumpet submission, whether it be a new Trumpet, a Retrumpet, or a reply Trumpet.
 */
public final class SubmitTrumpetManager {

    // Necessary for use in next ID query callback; ensure that this causes no issues, e.g. concurrency
    private static ParseObject trumpet;

    private SubmitTrumpetManager() {

    }

    /**
     * Creates a new ParseObject of class "Trumpet" with the user-provided information and submitting ParseUser. Submission date automatically saved in
     * "createdAt" field.
     * @param text The user-submitted text to be associated with this Trumpet.
     * @param user The ParseUser that submitted the Trumpet.
     */
    public static void submitNewTrumpet(String text, ParseUser user) {
        trumpet = new ParseObject("Trumpet");
        trumpet.put("text", text);
        trumpet.put("user", user);
        trumpet.put("retrumpet", false);
        //trumpet.put("retrumpeter", ""); Defaults to empty
        trumpet.put("retrumpets", 0);
        trumpet.put("likes", 0);
        trumpet.put("replies", 0);
        // This query retrieves the next available Trumpet ID for the new Trumpet from the TrumpetCounter object and atomically increments the counter field.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TrumpetCounter");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject trumpetCounter, ParseException e) {
                if (e == null) {
                    trumpet.put("trumpetID", trumpetCounter.getInt("nextTrumpetID"));
                    trumpet.saveInBackground();
                    /*
                    try{
                        trumpet.save();
                    } catch(ParseException exception){

                    }
                    */
                    trumpetCounter.increment("nextTrumpetID");
                    trumpetCounter.saveInBackground();
                } else {

                }
            }
        });
    }

    /**
     * Creates a new "retrumpet" Trumpet that is an exact clone of the retrumpeted Trumpet with the exception of two additional pieces of information: the user
     * that initiated the Retrumpet, and the retrumpet = true boolean (likely not necessary but helps keep the logic a bit cleaner). Retrumpets share the same TrumpetID
     * as the original Trumpet, but can be differentiated by these new fields and their different objectID, which allows for loading of that specific Retrumpet
     * in ViewTrumpetActivity.
     * @param trumpet, the original Trumpet that is being "retrumpeted".
     * @param retrumpeter, the username of the user that initiated the retrumpet.
     */
    public static void submitRetrumpet(final ParseObject trumpet, String retrumpeter) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateTrumpetManager.updateRetrumpetCount(trumpet.getInt("trumpetID"));
            }
        }, 200);
        ParseObject retrumpet = new ParseObject("Trumpet");
        retrumpet.put("text", trumpet.get("text"));
        retrumpet.put("user", trumpet.get("user"));
        retrumpet.put("retrumpet", true);
        retrumpet.put("retrumpeter", retrumpeter);
        retrumpet.put("retrumpets", trumpet.getInt("retrumpets"));
        retrumpet.put("likes", trumpet.get("likes"));
        retrumpet.put("replies", trumpet.get("replies"));
        retrumpet.put("trumpetID", trumpet.get("trumpetID"));
        retrumpet.saveInBackground();
    }

    /**
     * Creates a "reply" Trumpet that is linked to another Trumpet (the one replied to) by the field "replyTrumpetID". Trumpet is otherwise identical to any
     * other new Trumpet. Automatically updates reply count for Trumpet that is being replied to.
     * @param text The user-submitted text to be associated with this Trumpet.
     * @param user The ParseUser that submitted the Trumpet.
     * @param replyTrumpetID The trumpetID of the Trumpet that is being replied to.
     */

    public static void submitReplyTrumpet(String text, ParseUser user, int replyTrumpetID) {
        UpdateTrumpetManager.updateReplyCount(replyTrumpetID);
        trumpet = new ParseObject("Trumpet");
        trumpet.put("text", text);
        trumpet.put("user", user);
        trumpet.put("retrumpet", false);
        //trumpet.put("retrumpeter", ""); Defaults to empty
        trumpet.put("retrumpets", 0);
        trumpet.put("likes", 0);
        trumpet.put("replies", 0);
        trumpet.put("replyTrumpetID", replyTrumpetID);
        // This query retrieves the next available Trumpet ID for the new Trumpet from the TrumpetCounter object and atomically increments the counter field.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TrumpetCounter");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject trumpetCounter, ParseException e) {
                if (e == null) {
                    trumpet.put("trumpetID", trumpetCounter.getInt("nextTrumpetID"));
                    trumpet.saveInBackground();
                    trumpetCounter.increment("nextTrumpetID");
                    trumpetCounter.saveInBackground();
                } else {

                }
            }
        });
    }
}


