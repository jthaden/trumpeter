package jthd.trumpeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class FeedActivity extends AppCompatActivity {

    private ParseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mUser = ParseUser.getCurrentUser();
        Log.d("FeedActivity", mUser.getEmail());

    }


}
