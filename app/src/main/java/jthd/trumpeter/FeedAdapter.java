package jthd.trumpeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.ParseObject;

/**
 * Created by Jesse on 1/5/2016.
 */
public class FeedAdapter extends ArrayAdapter<String> {

    private FeedManager mFeedManager;
    private Context mContext;



    public FeedAdapter(Context context, int resource){
        super(context, resource);
        mFeedManager = new FeedManager();
        mContext = context;
        // Do I need to do anything with resource in this class?
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TrumpetView view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null){
            view = (TrumpetView) inflater.inflate(R.layout.feed_item, null);
        } else {
            view = (TrumpetView) convertView;
        }
        ParseObject trumpet = mFeedManager.getNextTrumpet();
        view.showTrumpet(trumpet);
        return view;
    }


}
