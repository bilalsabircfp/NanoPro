package org.bilal.dzone.nano_productions.Search.detailers;

/**
 * Created by DzonE on 01-Aug-18.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bilal.dzone.nano_productions.Map.Maps_ModelClass;
import org.bilal.dzone.nano_productions.R;

import java.util.ArrayList;

/**
 * Created by  techsolpoint.com on 3/18/2018.
 */

public class DetailerMap_Adapter extends RecyclerView.Adapter<DetailerMap_Adapter.RvViewHolder> {

    Activity context;
    ArrayList<Maps_ModelClass> modelClasses;


    public DetailerMap_Adapter(Activity context, ArrayList<Maps_ModelClass> modelClasses) {
        this.context = context;
        this.modelClasses = modelClasses;
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    View view;

    @Override
    public DetailerMap_Adapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.search_map_items, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }


    public class RvViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        TextView name, address, number;
        ImageView pic;
        Button btn_details;


        public RvViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.email);
            pic = itemView.findViewById(R.id.profile_image);
            number = itemView.findViewById(R.id.number);
            btn_details = itemView.findViewById(R.id.call);
        }
    }


    @Override
    public void onBindViewHolder(final DetailerMap_Adapter.RvViewHolder holder, final int position) {
        final Maps_ModelClass modelClass = modelClasses.get(position);

        holder.name.setText(modelClass.getName());
        holder.address.setText(modelClass.getAddress());
        holder.number.setText(modelClass.getPhone_number());

        String imgUrl = modelClass.getImage();
        Glide.with(context)
                .load("http://techeasesol.com/nano_production/" + imgUrl)
                .apply(new RequestOptions().placeholder(R.drawable.loading_small).error(R.drawable.ic_error))
                .into(holder.pic);


        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,
//                        modelClass.getPhone_number()+"", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Check Permissions Now
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    // permission has been granted, continue as usual
//            Toast.makeText(this, "accessed", Toast.LENGTH_SHORT).show();

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + modelClass.getPhone_number() + ""));

                    context.startActivity(callIntent);
                }

            }
            });

//        holder.btn_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context,Single_Product.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("title", modelClass.getTitle());
//                intent.putExtra("edition", modelClass.getEdition());
//                intent.putExtra("features", modelClass.getFeatures());
//                intent.putExtra("information", modelClass.getInformation());
//                intent.putExtra("safety", modelClass.getSafety());
//                intent.putExtra("use", modelClass.getUse());
//                intent.putExtra("image", modelClass.getImage());
//                intent.putExtra("technical", modelClass.getTechnical_data());
//                context.startActivity(intent);
//
////                Toast.makeText(context, "details"+modelClass.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });


        }


    }