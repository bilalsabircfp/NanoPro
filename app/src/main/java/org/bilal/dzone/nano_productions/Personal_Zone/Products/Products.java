package org.bilal.dzone.nano_productions.Personal_Zone.Products;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Products extends AppCompatActivity {

    ArrayList<Model_Class> model_classArrayList = new ArrayList<Model_Class>();
    RecyclerView recyclerView;
    Adapter_Class adapter_class;
    ImageView back;
    AwesomeProgressDialog awesomeInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        back = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



        if (new Check_internet_connection(Products.this).isNetworkAvailable()) {

            awesomeInfoDialog = new AwesomeProgressDialog(Products.this);
            awesomeInfoDialog.setTitle("Loading!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();
            getServerData();

        } else {

            Toast.makeText(Products.this,
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Products.this.finish();
            }
        });


        //item click listner for recycler item
        //on item click listner
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
//                recyclerView, new ClickListener() {
//
//            @Override
//            public void onClick(View view, int position) {
//
//                Intent intent = new Intent(Products.this, Single_Product.class);
//                startActivity(intent);
//                Toast.makeText(Products.this, model_classArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                Toast.makeText(Products.this, model_classArrayList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        }));


    }

    //server call
    public void getServerData() {
        String urlGetServerData = Url.BaseUrl+ "product/show";
        System.out.print(urlGetServerData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response + "");
                        awesomeInfoDialog.hide();
                        try {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONArray("products");

                            //get single object
                            String kss = response.getString("success");
                            Log.e("kss", kss + "");

                            for (int p = 0; p < jsonArray.length(); p++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                Model_Class rvdata = gson.fromJson(String.valueOf(jsonObject), Model_Class.class);
                                model_classArrayList.add(rvdata);

                            }

                            adapter_class = new Adapter_Class(getApplicationContext(), model_classArrayList);
                            recyclerView.setAdapter(adapter_class);

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
}



/////////////////////////////////////////////
    //on row click listner
    interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

/////////////////////////////////////////////////