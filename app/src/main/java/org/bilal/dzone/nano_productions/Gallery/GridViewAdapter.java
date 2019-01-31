package org.bilal.dzone.nano_productions.Gallery;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.Personal_Zone.NewsfeedAdapter;
import org.bilal.dzone.nano_productions.Personal_Zone.Products.Model_Class;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.URL.Url;

import java.util.ArrayList;

/**
 * Created by Belal on 12/22/2015.
 */
public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;

    //Context
    private Activity con;

    //Array List that would contain the urls and the titles for the images

    ArrayList<String> modelClasses;


    public GridViewAdapter(Activity con, ArrayList<String> modelClasses) {
        //Getting all the values
        this.con = con;
        this.modelClasses = modelClasses;

    }

    @Override
    public int getCount() {
        return modelClasses.size();
    }

    @Override
    public Object getItem(int position) {
        return modelClasses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class Viewholder {

        ImageView imageView;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Viewholder viewholder;

        LayoutInflater inflater = con.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.gallery_itemsa, null);

            viewholder.imageView = convertView.findViewById(R.id.image1);

            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        Glide.with(convertView).load(modelClasses.get(position)).into(viewholder.imageView);

        Log.e("adapter", modelClasses.get(position));

        return convertView;
    }
}