package org.bilal.dzone.nano_productions.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;

public class FullScreen extends AppCompatActivity {

    String pimage, name, post;
    TextView name_, post_;ImageView pimage_;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        pimage = getIntent().getStringExtra("image");
        post = getIntent().getStringExtra("content");
        name = getIntent().getStringExtra("title");

        pimage_ = findViewById(R.id.profile_image);
        name_ = findViewById(R.id.name);
        post_ = findViewById(R.id.post);
        back = findViewById(R.id.imageView);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreen.this.finish();
            }
        });


        post_.setText(post);
        name_.setText(name);
        Glide.with(this).load(pimage).into(pimage_);


    }
}
