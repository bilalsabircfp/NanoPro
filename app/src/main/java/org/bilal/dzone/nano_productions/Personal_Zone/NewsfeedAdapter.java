package org.bilal.dzone.nano_productions.Personal_Zone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.URL.Url;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class NewsfeedAdapter extends BaseAdapter {

    Activity con;
    String[] title, content, image, time;

    public NewsfeedAdapter(Activity con, String[] title, String[] content, String[] image,String[] time) {
        this.con = con;
        this.title = title;
        this.content = content;
        this.image = image;
        this.time = time;

    }

    @Override
    public int getCount() {
        return title.length;
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

        TextView title, content, time;
        ImageView imageView;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.newsfeed_listview_items, null);


            viewholder.time = (TextView) convertView.findViewById(R.id.time);
            viewholder.title = (TextView) convertView.findViewById(R.id.name);
            viewholder.content = (TextView) convertView.findViewById(R.id.post);
            viewholder.imageView =  convertView.findViewById(R.id.profile_image);

            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        viewholder.time.setText(time[position]);
        viewholder.title.setText(title[position]);
        viewholder.content.setText(content[position]);
        Glide.with(convertView).load(image[position]).into(viewholder.imageView);



        return convertView;
    }
}
