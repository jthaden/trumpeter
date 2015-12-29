package jthd.trumpeter;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.parse.ParseUser;

public class FeedActivity extends AppCompatActivity {

    private ParseUser mUser;

    private Toolbar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mTitleBar = (Toolbar) findViewById(R.id.titleBar);
        setSupportActionBar(mTitleBar);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mUser = ParseUser.getCurrentUser();
        Log.d("FeedActivity", mUser.getEmail());
        launchSubmitBarFragment();
    }

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
