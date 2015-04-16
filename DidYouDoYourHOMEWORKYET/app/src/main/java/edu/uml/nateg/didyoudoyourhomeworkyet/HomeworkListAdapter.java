package edu.uml.nateg.didyoudoyourhomeworkyet;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

public class HomeworkListAdapter extends BaseAdapter {

    // List of HomeworkItems
    private final List<HomeworkItem> mItems = new ArrayList<HomeworkItem>();

    private final Context mContext;

    public HomeworkListAdapter(Context context) {

        mContext = context;

    }

    // Add a HomeworkItem to the adapter

    public void add(HomeworkItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    // Clears the list adapter of all items.

    public void clear(){

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of HomeworkItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of HomeworkItems

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the HomeworkItem

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    //Create a View to display the ToDoItem
    // at specified position in mItems

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout itemLayout;
        if (convertView == null) {
            log("null convertview");
            itemLayout = (RelativeLayout) LayoutInflater.from(mContext)
                            .inflate(R.layout.adapter_homework_item, null);
        } else {
            log("non-null convertview");
            itemLayout = (RelativeLayout) convertView;
        }

        // Get the current HomeworkItem
        final HomeworkItem hwItem = mItems.get(position);

        //Fill in specific HomeworkItem data

        // Display Title in TextView

        TextView titleView = (TextView) itemLayout.findViewById(R.id.adapterNameView);
        titleView.setText(hwItem.getTitle());


        // Display Percent Done in a TextView

        final TextView percentView = (TextView) itemLayout.findViewById(R.id.adapterPercentView);
        percentView.setText(hwItem.getPriority().toString());


        //Display Priority in a TextView

        final TextView priorityView = (TextView) itemLayout.findViewById(R.id.adapterPriorityView);
        // Display Date.


        final TextView dateView = (TextView) itemLayout.findViewById(R.id.adapterDateView);
        dateView.setText(HomeworkItem.FORMAT.format(hwItem.getDate()));

        // Return the View you just created
        return itemLayout;

    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}