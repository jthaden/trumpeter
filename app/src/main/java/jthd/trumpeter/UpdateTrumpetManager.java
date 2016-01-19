package jthd.trumpeter;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


// TODO TrumpetUpdateManager?
public final class UpdateTrumpetManager {

    private UpdateTrumpetManager(){

    }

    public static void updateRetrumpetCount(int trumpetID){
        Log.d("UpdateTrumpetManager", "Updating ID: " + Integer.toString(trumpetID));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("trumpetID", trumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    Log.d("UpdateTrumpetManager", "Trumpets found to update: " + Integer.toString(trumpetList.size()));
                    for (ParseObject trumpet : trumpetList){
                        trumpet.increment("retrumpets");
                    }
                } else {
                    // There should always be at least one trumpet when this function is run, so this should never happen
                }
            }
        });
    }

    public static void updateLikeCount(int trumpetID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("trumpetID", trumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    for (ParseObject trumpet : trumpetList){
                        trumpet.increment("likes");
                    }
                } else {
                    // There should always be at least one trumpet when this function is run, so this should never happen
                }
            }
        });
    }


}
