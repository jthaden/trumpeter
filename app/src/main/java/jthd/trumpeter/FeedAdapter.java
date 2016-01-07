package jthd.trumpeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.ParseObject;

/**
 * Custom Adapter class, extended from ArrayAdapter. Stores data source (FeedManager object) that provides data to be used in each Trumpet
 * for each call of getView(). Setting of Views is handled in TrumpetView, a custom View object that manages the layout for each Trumpet,
 * found in trumpetView_layout.xml. This leaves the Adapter performing solely the Controller role.
 */
public class FeedAdapter extends ArrayAdapter<String> {

    private FeedManager mFeedManager;
    private Context mContext;


    // TODO Why this constructor?
    public FeedAdapter(Context context, int resource){
        super(context, resource);
        mFeedManager = new FeedManager();
        mContext = context;
        // Do I need to do anything with context or resource in this class?
    }

    @Override
    /**
     * getView overridden to populate custom trumpetView, which represents the complete layout for each "Trumpet" or item in the ListView.
     * TrumpetView serves as a "ViewHolder", requiring the views in trumpetView to be looked up only once. mFeedManager provides the next
     * Trumpet (decided in FeedManager) when getView() is called. getView() is called as needed (when user scrolls past loaded items, etc).
     */
    public View getView(int position, View convertView, ViewGroup parent){
        TrumpetView view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null){
            view = (TrumpetView) inflater.inflate(R.layout.trumpetView_layout, null);
        } else {
            view = (TrumpetView) convertView;
        }
        ParseObject trumpet = mFeedManager.getNextTrumpet();
        view.showTrumpet(trumpet);
        return view;
    }


}
