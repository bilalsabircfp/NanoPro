package org.bilal.dzone.nano_productions.Search.gallery;

/**
 * Created by DzonE on 01-Aug-18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.Gallery.Gallery;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;

import java.util.ArrayList;

/**
 * Created by  techsolpoint.com on 3/18/2018.
 */

public class Gallery_Adapter extends RecyclerView.Adapter<Gallery_Adapter.RvViewHolder>{

    Context context;
    ArrayList<Gallery_Model> modelClasses;


    public Gallery_Adapter(Context context, ArrayList<Gallery_Model> modelClasses){
        this.context = context;
        this.modelClasses = modelClasses;
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    View view;
    @Override
    public Gallery_Adapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.search_gallery_items ,parent,false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }


    public class RvViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        ImageView pic;


        public RvViewHolder(View itemView) {
            super(itemView);


            pic =  itemView.findViewById(R.id.img);
            llItem = itemView.findViewById(R.id.ll);

        }
    }



    @Override
    public void onBindViewHolder(final Gallery_Adapter.RvViewHolder holder, final int position) {
        final Gallery_Model modelClass = modelClasses.get(position);


        String imgUrl = modelClass.getImage();
        Glide.with(context)
                .load(Url.BaseUrl + imgUrl)
                .into(holder.pic);



        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Gallery.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("unSplittedImages", modelClass.getGallery_images());
//                intent.putExtra("image", "http://techeasesol.com/nano_production/"+modelClass.getImage());
                context.startActivity(intent);

//                Toast.makeText(context, modelClass.getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}