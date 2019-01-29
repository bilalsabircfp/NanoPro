package org.bilal.dzone.nano_productions.Personal_Zone.Products.SingleProduct;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bilal.dzone.nano_productions.R;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Single_Product extends AppCompatActivity {

    String title, edition, features, information, safety, use, technical, image;
    TextView title_, edition_, features_, tech_data, information_;
    Button btn_info, btn_how_to, btn_safety;
    ImageView imageView, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single__product);

        title = getIntent().getStringExtra("title");
        edition = getIntent().getStringExtra("edition");
        features = getIntent().getStringExtra("features");
        information = getIntent().getStringExtra("information");
        safety = getIntent().getStringExtra("safety");
        use = getIntent().getStringExtra("use");
        technical = getIntent().getStringExtra("technical");
        image = getIntent().getStringExtra("image");

        back =findViewById(R.id.imageView);
        title_ = findViewById(R.id.ptitle);
        edition_ = findViewById(R.id.edition);
        features_ = findViewById(R.id.features);
        tech_data = findViewById(R.id.tech_data);
        information_ = findViewById(R.id.more_info);
        btn_how_to = findViewById(R.id.btn_how);
        btn_info = findViewById(R.id.btn_info);
        btn_safety = findViewById(R.id.btn_safety);
        imageView = findViewById(R.id.pimage);


        title_.setText(title);
        edition_.setText(edition);

        ////////
        //splitting data
        String complete_String = (features);
        ArrayList<String> all_features = new ArrayList<String>();
        int itemCount = complete_String.split(",").length;
        StringTokenizer stringTokenizer = new StringTokenizer(complete_String, ",");

        //add all separated strings to arraylist
        for (int i=0; i<itemCount; i++){
            String feature = stringTokenizer.nextToken();
            all_features.add(feature);
        }   //get number of separator in a string


        //add string 1 by 1 to textview with line separator
        features_.setText("->"+" "+all_features.get(0));
        for (int i=1; i<all_features.size(); i++){
//            p_zone.append(System.getProperty("line.separator"));
            features_.append("\n");
            features_.append("->"+" "+all_features.get(i));

        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Single_Product.this.finish();
            }
        });

        //////

        information_.setText(information);

        ////////
        //splitting data
        String complete_String1 = (technical);
        ArrayList<String> all_features1 = new ArrayList<String>();
        int itemCount1 = complete_String1.split(",").length;
        StringTokenizer stringTokenizer1 = new StringTokenizer(complete_String1, ",");

        //add all separated strings to arraylist
        for (int i=0; i<itemCount1; i++){
            String technical = stringTokenizer1.nextToken();
            all_features1.add(technical);
        }   //get number of separator in a string


        //add string 1 by 1 to textview with line separator
        tech_data.setText("->"+" "+all_features1.get(0));
        for (int i=1; i<all_features1.size(); i++){
//            p_zone.append(System.getProperty("line.separator"));
            tech_data.append("\n");
            tech_data.append("->"+" "+all_features1.get(i));

        }


        //////



        Glide.with(this).load("http://www.nano.nanopro-group.com/"+image)
                .apply(new RequestOptions().placeholder(R.drawable.loading_small).error(R.drawable.ic_error))
                .into(imageView);

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_safety.setBackgroundResource(R.drawable.sliver_app_light_sharp);
                btn_how_to.setBackgroundResource(R.drawable.sliver_app_light_sharp);
                btn_info.setBackgroundResource(R.drawable.sliver_app_light_sharp_light);
                information_.setText(information);
            }
        });


        btn_safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_safety.setBackgroundResource(R.drawable.sliver_app_light_sharp_light);
                btn_how_to.setBackgroundResource(R.drawable.sliver_app_light_sharp);
                btn_info.setBackgroundResource(R.drawable.sliver_app_light_sharp);
                information_.setText(safety);
            }
        });


        btn_how_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_safety.setBackgroundResource(R.drawable.sliver_app_light_sharp);
                btn_how_to.setBackgroundResource(R.drawable.sliver_app_light_sharp_light);
                btn_info.setBackgroundResource(R.drawable.sliver_app_light_sharp);

                information_.setText(use);
            }
        });



    }
}
