package org.bilal.dzone.nano_productions.Gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Gallery extends AppCompatActivity {

    ArrayList<GalleryAdapter> model_classArrayList = new ArrayList<GalleryAdapter>();
    GridView gridView;
    AwesomeProgressDialog awesomeInfoDialog;
    ImageView back;
    PhotoView photoView;
    int visible = 0, pos;
    String id, unSplittedImages = "";
    ImageView left, right;
    int max;
    ArrayList<String> ListSplittedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        photoView = findViewById(R.id.photo_view);
        gridView = findViewById(R.id.gridView);
        back = findViewById(R.id.imageView);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);


        ListSplittedImages = new ArrayList<String>();


        unSplittedImages = getIntent().getStringExtra("unSplittedImages");
        Log.e("unSplittedImages", unSplittedImages);


        StringTokenizer stringTokenizer = new StringTokenizer(unSplittedImages, ",");

        while (stringTokenizer.hasMoreTokens()) {

            ListSplittedImages.add(stringTokenizer.nextToken());
        }

        GridViewAdapter gridViewAdapter = new GridViewAdapter(Gallery.this, ListSplittedImages);
        gridView.setAdapter(gridViewAdapter);

        max = ListSplittedImages.size();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                visible = 1;
                gridView.setVisibility(View.GONE);
                photoView.setVisibility(View.VISIBLE);
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);


                Glide.with(Gallery.this)
                        .load(Url.BaseUrl + ListSplittedImages.get(position) + "")
                        .apply(new RequestOptions().placeholder(R.drawable.progress).error(R.drawable.ic_error))
                        .into(photoView);

                pos = position;

                btnClicks();

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible == 1) {
                    pos = 0;
                    gridView.setVisibility(View.VISIBLE);
                    left.setVisibility(View.GONE);
                    right.setVisibility(View.GONE);
                    photoView.setVisibility(View.GONE);
                    visible = 0;
                } else {
                    Gallery.this.finish();
                }
            }
        });


//        awesomeInfoDialog = new AwesomeProgressDialog(Gallery.this);
//        awesomeInfoDialog.setTitle("Loading!");
//        awesomeInfoDialog.setMessage("Please Wait..");
//        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
//        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
//        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
//        awesomeInfoDialog.setCancelable(false);
//        awesomeInfoDialog.show();


        if (new Check_internet_connection(Gallery.this).isNetworkAvailable()) {

//            getServerData();

        } else {

            awesomeInfoDialog.hide();
            Toast.makeText(Gallery.this,
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    ArrayList<String> strings = new ArrayList<String>();

    private void getServerData() {
        String urlGetServerData = Url.BaseUrl + "images-gallery/" + id;
        System.out.print(urlGetServerData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        awesomeInfoDialog.hide();
                        Log.e("response", response + "");
                        try {

                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONArray("images");

                            //get single object
                            String kss = response.getString("success");
                            Log.e("kss", kss + "");


                            for (int p = 0; p < jsonArray.length(); p++) {
                                kss = jsonArray.getString(p);
                                Log.e("string", kss);

                                strings.add(kss);

//                                JSONObject jsonObject = jsonArray.getJSONObject(p);
//                                GalleryAdapter rvdata = gson.fromJson(String.valueOf(kss), GalleryAdapter.class);
//                                model_classArrayList.add(rvdata);

                            }

                            max = strings.size();

                            GridViewAdapter gridViewAdapter = new GridViewAdapter(Gallery.this, ListSplittedImages);
                            gridView.setAdapter(gridViewAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        awesomeInfoDialog.hide();
                        System.out.println(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }


    public void btnClicks() {


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = pos - 1;

                if (pos >= 0) {

                    Glide.with(Gallery.this)
                            .load(Url.BaseUrl + ListSplittedImages.get(pos) + "")
                            .apply(new RequestOptions().placeholder(R.drawable.progress).error(R.drawable.ic_error))
                            .into(photoView);

//                    Toast.makeText(Gallery.this, pos+"", Toast.LENGTH_SHORT).show();

                } else
                    pos = pos + 1;
//                    Toast.makeText(Gallery.this, "No More Images", Toast.LENGTH_SHORT).show();
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = pos + 1;

                if (pos < max) {

                    Glide.with(Gallery.this)
                            .load(Url.BaseUrl + ListSplittedImages.get(pos) + "")
                            .apply(new RequestOptions().placeholder(R.drawable.progress).error(R.drawable.ic_error))
                            .into(photoView);

//                    Toast.makeText(Gallery.this, pos+"", Toast.LENGTH_SHORT).show();
                } else {
                    pos = pos - 1;
//                    Toast.makeText(Gallery.this, "No More Images", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {

        if (visible == 1) {
            pos = 0;
            gridView.setVisibility(View.VISIBLE);
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
            photoView.setVisibility(View.GONE);
            visible = 0;
        } else {
            Gallery.this.finish();
        }
    }

}
