package org.bilal.dzone.nano_productions.Gallery.albums;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.Gallery.GalleryAdapter;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.gallery.Gallery_Model;
import org.bilal.dzone.nano_productions.URL.Url;

import java.util.ArrayList;


/**
 * Created by Bilal on 17-Feb-17.
 */

public class Albums_Adapter extends BaseAdapter {

    ArrayList<Gallery_Model> modelClasses;
    private Activity context;

    public Albums_Adapter (Activity context, ArrayList<Gallery_Model> modelClasses){
        //Getting all the values
        this.context = context;
        this.modelClasses = modelClasses;

    }

    @Override
    public int getCount() {
        return modelClasses.size();
    }

    @Override
    public Object getItem(int position) {
        return modelClasses.get(position).getImage();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class Viewholder {

        TextView title , number;
        ImageView imageView;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Viewholder viewholder;

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {

            viewholder = new Viewholder();
            convertView = inflater.inflate(R.layout.albums_items, null);

            viewholder.number = (TextView) convertView.findViewById(R.id.no);
            viewholder.title = (TextView) convertView.findViewById(R.id.text1);
            viewholder.imageView =  convertView.findViewById(R.id.image1);

            convertView.setTag(viewholder);

        } else {

            viewholder = (Viewholder) convertView.getTag();
        }


        viewholder.number.setText("+"+modelClasses.get(position).getNo_of_images());
        viewholder.title.setText(modelClasses.get(position).getTitle());
        Glide.with(convertView).load(Url.BaseUrl+modelClasses.get(position).getImage())
                .into(viewholder.imageView);



        return convertView;
    }
}
