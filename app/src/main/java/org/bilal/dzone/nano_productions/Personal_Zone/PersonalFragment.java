package org.bilal.dzone.nano_productions.Personal_Zone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.bilal.dzone.nano_productions.Gallery.albums.Album;
import org.bilal.dzone.nano_productions.Login.Login_Activity;
import org.bilal.dzone.nano_productions.Login.Login_Fragment;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.Customer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Detailer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Importer.Importer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Products.Products;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.FullScreen;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.bilal.dzone.nano_productions.json.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PersonalFragment extends Fragment {

    NonScrollListView nonScrollListView;
    TextView p_zone;
    AwesomeProgressDialog awesomeInfoDialog;
    JSONObject jsonObj;
    JSONArray jsonArray, jsonArray2;
    String server_check, user_type;
    String[] name_, image, content, time, title, role;
    TextView productTxt, galleryTxt;
    ImageView productImage, galleryImage, back;
    final List<String[]> values = new LinkedList<String[]>();
    RelativeLayout cars;
    LinearLayout img1;
    Intent intent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        String p_z = sharedPreferences.getString("name", "");
        user_type = sharedPreferences.getString("user_type", "");

        View v = inflater.inflate(R.layout.car_details, container, false);

        nonScrollListView = v.findViewById(R.id.lv_nonscroll_list);
        cars = v.findViewById(R.id.r_cars);
        p_zone = v.findViewById(R.id.pzone);
        p_zone.setFocusable(true);
        back = v.findViewById(R.id.imageView);
        img1 = v.findViewById(R.id.img1);
        productImage = v.findViewById(R.id.pimage);
        galleryImage = v.findViewById(R.id.gimage);
        productTxt = v.findViewById(R.id.p_txt);
        galleryTxt = v.findViewById(R.id.gtxt);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), Album.class);
                startActivity(intent);
            }
        });


        nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), FullScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", name_[position]);
                intent.putExtra("content", content[position]);
                intent.putExtra("image", image[position]);
                intent.putExtra("name", title[position]);
                intent.putExtra("role", role[position]);
                getActivity().startActivity(intent);
//                Toast.makeText(getActivity(), name_[position]+"", Toast.LENGTH_SHORT).show();
            }
        });


//        if (p_z.equals("")) {
//            p_zone.setText("Login to access Personal Zone");
//        } else {
//            p_zone.setText("Click to see Personal Zone");
//        }
        if (p_z.equals("")) {
            p_zone.setText("Login to access Personal Zone");
        } else {
            p_zone.setText("Connected as" + " " + p_z + "\n" +
                    "Click to view Personal Zone");
        }


        p_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (user_type) {
                    case "": {
                        Fragment fragment = new Login_Fragment();
                        getFragmentManager().beginTransaction().replace(R.id.content, fragment).
                                addToBackStack(null).commit();
                        Toast.makeText(getActivity(), "Login To Access Personal Zone", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case "detailer": {
                        Fragment fragment = new Detailer_Zone();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        break;
                    }
                    case "importer": {
                        Fragment fragment = new Importer_Zone();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        break;
                    }
                    case "customer": {
                        Fragment fragment = new Customer_Zone();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        break;
                    }
                }
            }
        });


        if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

            if (values.size() > 0) {
                values.clear();
            }

            awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
            awesomeInfoDialog.setTitle("Loading!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();
            Load_Data();

        } else {

            Toast.makeText(getActivity(),
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Products.class);
                values.clear();
                startActivity(intent);
            }
        });


//        //added textview , Max Lines(you want to show at normal),and view more label true so that you can expand
//        ResizableCustomView.doResizeTextView(p_zone, 3, "View More", true);


        return v;


    }


    String server_response = "0";
    JSONObject StringObj;

    //server call
    private void Load_Data() {
        String urlGetServerData = Url.BaseUrl + "personal-zone";
        System.out.print(urlGetServerData);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response + "");
                        awesomeInfoDialog.hide();

                        try {

                            String SgalleryText, SgalleryImage, SproductImage, SproductText;


                            jsonObj = new JSONObject(response.toString());
                            Log.e("JObject", response.toString());
                            jsonArray = jsonObj.getJSONArray("news_feed");
                            Log.e("jsonArray", jsonArray.toString());
//                jsonArray2 = jsonObj.getJSONArray("gallery_img");


                            //get gallery, product and newsfeed image and text
                            StringObj = jsonObj.getJSONObject("gallery_image");
                            SgalleryText = StringObj.getString("text");
                            SgalleryImage = StringObj.getString("image");

                            galleryTxt.setText(SgalleryText);
                            Glide.with(getActivity()).load(Url.BaseUrl + SgalleryImage)
                                    .into(galleryImage);


                            StringObj = jsonObj.getJSONObject("product_image");
                            SproductText = StringObj.getString("text");
                            SproductImage = StringObj.getString("image");

                            productTxt.setText(SproductText);
                            productImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            Glide.with(getActivity()).load(Url.BaseUrl + SproductImage)
                                    .into(productImage);


                            JSONObject c;

                            name_ = new String[(jsonArray.length())];
                            image = new String[(jsonArray.length())];
                            time = new String[(jsonArray.length())];
                            content = new String[(jsonArray.length())];
                            title = new String[(jsonArray.length())];
                            role = new String[(jsonArray.length())];

//                //2nd array
//                title = new String[(jsonArray2.length())];
//                image_ = new String[(jsonArray2.length())];
//                created_at = new String[(jsonArray2.length())];
//                updated_at = new String[(jsonArray2.length())];


                            server_response = jsonObj.getString("success");


                            if (server_response.equals("true")) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    c = jsonArray.getJSONObject(i);

                                    //news feed array
                                    if (c.length() > 0) {

                                        name_[i] = c.getString("name");
                                        image[i] = c.getString("image");
                                        content[i] = c.getString("content");
                                        time[i] = c.getString("time");
                                        title[i] = c.getString("title");
                                        role[i] = c.getString("role");


                                        Log.e("array1", name_[i] + "\n" + image[i] + "\n" + content[i] + "\n" +
                                                time[i]);

                                    }

                                }


                                for (int k = 0; k < name_.length; k++) {

                                    values.add(new String[]{name_[k], content[k], time[k], image[k]});
                                }


                                //fetch second array of cars
