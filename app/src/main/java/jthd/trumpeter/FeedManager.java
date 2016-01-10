package jthd.trumpeter;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The static FeedManager class decides which Trumpets to provide for display in FeedActivity. Currently just returns all, most recent first.
 * Since this class is now static, most of the decision-making should be occurring as I construct the list and before I return it to Adapter
 */
// TODO another gacky need for a static variable just to be able to return here... need to find out what else I can do
public class FeedManager {

    private final int MAX_TRUMPETS = 1000;


    private List<ParseObject> trumpetList = new ArrayList<ParseObject>();


    public FeedManager(){
        trumpetList = retrieveAllTrumpets();
    }



    public List<ParseObject> getTrumpets(){
        return trumpetList;
    }

    /**
     * Retrieves MAX_TRUMPETS (currently 1000) most recent Trumpets in descending order (most recent Trumpets first) and returns them as a List<ParseObject>.
     * NOTE: The loading time is probably not acceptable for anything more than 100 Trumpets. TODO: Don't need to load them all at once. Optimize.
     * @return Returns list of Trumpets for display in FeedActivity.
     */
    private List<ParseObject> retrieveAllTrumpets() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.orderByDescending("createdAt");
        query.setLimit(MAX_TRUMPETS);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tList, ParseException e) {
                if (e == null) {
                    // Trumpets retrieved, store in private member list
                    Log.d("FeedManager", "Found trumpets");
                    trumpetList = new ArrayList<ParseObject>(tList);
                    Log.d("FeedManager", "trumpetList count " + trumpetList.size());
                } else {
                    // Error occurred retrieving Trumpets; display message to user
                    Log.d("FeedManager", "Found no trumpets");
                }
            }
        });
        Log.d("FeedManager", "trumpetList count at return" + trumpetList.size());
        return trumpetList;
        // TODO honestly not sure why this return is necessary, and why copy straight to member variable isn't working; revisit later
    }



}
