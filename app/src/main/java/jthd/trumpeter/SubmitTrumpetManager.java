package jthd.trumpeter;


import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;



public final class SubmitTrumpetManager {

    // Necessary for use in next ID query callback; ensure that this causes no issues, e.g. concurrency
    private static ParseObject trumpet;

    private SubmitTrumpetManager() {

    }

    /**
     * Creates a new ParseObject of class "Trumpet" with the user-provided information and submitting ParseUser. Submission date automatically saved in
     * "createdAt" field.
     */
    public static void submitNewTrumpet(String text, ParseUser user) {
        trumpet = new ParseObject("Trumpet");
        trumpet.put("text", text);
        trumpet.put("user", user);
        trumpet.put("retrumpet", false);
        //trumpet.put("retrumpeter", ""); Should default to empty "", right?
        trumpet.put("retrumpets", 0);
        trumpet.put("likes", 0);
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


    public static void submitRetrumpet(ParseObject trumpet, String retrumpeter) {
        UpdateTrumpetManager.updateRetrumpetCount(trumpet.getInt("trumpetID"));
        ParseObject retrumpet = new ParseObject("Trumpet");
        retrumpet.put("text", trumpet.get("text"));
        retrumpet.put("user", trumpet.get("user"));
        retrumpet.put("retrumpet", true);
        retrumpet.put("retrumpeter", retrumpeter);
        retrumpet.put("retrumpets", trumpet.get("retrumpets"));
        retrumpet.put("likes", trumpet.get("likes"));
        retrumpet.put("trumpetID", trumpet.get("trumpetID"));
        retrumpet.saveInBackground();
    }
}


