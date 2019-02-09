package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.bilal.dzone.nano_productions.R;

import java.util.ArrayList;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class Detailer_Adapter extends BaseAdapter implements Filterable {

    Activity con;
    private ArrayList<DetailerModelClass> mOriginalValues; // Original Values
    private ArrayList<DetailerModelClass> mDisplayedValues;    // Values to be displayed


    public Detailer_Adapter(Activity con, ArrayList<DetailerModelClass> detailerArrayList) {
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

        TextView name_, phone_number_, done_date_, model_, year_, color_;
        Button btn;
        LinearLayout customer_app;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.customer_list_items, null);


            viewholder.name_ = (TextView) convertView.findViewById(R.id.customer_name);
            viewholder.phone_number_ = (TextView) convertView.findViewById(R.id.customer_number);
            viewholder.done_date_ = (TextView) convertView.findViewById(R.id.maint_date);
            viewholder.model_ = (TextView) convertView.findViewById(R.id.car_name);
            viewholder.year_ = (TextView) convertView.findViewById(R.id.car_model);
            viewholder.color_ = (TextView) convertView.findViewById(R.id.car_color);
            viewholder.customer_app = (LinearLayout) convertView.findViewById(R.id.customer_layout);
            viewholder.btn = (Button) convertView.findViewById(R.id.btn);

            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        viewholder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ListView) parent).performItemClick(v, position, 1);
            }
        });


        viewholder.name_.setText(mDisplayedValues.get(position).getName());
        viewholder.phone_number_.setText(mDisplayedValues.get(position).getPhone_number());
        viewholder.done_date_.setText(mDisplayedValues.get(position).getDone_date());
        viewholder.model_.setText(mDisplayedValues.get(position).getModel());
        viewholder.year_.setText(mDisplayedValues.get(position).getYear());
        viewholder.color_.setText(mDisplayedValues.get(position).getColor());


        if (mDisplayedValues.get(position).getColor().equals("red") ||
                mDisplayedValues.get(position).getColor().equals("Red")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.red_app);
        } else if (mDisplayedValues.get(position).getColor().equals("silver") ||
                mDisplayedValues.get(position).getColor().equals("Silver")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        } else if (mDisplayedValues.get(position).getColor().equals("green")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        } else if (mDisplayedValues.get(position).getColor().equals("black") ||
                mDisplayedValues.get(position).getColor().equals("Black")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.black_app);
        } else if (mDisplayedValues.get(position).getColor().equals("white")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        }else
            viewholder.customer_app.setBackgroundResource(R.drawable.blue_app);


        return convertView;
    }


    //filter arraylist
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<DetailerModelClass>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<DetailerModelClass> FilteredArrList = new ArrayList<>();

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
                        String data2 = mOriginalValues.get(i).getModel();
                        if (data.toLowerCase().startsWith(constraint.toString())
                                || data2.toLowerCase().startsWith(constraint.toString())) {

                            FilteredArrList.add(new DetailerModelClass(mOriginalValues.get(i).getName()
                                    ,mOriginalValues.get(i).getPhone_number()
                                    ,mOriginalValues.get(i).getDone_date()
                                    ,mOriginalValues.get(i).getModel()
                                    ,mOriginalValues.get(i).getYear()
                                    ,mOriginalValues.get(i).getColor()
                                    ,mOriginalValues.get(i).getTitle()
                                    ,mOriginalValues.get(i).getEdition()
                                    ,mOriginalValues.get(i).getEmail()
                                    ,mOriginalValues.get(i).getWarranty_code()
                                    ,mOriginalValues.get(i).getLicense_plate_no()));


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
