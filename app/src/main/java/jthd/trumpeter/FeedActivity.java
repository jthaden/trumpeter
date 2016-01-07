package jthd.trumpeter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseUser;

public class FeedActivity extends AppCompatActivity {

    private ParseUser mUser;

    private Toolbar mTitleBar;
    private ListView mFeedListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mTitleBar = (Toolbar) findViewById(R.id.titleBar);
        mFeedListView = (ListView) findViewById(R.id.feedListView);
        // TODO Is this constructor acceptable? Should context be this or context provided by App?
        mFeedListView.setAdapter(new FeedAdapter(this, R.layout.trumpetView_layout));
        setSupportActionBar(mTitleBar);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mUser = ParseUser.getCurrentUser();
        Log.d("FeedActivity", mUser.getEmail());
        launchSubmitBarFragment();

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


}