//                    for (int i = 0; i < jsonArray2.length(); i++) {
//
//                        c = jsonArray2.getJSONObject(i);
//
//                        if (c.length() > 0) {
//
//                            title[i] = c.getString("title");
//                            image_[i] = c.getString("image");
//                            created_at[i] = c.getString("created_at");
//                            updated_at[i] = c.getString("updated_at");
//
//                            Log.e("array2", title[i] + "\n" + image_[i] + "\n" + created_at[i] + "\n" +
//                                    updated_at[i]);
//
//                        }
//
//                    }


                            }


                            server_check = "true";


                        } catch (Exception e) {
                            e.printStackTrace();

                            server_check = "false";
                        }


                        //check if server is ok
                        if (server_check.equals("false")) {

                            awesomeInfoDialog.hide();

                            new AwesomeErrorDialog(getActivity())
                                    .setTitle("Server Error!")
                                    .setMessage("No Response From Server..")
                                    .setDialogBodyBackgroundColor(R.color.bottom_nav)
                                    .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                    .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                    .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                    .setButtonText(getString(R.string.dialog_ok_button))
                                    .setErrorButtonClick(new Closure() {
                                        @Override
                                        public void exec() {
                                            // click
                                        }
                                    })
                                    .show();
                        } else {


                            if (server_response.equals("true")) {

                                awesomeInfoDialog.hide();

                                //set product images and text
                                if (image.length > 0) {

//                        Glide.with(getActivity()).load(Url.BaseUrl + image_[0])
//                                .apply(new RequestOptions().placeholder(R.drawable.ic_image).error(R.drawable.ic_error))
//                                .into(image1);
//                        Glide.with(getActivity()).load(Url.BaseUrl + image_[1]).
//                                apply(new RequestOptions().placeholder(R.drawable.ic_image).error(R.drawable.ic_error))
//                                .into(image2);
//                        Glide.with(getActivity()).load(Url.BaseUrl + image_[2])
//                                .apply(new RequestOptions().placeholder(R.drawable.ic_image).error(R.drawable.ic_error))
//                                .into(image3);

//                        img_txt1.setText(title[0]);
//                        img_txt2.setText(title[1]);
//                        img_txt3.setText(title[2]);


                                    nonScrollListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                                    nonScrollListView.setFocusable(false);

                                    final NewsfeedAdapter adapter = new NewsfeedAdapter(getActivity(),
                                            title, content, image, time, name_, role);
                                    nonScrollListView.setAdapter(adapter);

                                }


                            } else {

                                awesomeInfoDialog.hide();

                                new AwesomeErrorDialog(getActivity())
                                        .setMessage("Request Timeout Slow Internet Connection!")
                                        .setTitle("Timeout!")
                                        .setDialogBodyBackgroundColor(R.color.bottom_nav)
                                        .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                        .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                        .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                        .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                        .setButtonText(getString(R.string.dialog_ok_button))
                                        .setErrorButtonClick(new Closure() {
                                            @Override
                                            public void exec() {
                                                // click
                                            }
                                        })
                                        .show();

                            }
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        awesomeInfoDialog.hide();
                        System.out.println(error.toString());

                        new AwesomeErrorDialog(getActivity())
                                .setTitle("Server Error!")
                                .setMessage("No Response From Server.")
                                .setDialogBodyBackgroundColor(R.color.bottom_nav)
                                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                                .setCancelable(true).setButtonText(getString(R.string.dialog_ok_button))
                                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                                .setButtonText(getString(R.string.dialog_ok_button))
                                .setErrorButtonClick(new Closure() {
                                    @Override
                                    public void exec() {
                                        // click
                                    }
                                })
                                .show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }


}