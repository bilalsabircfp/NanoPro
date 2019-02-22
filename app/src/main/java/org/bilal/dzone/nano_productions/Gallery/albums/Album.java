package org.bilal.dzone.nano_productions.Gallery.albums;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.Gallery.Gallery;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.gallery.Gallery_Model;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Album extends AppCompatActivity {

    ArrayList<Gallery_Model> model_classArrayList = new ArrayList<Gallery_Model>();
    GridView gridView;
    AwesomeProgressDialog awesomeInfoDialog;
    ImageView back;
    int visible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        gridView = findViewById(R.id.gridView);
        back = findViewById(R.id.imageView);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Album.this, Gallery.class);
                intent.putExtra("id", model_classArrayList.get(position).getId());
                intent.putExtra("unSplittedImages", model_classArrayList.get(position).getGallery_images());
                startActivity(intent);

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible == 1){

                    visible = 0;
                }
                else {
                    Album.this.finish();
                }
            }
        });


        awesomeInfoDialog = new AwesomeProgressDialog(Album.this);
        awesomeInfoDialog.setTitle("Loading!");
        awesomeInfoDialog.setMessage("Please Wait..");
        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
        awesomeInfoDialog.setCancelable(false);
        awesomeInfoDialog.show();


        if (new Check_internet_connection(Album.this).isNetworkAvailable()) {

            getServerData();

        } else {

            awesomeInfoDialog.hide();
            Toast.makeText(Album.this,
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


    }


    private void getServerData() {
        String urlGetServerData = Url.BaseUrl+ "albums";
        System.out.print(urlGetServerData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        awesomeInfoDialog.hide();
                        Log.e("response", response + "");
                        try {

                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONArray("albums");

                            //get single object
                            String kss = response.getString("success");
                            Log.e("kss", kss + "");

                            for (int p = 0; p < jsonArray.length(); p++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                Gallery_Model rvdata = gson.fromJson(String.valueOf(jsonObject), Gallery_Model.class);
                                model_classArrayList.add(rvdata);

                            }

                            Albums_Adapter gridViewAdapter = new Albums_Adapter(Album.this,model_classArrayList);
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


    @Override
    public void onBackPressed() {

        if (visible == 1){

            visible = 0;
        }
        else {
            Album.this.finish();
        }
    }
}
