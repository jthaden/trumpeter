package jthd.trumpeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 *
 * NOTE: Unable to pass along ParseObject directly, as it isn't serializable. ParseProxyObject solution is available but results in nasty code,
 * requiring a function for PPO in TrumpetView. Currently just re-retrieving by objectID; should be fairly quick lookup. Making this a fragment
 * may solve this issue entirely, but first examine what new issues it creates.
 */

public class ViewTrumpetActivity extends AppCompatActivity {


    private final int MAX_TRUMPETS = 1000;

    private TrumpetView detailedTrumpetView;
    private ListView replyFeedListView;
    private Toolbar titleBar;
    private SwipeRefreshLayout replyFeedSwipeLayout;
    private TextView emptyTextView;

    private String detailedTrumpetObjectID;
    private int detailedTrumpetID;
    private boolean fromReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trumpet);
        Intent intent = getIntent();
        detailedTrumpetObjectID = intent.getStringExtra("objectID");
        detailedTrumpetID = intent.getIntExtra("trumpetID", -1);
        fromReply = intent.getBooleanExtra("fromReply", true);
        detailedTrumpetView = (TrumpetView)findViewById(R.id.detailedItemLayout);
        replyFeedListView = (ListView) findViewById(R.id.replyFeedListView);
        replyFeedSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.replyFeedSwipeLayout);
        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        replyFeedListView.setEmptyView(emptyTextView);
        // TODO Experimenting here by setting views in onCreate; seems like a better idea. If no issues, do it this way in all other activities where it makes sense
        setViews();
        titleBar = (Toolbar) findViewById(R.id.titleBar);
        setSupportActionBar(titleBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        replyFeedSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                      @Override
                                                      public void onRefresh() {
                                                          // This method performs the actual data-refresh operation.
                                                          // The method calls setRefreshing(false) when it's finished.
                                                          refreshListView();
                                                      }
                                                  }
        );
        // Manually defines OnScrollListener for replyFeedListView in order for SwipeRefreshLayout to have more than one child (e.g. for empty TextView).
        // Swiping to refresh is enabled when top row being shown is the top-most position, and disabled when it isn't.
        replyFeedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (replyFeedListView == null || replyFeedListView.getChildCount() == 0) ?
                                0 : replyFeedListView.getChildAt(0).getTop();
                replyFeedSwipeLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

    }


    /**
     * Upon returning from successful reply submission to the Trumpet currently being viewed, increment the reply counter of detailedTrumpet by 1 to reflect your new
     * reply and refresh the replyListView.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    detailedTrumpetView.incrementDetailedReplyCount();
                    replyFeedSwipeLayout.setRefreshing(true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            refreshListView();
                        }
                    }, 300);

                }
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_trumpet, menu);
        return true;
    }


    /**
     * Defines the ActionBar options. Currently have Back, which simply calls finish(), Refresh (always shown as icon) and Sign Out (always overflow menu).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.signOutAction:
                ParseUser.getCurrentUser().logOut();
                toLoginActivity();
            case R.id.settingsAction:
                // launch Settings activity
                return true;
            case R.id.refreshAction:
                replyFeedSwipeLayout.setRefreshing(true);
                refreshListView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Retrieve the calling Trumpet (given its object ID), load the detailed Trumpet View with its information, then query that Trumpet's replies and load the ListView
     * with those reply Trumpets in ascending (most recent last) order. Determines whether or not to wait 300 ms before loading based on source of launch; if
     * launched post-reply, wait 300 ms for reply upload. If not, just load replies immediately.
     * As stated in class documentation, this function currently does an (inexpensive) lookup that isn't really necessary. Performance impact should be minor but
     * continue to think about options. Lack of TrumpetID also forces a nested query, which is a bit weird.
     */
    private void setViews(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.whereEqualTo("objectId", detailedTrumpetObjectID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject trumpet, ParseException e) {
                if (e == null) {
                    detailedTrumpetView.showDetailedTrumpet(trumpet);
                    // If a reply was just submitted before loading this ViewTrumpetActivity, need to wait a small amount of time to ensure that the reply has been uploaded
                    // to Parse before loading reply data. Also, since detailedTrumpet was likely loaded before reply submission was processed, locally +1 the reply count.
                    // UPDATE ON THIS: reply count seems to be updated on Parse before loading detailedTrumpet every time, so no need to locally update reply count.
                    if (fromReply){
                        //detailedTrumpetView.incrementDetailedReplyCount();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                loadReplyListViewData();
                            }
                        }, 300);
                        // Otherwise, just load the reply data immediately
                    } else {
                        loadReplyListViewData();
                    }

                } else {

                }
            }
        });
    }

    /**
     * Loads the list of Trumpets that are marked as reply Trumpets to the Trumpet being viewed. This relationship is maintained through the attribute replyTrumpetID,
     * which points to the trumpetID of the Trumpet being viewed.
     */
    private void loadReplyListViewData(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.orderByAscending("createdAt");
        query.whereEqualTo("replyTrumpetID", detailedTrumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    // Trumpets retrieved, create FeedAdapter with this data and load the Adapter into the ListView
                    Log.d("FeedManager", "Found trumpets");
                    FeedAdapter adapter = new FeedAdapter(ViewTrumpetActivity.this, R.layout.trumpet_view_layout, trumpetList);
                    replyFeedListView.setAdapter(adapter);
                    Log.d("FeedManager", "trumpetList count " + trumpetList.size());
                } else {
                    // Error occurred retrieving Trumpets; display message to user
                    Log.d("FeedManager", "Found no trumpets");
                }
            }
        });
    }


    /**
     * Refreshes the reply data in the ListView's adapter and reloads the new data into the existing ListView TrumpetViews.
     * Note: Call feedSwipeLayout.setRefreshing(true) before every call to this function. This function sets refreshing to false once refreshing
     * is complete.
     */
    private void refreshListView(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Trumpet");
        query.orderByAscending("createdAt");
        query.whereEqualTo("replyTrumpetID", detailedTrumpetID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> trumpetList, ParseException e) {
                if (e == null) {
                    // Trumpets retrieved, get adapter and update its internal trumpetList
                    Log.d("FeedActivity", Integer.toString(trumpetList.size()));
                    Log.d("FeedActivity", "Found trumpets to refresh");
                    FeedAdapter adapter = (FeedAdapter) replyFeedListView.getAdapter();
                    adapter.getNewData(trumpetList);
                    Log.d("FeedActivity", "Trumpets refreshed");
                    adapter.notifyDataSetChanged();
                    replyFeedSwipeLayout.setRefreshing(false);
                } else {
                    // Error occurred retrieving Trumpets; display message to user
                    Log.d("FeedActivity", "Found no trumpets to refresh");
                }
            }
        });
    }

    /**
     * Returns to LoginActivity after successful sign-out. Flags clear the backstack so user cannot return without logging in again.
     */
    private void toLoginActivity(){
        Intent intent = new Intent(ViewTrumpetActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
