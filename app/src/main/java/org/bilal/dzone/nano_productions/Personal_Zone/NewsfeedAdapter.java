package org.bilal.dzone.nano_productions.Personal_Zone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class NewsfeedAdapter extends BaseAdapter {

    Activity con;
    String[] title, content, image, time, name, role;

    public NewsfeedAdapter(Activity con, String[] title, String[] content, String[] image, String[] time, String[] name, String[] role) {
        this.con = con;
        this.title = title;
        this.content = content;
        this.image = image;
        this.time = time;
        this.name = name;
        this.role = role;
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

        TextView title, content, time, name, role;
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
            viewholder.name = (TextView) convertView.findViewById(R.id.name);
            viewholder.content = (TextView) convertView.findViewById(R.id.post);
            viewholder.imageView =  convertView.findViewById(R.id.profile_image);
            viewholder.title = (TextView) convertView.findViewById(R.id.title);
            viewholder.role = (TextView) convertView.findViewById(R.id.role);

            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        viewholder.time.setText(time[position]);
        viewholder.title.setText(name[position]);
        viewholder.content.setText(content[position]);
        viewholder.name.setText(title[position]);
        viewholder.role.setText(role[position]);
        Glide.with(convertView).load(Url.BaseUrl + image[position]).into(viewholder.imageView);



        return convertView;
    }
}
