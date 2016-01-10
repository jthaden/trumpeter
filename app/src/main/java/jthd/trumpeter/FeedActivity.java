package jthd.trumpeter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements SubmitBarFragment.OnFragmentInteractionListener{

    private final int MAX_TRUMPETS = 1000;

    private ParseUser mUser;

    private Toolbar mTitleBar;
    private ListView mFeedListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mTitleBar = (Toolbar) findViewById(R.id.titleBar);
        mFeedListView = (ListView) findViewById(R.id.feedListView);
        /**
         * Waiting on FeedManager concept. Need to verify how to save List<ParseObject> data from a Callback inner class and return it in a function,
         * or I'll have to do all similar queries like this.
         */
        // FeedManager feedManager = new FeedManager();
        // List<ParseObject> trumpetList = feedManager.getTrumpets();
        loadListViewData();
        //mFeedListView.deferNotifyDataSetChanged();
        setSupportActionBar(mTitleBar);
        Log.d("FeedActivity", "SUPER BOOP");
    }

    @Override
    protected void onResume(){
        super.onResume();
        mUser = ParseUser.getCurrentUser();
        Log.d("FeedActivity", mUser.getEmail());
        launchSubmitBarFragment();
        // TODO; need a good way to refresh. Call refresh function (that refresh button also uses) on resume, that somehow loads adapter with new info?
        // Make new adapter and load it? All that needs to be done is making a need FeedManager in FeedAdapter; function that just does that?
        // Clarify: Need to refresh when "reloading" it EXCEPT when backing onto it (in which case I should be at the same scroll position and data)
        // Back button should be onRestart(); may not need to do anything. All other forms of creation (I think) should reload data and start user at top

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsAction:
                // launch Settings activity
                return true;
            //case R.id.profileAction:
            // launch ProfileActivity
            // return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSubmitBarFragmentInteraction(Uri uri) {

    }


    /**
     * Queries Parse for desired Trumpet data and loads that Trumpet data into the Feed ListView as a custom ArrayAdapter, FeedAdapter.
     * Currently loading basically all (MAX_TRUMPET) Trumpets. This won't scale obviously so will need to future-proof at some point.
     */
    private void loadListViewData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.orderByDescending("createdAt");
        query.setLimit(MAX_TRUMPETS);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    // Trumpets retrieved, create FeedAdapter with this data and load the Adapter into the ListView
                    Log.d("FeedManager", "Found trumpets");
                    FeedAdapter adapter = new FeedAdapter(FeedActivity.this, R.layout.trumpet_view_layout, trumpetList);
                    mFeedListView.setAdapter(adapter);
                    Log.d("FeedManager", "trumpetList count " + trumpetList.size());
                } else {
                    // Error occurred retrieving Trumpets; display message to user
                    Log.d("FeedManager", "Found no trumpets");
                }
            }
        });
    }

    /**
     * Launches the Submit Bar at the bottom of the layout that allows for users to travel to SubmitTrumpetActivity and "submit" Trumpets to Parse.
     */
    private void launchSubmitBarFragment(){
        Fragment trumpetFragment = new SubmitBarFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.feedLayout, trumpetFragment);
        transaction.commit();
    }


}
