package org.bilal.dzone.nano_productions.Personal_Zone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;

public class FullImage extends AppCompatActivity {

    ImageView back;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        back = findViewById(R.id.imageView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullImage.this.finish();
            }
        });

        url = getIntent().getStringExtra("url");
//        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        PhotoView photoView = findViewById(R.id.photo_view);

        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.ic_image).error(R.drawable.ic_error))
                .into(photoView);


    }
}
