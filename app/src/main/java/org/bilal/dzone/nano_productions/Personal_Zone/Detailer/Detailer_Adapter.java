package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.bilal.dzone.nano_productions.R;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class Detailer_Adapter extends BaseAdapter {

    Activity con;
    String[] name, phone_number, done_date, model, year, color;

    public Detailer_Adapter(Activity con, String[] name, String[] phone_number, String[] done_date, String[] model, String[] year, String[] color) {
        this.con = con;
        this.name = name;
        this.phone_number = phone_number;
        this.done_date = done_date;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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


        viewholder.name_.setText(name[position]);
        viewholder.phone_number_.setText(phone_number[position]);
        viewholder.done_date_.setText(done_date[position]);
        viewholder.model_.setText(model[position]);
        viewholder.year_.setText(year[position]);
        viewholder.color_.setText(color[position]);


        if (color[position].equals("red") || color[position].equals("Red")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.red_app);
        } else if (color[position].equals("silver") || color[position].equals("Silver")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        } else if (color[position].equals("green")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        } else if (color[position].equals("black") || color[position].equals("Black")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.black_app);
        } else if (color[position].equals("white")) {
            viewholder.customer_app.setBackgroundResource(R.drawable.sliver_app);
        }else
            viewholder.customer_app.setBackgroundResource(R.drawable.blue_app);


        return convertView;
    }
}
