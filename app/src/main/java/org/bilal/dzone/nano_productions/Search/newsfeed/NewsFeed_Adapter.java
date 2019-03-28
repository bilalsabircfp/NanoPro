package org.bilal.dzone.nano_productions.Search.newsfeed;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.FullScreen;
import org.bilal.dzone.nano_productions.Utils.Url;

import java.util.ArrayList;

/**
 * Created by  techsolpoint.com on 3/18/2018.
 */

public class NewsFeed_Adapter extends RecyclerView.Adapter<NewsFeed_Adapter.RvViewHolder>{

    Context context;
    ArrayList<NewsFeed_Model> modelClasses;


    public NewsFeed_Adapter(Context context, ArrayList<NewsFeed_Model> modelClasses){
        this.context = context;
        this.modelClasses = modelClasses;
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    View view;
    @Override
    public NewsFeed_Adapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.newsfeed_listview_items2 ,parent,false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }


    public class RvViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        TextView name, post, time, title, role;
        ImageView pic;


        public RvViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            post = itemView.findViewById(R.id.post);
            pic =  itemView.findViewById(R.id.profile_image);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            role = itemView.findViewById(R.id.role);
            llItem = itemView.findViewById(R.id.ll);

        }
    }



    @Override
    public void onBindViewHolder(final NewsFeed_Adapter.RvViewHolder holder, final int position) {
        final NewsFeed_Model modelClass = modelClasses.get(position);

        holder.time.setVisibility(View.GONE);

        holder.name.setText(modelClass.getTitle());
        holder.post.setText(modelClass.getContent());
        holder.title.setText(modelClass.getName());
        holder.role.setText(modelClass.getRole());

        String imgUrl = modelClass.getImage();
        Glide.with(context)
                .load(Url.BaseUrl + imgUrl)
                .into(holder.pic);



        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FullScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", modelClass.getName());
                intent.putExtra("content", modelClass.getContent());
                intent.putExtra("image",  modelClass.getImage());
                intent.putExtra("name", modelClass.getTitle());
                intent.putExtra("role",  modelClass.getRole());
                context.startActivity(intent);

//                Toast.makeText(context, modelClass.getName(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}