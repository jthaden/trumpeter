package jthd.trumpeter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom Adapter class, extended from ArrayAdapter. Stores Trumpet ParseObject list that provides data to be used
 * for each call of getView(). Setting of Views is handled in TrumpetView, a custom View object that manages the layout for each Trumpet,
 * found in trumpet_view_layout.xmll. This leaves the Adapter performing solely the Controller role.
 */
public class FeedAdapter extends ArrayAdapter<ParseObject> {

    private List<ParseObject> trumpetList;
    private Context context;


    // TODO Why this constructor?

    /**
     * This constructor currently allows for the Adapter to be used in other activities provided a different context, but I have no plans to do this.
     * Due to the TrumpetView custom View implementation, this Adapter is bound to layouts with exactly the same views as in TrumpetView (a very acceptable
     * sacrifice as it would be odd to need additional views elsewhere), so remember this before loading a different resource.
     */
    public FeedAdapter(Context context, int resource, List<ParseObject> data){
        super(context, resource, data);
        trumpetList = new ArrayList<ParseObject>(data);
        this.context = context;
        // Do I need to do anything with context or resource in this class?
    }

    @Override
    public int getCount() {
        Log.d("FeedAdapter", "getCount() called, " + trumpetList.size() + " returned");
        return trumpetList.size();
    }

    @Override
    public ParseObject getItem(int position){
        return trumpetList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return trumpetList.get(arg0).getInt("trumpetID");
    }



    @Override
    /**
     * getView overridden to populate custom trumpetView, which represents the complete layout for each "Trumpet" or item in the ListView.
     * TrumpetView serves as a "ViewHolder", requiring the views in trumpetView to be looked up only once. trumpetList provides the
     * Trumpet data, which was created and filtered in FeedManager. getView() is called as needed (when user scrolls past loaded items, etc).
     */
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d("FeedAdapter", "getView called");
        TrumpetView view;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null){
            view = (TrumpetView) inflater.inflate(R.layout.trumpet_view_layout, null);
        } else {
            view = (TrumpetView) convertView;
        }
        ParseObject trumpet = trumpetList.get(position);
        view.showTrumpet(trumpet);
        return view;
    }


}
