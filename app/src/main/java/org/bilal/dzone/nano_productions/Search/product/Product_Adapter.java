package org.bilal.dzone.nano_productions.Search.product;

/**
 * Created by DzonE on 01-Aug-18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bilal.dzone.nano_productions.Personal_Zone.Products.SingleProduct.Single_Product;
import org.bilal.dzone.nano_productions.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by  techsolpoint.com on 3/18/2018.
 */

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.RvViewHolder>{

    Context context;
    ArrayList<Product_Model> modelClasses;


    public Product_Adapter(Context context, ArrayList<Product_Model> modelClasses){
        this.context = context;
        this.modelClasses = modelClasses;
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }


    View view;
    @Override
    public Product_Adapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.product_list_items ,parent,false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }


    public class RvViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;
        TextView title_, edition_, feature_, information;
        ImageView pic;
        Button btn_details;


        public RvViewHolder(View itemView) {
            super(itemView);

            title_ = itemView.findViewById(R.id.ptitle);
            edition_ = itemView.findViewById(R.id.edition);
            pic =  itemView.findViewById(R.id.pimage);
            feature_ = itemView.findViewById(R.id.features);
            information =  itemView.findViewById(R.id.description);
            btn_details = itemView.findViewById(R.id.details);
        }
    }



    @Override
    public void onBindViewHolder(final Product_Adapter.RvViewHolder holder, final int position) {
        final Product_Model modelClass = modelClasses.get(position);

        holder.title_.setText(modelClass.getTitle());
        holder.edition_.setText(modelClass.getEdition());


        //splitting data
        String complete_String = modelClass.getFeatures();
        ArrayList<String> all_features = new ArrayList<String>();
        int itemCount = complete_String.split(",").length;
        StringTokenizer stringTokenizer = new StringTokenizer(complete_String, ",");

        //add all separated strings to arraylist
        for (int i=0; i<itemCount; i++){
            String feature = stringTokenizer.nextToken();
            all_features.add(feature);
        }   //get number of separator in a string


        //add string 1 by 1 to textview with line separator
        holder.feature_.setText("->"+" "+all_features.get(0));
        for (int i=1; i<all_features.size(); i++){
//            p_zone.append(System.getProperty("line.separator"));
            holder.feature_.append("\n");
            holder.feature_.append("->"+" "+all_features.get(i));

        }


        holder.information.setText(modelClass.getInformation());

        String imgUrl = modelClass.getImage();
        Glide.with(context)
                .load("http://techeasesol.com/nano_production/"+imgUrl)
                .apply(new RequestOptions().placeholder(R.drawable.loading_small).error(R.drawable.ic_error))
                .into(holder.pic);



        holder.btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,Single_Product.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", modelClass.getTitle());
                intent.putExtra("edition", modelClass.getEdition());
                intent.putExtra("features", modelClass.getFeatures());
                intent.putExtra("information", modelClass.getInformation());
                intent.putExtra("safety", modelClass.getSafety());
                intent.putExtra("use", modelClass.getUse());
                intent.putExtra("image", modelClass.getImage());
                intent.putExtra("technical", modelClass.getTechnical_data());
                context.startActivity(intent);

//                Toast.makeText(context, "details"+modelClass.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}