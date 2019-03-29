package org.bilal.dzone.nano_productions.Personal_Zone.Importer;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.DetailerModelClass;
import org.bilal.dzone.nano_productions.R;

import java.util.ArrayList;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class Importer_Adapter extends BaseAdapter implements Filterable {

    Activity con;
    private ArrayList<ImporterModelClass> mOriginalValues; // Original Values
    private ArrayList<ImporterModelClass> mDisplayedValues;    // Values to be displayed


    public Importer_Adapter(Activity con, ArrayList<ImporterModelClass> detailerArrayList) {
        this.con = con;
        this.mDisplayedValues = detailerArrayList;
        this.mOriginalValues = detailerArrayList;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class Viewholder {

        TextView name, phone_number, subscriptions_;
        Button call;
        FloatingActionButton add;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.importer_list_items, null);


            viewholder.name = (TextView) convertView.findViewById(R.id.name);
            viewholder.phone_number = (TextView) convertView.findViewById(R.id.number);
            viewholder.subscriptions_ = (TextView) convertView.findViewById(R.id.subscriptions);
            viewholder.call = (Button) convertView.findViewById(R.id.btn);
            viewholder.add = (FloatingActionButton) convertView.findViewById(R.id.add);


            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }

        viewholder.add.setFocusable(false);


        viewholder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 999111);
            }
        });


        viewholder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 999222);
            }
        });


        viewholder.name.setText(mDisplayedValues.get(position).getName());
        viewholder.phone_number.setText(mDisplayedValues.get(position).getPhone_number());
        viewholder.subscriptions_.setText(mDisplayedValues.get(position).getUsed_subscriptions()
                + " / " + mDisplayedValues.get(position).getDetailer_subscriptions());


        return convertView;
    }


    //filter arraylist
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<ImporterModelClass>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ImporterModelClass> FilteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getName();
                        String data6 = mOriginalValues.get(i).getPhone_number();
                        if (data.toLowerCase().startsWith(constraint.toString())
                                || data6.toLowerCase().startsWith(constraint.toString())) {

                            FilteredArrList.add(new ImporterModelClass(mOriginalValues.get(i).getName()
                                    , mOriginalValues.get(i).getEmail(),
                                    mOriginalValues.get(i).getPhone_number(),
                                    mOriginalValues.get(i).getDetailer_id(),
                                    mOriginalValues.get(i).getLongitude(),
                                    mOriginalValues.get(i).getLongitude(),
                                    mOriginalValues.get(i).getAddress(),
                                    mOriginalValues.get(i).getDetailer_subscriptions(),
                                    mOriginalValues.get(i).getUsed_subscriptions()));


                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
