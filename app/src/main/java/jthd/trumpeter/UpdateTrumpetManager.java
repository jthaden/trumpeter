package jthd.trumpeter;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * Static class that manages Trumpet update actions, e.g. updating retrumpet count, like count, and reply count.
 */
public final class UpdateTrumpetManager {

    private UpdateTrumpetManager(){

    }


    /**
     * Updates the retrumpet count for all Trumpets with trumpetID.
     */
    public static void updateRetrumpetCount(int trumpetID){
        Log.d("UpdateTrumpetManager", "Updating ID: " + Integer.toString(trumpetID));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("trumpetID", trumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    Log.d("UpdateTrumpetManager", "Trumpets found to update: " + Integer.toString(trumpetList.size()));
                    int count = 1;
                    for (ParseObject trumpet : trumpetList){
                        Log.d("UpdateTrumpetManager", "Updating retrumpet count for trumpet #" + Integer.toString(count));
                        trumpet.increment("retrumpets");
                        count++;
                        trumpet.saveInBackground();
                    }
                } else {
                    // There should always be at least one trumpet when this function is run, so this should never happen
                }
            }
        });
    }

    /**
     * Updates the like count for all Trumpets with trumpetID.
     */
    public static void updateLikeCount(int trumpetID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("trumpetID", trumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    for (ParseObject trumpet : trumpetList){
                        trumpet.increment("likes");
                        trumpet.saveInBackground();
                    }
                } else {
                    // There should always be at least one trumpet when this function is run, so this should never happen
                }
            }
        });
    }

    /**
     * Updates the reply count for all Trumpets with trumpetID.
     */
    public static void updateReplyCount(int trumpetID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("trumpetID", trumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    for (ParseObject trumpet : trumpetList){
                        trumpet.increment("replies");
                        trumpet.saveInBackground();
                    }
                } else {
                    // There should always be at least one trumpet when this function is run, so this should never happen
                }
            }
        });
    }


}
