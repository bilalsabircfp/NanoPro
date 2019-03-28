package org.bilal.dzone.nano_productions.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;

public class FullScreen extends AppCompatActivity {

    String pimage, name, post, title, role;
    TextView name_, post_, title_, role_;ImageView pimage_;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        pimage = getIntent().getStringExtra("image");
        post = getIntent().getStringExtra("content");
        name = getIntent().getStringExtra("title");
        title = getIntent().getStringExtra("name");
        role = getIntent().getStringExtra("role");


        pimage_ = findViewById(R.id.profile_image);
        name_ = findViewById(R.id.name);
        post_ = findViewById(R.id.post);
        back = findViewById(R.id.imageView);
        title_ = findViewById(R.id.title);
        role_ = findViewById(R.id.role);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreen.this.finish();
            }
        });


        post_.setText(post);
        name_.setText(title);
        title_.setText(name);
        role_.setText(role);
        Glide.with(this).load(Url.BaseUrl + pimage).into(pimage_);


    }
}
