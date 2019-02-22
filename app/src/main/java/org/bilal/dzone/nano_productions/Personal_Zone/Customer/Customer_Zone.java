package org.bilal.dzone.nano_productions.Personal_Zone.Customer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Customer_Zone extends Fragment {

    TextView read;
    AwesomeProgressDialog awesomeInfoDialog;
    String server_check;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String api_token, customer_id, Myname, Myemail, warranty_code, phone, app_date
            ,license, model, year, color, title, edition, detailer_name, detailer_email
            ,detailer_phn_nmbr, detailer_img, address;
    TextView year_, model_, color_, plate_, myName_, myNumber_, app_date_, warranty_
        ,detailer_name_, detailer_email_, detailer_phn_nmbr_, coating;
    ImageView detailer_img_;
    Button call;
    ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        customer_id = sharedPreferences.getString("id", "");

        View v = inflater.inflate(R.layout.activity_customer__zone, container, false);


        read = v.findViewById(R.id.read);
        back = v.findViewById(R.id.imageView);
        coating = v.findViewById(R.id.coat);
        year_ = v.findViewById(R.id.year);
        model_ = v.findViewById(R.id.model);
        color_ = v.findViewById(R.id.color);
        plate_ = v.findViewById(R.id.plate);
        myName_ = v.findViewById(R.id.name);
        myNumber_ = v.findViewById(R.id.number);
        app_date_ = v.findViewById(R.id.app_date);
        warranty_ = v.findViewById(R.id.warranty_no);
        detailer_name_ = v.findViewById(R.id.detailer_name);
        detailer_phn_nmbr_ = v.findViewById(R.id.num);
        detailer_email_ = v.findViewById(R.id.address);
        detailer_img_ = v.findViewById(R.id.profile_image);
        call = v.findViewById(R.id.call);


        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.nanopro-group.com/warranty";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_call();
            }
        });


        if (new Check_internet_connection(getActivity().getApplicationContext()).isNetworkAvailable()) {

            awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
            awesomeInfoDialog.setTitle("Loading!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();
            Load_data();

        } else {

            Toast.makeText(getActivity().getApplicationContext(),
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


        return v;
    }


    String server_response = "0", server_response_text;
    //server call
    private void Load_data() {
        String urlGetServerData = Url.BaseUrl+ "user/user-details";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("api_token", api_token);
        postParam.put("customer_id", customer_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {

                    jsonObj = new JSONObject(response.toString());
                    server_response = response.getString("success");

                    if (server_response.equals("true")) {


                        JSONObject object = jsonObj.getJSONObject("user");
                        address = object.getString("addres");
                        Myname = object.getString("name");
                        Myemail = object.getString("phone_number");
                        warranty_code = object.getString("warranty_code");
                        phone = object.getString("phone_number");
                        app_date = object.getString("done_date");
                        license = object.getString("license_plate_no");
                        model = object.getString("model");
                        year = object.getString("year");
                        color = object.getString("color");
                        title = object.getString("title");
                        edition = object.getString("edition");
                        detailer_name = object.getString("detailer_name");
                        detailer_img = object.getString("detailer_img");
                        detailer_phn_nmbr = object.getString("detailer_phn_nmbr");
                        detailer_email = object.getString("detailer_email");


                        Log.e("results", Myname + "\n" + Myemail + "\n" + warranty_code);

                    }


                    server_check = "true";

                } catch (Exception e) {
                    e.printStackTrace();

                    //server response/////////////////////////
                    server_check = "false";
                }


                //check if server is ok
                if (server_check.equals("false")) {

                    awesomeInfoDialog.hide();

                    new AwesomeErrorDialog(getActivity())
                            .setTitle("Server Error!")
                            .setMessage("No Record Found.")
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

                        coating.setText("1 X " + " \t " +title + " \t " + edition);
                        year_.setText ("Year   :" + "  " +year);
                        model_.setText("Model  :" + " " +model);
                        color_.setText("Color  :" + "  " +color);
                        plate_.setText(license);
                        myName_.setText(Myname);
                        myNumber_.setText(Myemail);
                        app_date_.setText(app_date);
                        warranty_.setText(warranty_code);
                        detailer_email_.setText(address);
                        detailer_phn_nmbr_.setText(detailer_phn_nmbr);
                        detailer_name_.setText(detailer_name);
                        Glide.with(getActivity()).load(Url.BaseUrl+detailer_img).into(detailer_img_);


                    } else {

                        awesomeInfoDialog.hide();

                        new AwesomeErrorDialog(getActivity())
                                .setTitle("Timeout!")
                                .setDialogBodyBackgroundColor(R.color.bottom_nav)
                                .setMessage("Server Timeout..")
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



    public void make_call(){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            // permission has been granted, continue as usual
//            Toast.makeText(this, "accessed", Toast.LENGTH_SHORT).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ detailer_phn_nmbr));

            startActivity(callIntent);
        }

    }

}