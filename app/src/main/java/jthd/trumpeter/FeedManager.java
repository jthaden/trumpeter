package jthd.trumpeter;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Iterator;
import java.util.List;

public class FeedManager {

    private final int MAX_TRUMPETS = 1000;

    private List<ParseObject> mTrumpetList;
    private Iterator<ParseObject> currentTrumpet;

    /**
     * Default constructor: Retrieves all Trumpets in database. Default constructor will eventually become dynamic (load only as necessary) constructor,
     * and this "load all" constructor will be moved.
     */
    public FeedManager() {
        retrieveAllTrumpets();
        currentTrumpet = mTrumpetList.iterator();
    }

    /**
     * Decides which Trumpet should be selected for the next ScrollView entry; currently returns Trumpets in descending order.
     * @return Returns the next Trumpet if it exists, otherwise returns null.
     */
    public ParseObject getNextTrumpet() {
        if (currentTrumpet.hasNext())
            return currentTrumpet.next();
        else
            return null;
    }

    /**
     * Retrieves MAX_TRUMPETS (currently 1000) most recent Trumpets in descending order (most recent Trumpets first) and stores them in the mTrumpetList
     * member list. This is as many as would ever need to be loaded at a given time.
     * NOTE: The loading time is probably not acceptable for anything more than 100 Trumpets. Use dynamic constructor which only loads some Trumpets
     * initially and loads more Trumpets when given some indication in FeedActivity.
     */
    private void retrieveAllTrumpets() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.orderByDescending("createdAt");
        query.setLimit(MAX_TRUMPETS);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    // Trumpets retrieved, store in private member list
                    mTrumpetList = trumpetList;
                } else {
                    // Error occurred retrieving Trumpets; display message to user
                }
            }
        });
    }


}
