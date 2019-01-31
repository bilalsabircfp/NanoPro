package org.bilal.dzone.nano_productions.Search;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.Map.Maps_ModelClass;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.detailers.DetailerMap_Adapter;
import org.bilal.dzone.nano_productions.Search.gallery.Gallery_Adapter;
import org.bilal.dzone.nano_productions.Search.gallery.Gallery_Model;
import org.bilal.dzone.nano_productions.Search.newsfeed.NewsFeed_Adapter;
import org.bilal.dzone.nano_productions.Search.newsfeed.NewsFeed_Model;
import org.bilal.dzone.nano_productions.Search.product.Product_Adapter;
import org.bilal.dzone.nano_productions.Search.product.Product_Model;
import org.bilal.dzone.nano_productions.URL.Url;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    ImageButton search, filter;
    NewsFeed_Adapter adapter_class;
    Product_Adapter product_adapter;
    DetailerMap_Adapter detailer_adapter;
    Gallery_Adapter gallery_adapter;
    ArrayList<NewsFeed_Model> model_classArrayList = new ArrayList<NewsFeed_Model>();
    ArrayList<Product_Model> product_classArrayList = new ArrayList<Product_Model>();
    ArrayList<Maps_ModelClass> map_classArrayList = new ArrayList<Maps_ModelClass>();
    ArrayList<Gallery_Model> gallery_classArrayList = new ArrayList<Gallery_Model>();
    AwesomeProgressDialog awesomeInfoDialog;
    String key, search_value="";
    EditText editText;
    int checked = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        filter = v.findViewById(R.id.btnfilter);
        recyclerView = v.findViewById(R.id.list);
        search = v.findViewById(R.id.btnsearch);
        editText = v.findViewById(R.id.et_search);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_value = "";
                dialog();
            }
        });



        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:


                            key = editText.getText().toString().trim();

                            if (key.equals("")) {
                                Toast.makeText(getActivity(),
                                        "Write Some Keyword to Search", Toast.LENGTH_SHORT).show();
                            }
                            else if (search_value.equals("")) {
                                Toast.makeText(getActivity(),
                                        "Select Search Category ", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                if (model_classArrayList.size() > 0 || gallery_classArrayList.size() > 0
                                        || product_classArrayList.size() > 0 || map_classArrayList.size() > 0) {
                                    model_classArrayList.clear();
                                    product_classArrayList.clear();
                                    map_classArrayList.clear();
                                    gallery_classArrayList.clear();
                                }


                                awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
                                awesomeInfoDialog.setTitle("Loading!");
                                awesomeInfoDialog.setMessage("Please Wait..");
                                awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                                awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                                awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                                awesomeInfoDialog.setCancelable(false);
                                awesomeInfoDialog.show();

                                if (search_value.equals("news_feed")){
                                    getNewsfeed();
                                }
                                else if (search_value.equals("products")){
                                    getProduct();
                                }
                                else if (search_value.equals("detailer")){
                                    getDetailers();
                                }
                                else if (search_value.equals("title")){
                                    getImage();
                                }

                            }

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                key = editText.getText().toString().trim();

                if (key.equals("")) {
                    //open keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    Toast.makeText(getActivity(),
                            "Write Some Keyword to Search", Toast.LENGTH_SHORT).show();
                }
                else if (search_value.equals("")) {
                    Toast.makeText(getActivity(),
                            "Select Search Category ", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (model_classArrayList.size() > 0 || gallery_classArrayList.size() > 0
                            || product_classArrayList.size() > 0 || map_classArrayList.size() > 0) {
                        model_classArrayList.clear();
                        product_classArrayList.clear();
                        map_classArrayList.clear();
                        gallery_classArrayList.clear();
                    }


                    awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
                    awesomeInfoDialog.setTitle("Loading!");
                    awesomeInfoDialog.setMessage("Please Wait..");
                    awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                    awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                    awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                    awesomeInfoDialog.setCancelable(false);
                    awesomeInfoDialog.show();

                    if (search_value.equals("news_feed")){
                        getNewsfeed();
                    }
                    else if (search_value.equals("products")){
                        getProduct();
                    }
                    else if (search_value.equals("detailer")){
                        getDetailers();
                    }
                    else if (search_value.equals("title")){
                        getImage();
                    }

                }
            }
        });


        return v;
    }


    //server call
    private void getNewsfeed() {
        String urlGetServerData = Url.BaseUrl+"search";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(search_value, key);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.getJSONArray("result");

                    //get single object
                    String kss = response.getString("success");
                    Log.e("kss", kss + "");

                    for (int p = 0; p < jsonArray.length(); p++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(p);
                        NewsFeed_Model rvdata = gson.fromJson(String.valueOf(jsonObject),
                                NewsFeed_Model.class);
                        model_classArrayList.add(rvdata);

                    }

                    adapter_class = new NewsFeed_Adapter(getActivity(), model_classArrayList);
                    recyclerView.setAdapter(adapter_class);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();

                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        awesomeInfoDialog.hide();
                        Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


    String response_txt;
    //server call
    private void getProduct() {
        String urlGetServerData = Url.BaseUrl+"search";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(search_value, key);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();

                try {
                    Gson gson = new Gson();

                    //get single object
                    response_txt = response.getString("success");
                    Log.e("kss", response_txt + "");


                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int p = 0; p < jsonArray.length(); p++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(p);
                        Product_Model rvdata = gson.fromJson(String.valueOf(jsonObject),
                                Product_Model.class);
                        product_classArrayList.add(rvdata);

                    }

                    product_adapter = new Product_Adapter(getActivity(), product_classArrayList);
                    recyclerView.setAdapter(product_adapter);


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        awesomeInfoDialog.hide();
                        Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }



    private void getDetailers() {
        String urlGetServerData = Url.BaseUrl+"search";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(search_value, key);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.getJSONArray("result");

                    //get single object
                    response_txt = response.getString("success");
                    Log.e("kss", response_txt + "");


                    for (int p = 0; p < jsonArray.length(); p++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(p);
                        Maps_ModelClass rvdata = gson.fromJson(String.valueOf(jsonObject),
                                Maps_ModelClass.class);
                        map_classArrayList.add(rvdata);

                    }

                    detailer_adapter = new DetailerMap_Adapter(getActivity(), map_classArrayList);
                    recyclerView.setAdapter(detailer_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                        awesomeInfoDialog.hide();
                        System.out.println(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }



    private void getImage() {
        String urlGetServerData = Url.BaseUrl+"search";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(search_value, key);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.getJSONArray("result");

                    //get single object
                    response_txt = response.getString("success");
                    Log.e("kss", response_txt + "");


                    for (int p = 0; p < jsonArray.length(); p++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(p);
                        Gallery_Model rvdata = gson.fromJson(String.valueOf(jsonObject),
                                Gallery_Model.class);
                        gallery_classArrayList.add(rvdata);

                    }

                    gallery_adapter = new Gallery_Adapter(getActivity(), gallery_classArrayList);
                    recyclerView.setAdapter(gallery_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_SHORT).show();
                        awesomeInfoDialog.hide();
                        System.out.println(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


    Dialog dialog;

    //filter dialog
    public void dialog() {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_search);

        Button cross, call;
        final CheckBox detailer, product, gallery, news;
        cross = dialog.findViewById(R.id.cross);
        call = dialog.findViewById(R.id.btn);
        detailer = dialog.findViewById(R.id.checkBox);
        gallery = dialog.findViewById(R.id.checkBox2);
        news = dialog.findViewById(R.id.news);
        product = dialog.findViewById(R.id.product);


        detailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery.setChecked(false);
                news.setChecked(false);
                product.setChecked(false);

            }
        });



        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product.setChecked(false);
                news.setChecked(false);
                detailer.setChecked(false);

            }
        });



        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery.setChecked(false);
                news.setChecked(false);
                detailer.setChecked(false);

            }
        });


        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                product.setChecked(false);
                gallery.setChecked(false);
                detailer.setChecked(false);

            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (detailer.isChecked()){
//                    Toast.makeText(getActivity(), "det", Toast.LENGTH_SHORT).show();
                    search_value = "detailer";
                    dialog.dismiss();
                }
                else if (gallery.isChecked()){
//                    Toast.makeText(getActivity(), "gal", Toast.LENGTH_SHORT).show();
                    search_value = "title";
                    dialog.dismiss();
                }
                else if (product.isChecked()){
//                    Toast.makeText(getActivity(), "pro", Toast.LENGTH_SHORT).show();
                    search_value = "products";
                    dialog.dismiss();
                }
                else if (news.isChecked()){
//                    Toast.makeText(getActivity(), "newas", Toast.LENGTH_SHORT).show();
                    search_value = "news_feed";
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Select Search Category", Toast.LENGTH_SHORT).show();
                }


            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

}