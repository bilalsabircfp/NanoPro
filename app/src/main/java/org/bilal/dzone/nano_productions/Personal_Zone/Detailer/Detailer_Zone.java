package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.Login.Login_Activity;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.Customer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Products.Adapter_Class;
import org.bilal.dzone.nano_productions.Personal_Zone.Products.Model_Class;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.URL.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.bilal.dzone.nano_productions.json.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Detailer_Zone extends Fragment {

    AwesomeProgressDialog awesomeInfoDialog;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String server_check;
    String[] name, phone_number, done_date, model, year, color;
    String remainingSubscriptions;
    String detailerSubscriptions="", api_token, detailer_id, used_subs="";
    ListView listView;
    TextView subscriptions;
    String no_array = "", warranty_code, warranty_code_id;
    Button enlarge;
    FloatingActionButton add;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");

        View v = inflater.inflate(R.layout.activity_detailer__zone, container, false);

        listView = v.findViewById(R.id.list);
        subscriptions = v.findViewById(R.id.subscriptions);
        add = v.findViewById(R.id.add);
        enlarge = v.findViewById(R.id.enlarge);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        back = v.findViewById(R.id.imageView);
        back.setVisibility(View.INVISIBLE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listview_click(id, position);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

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
                    swipeRefreshLayout.setRefreshing(false);

                } else {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (used_subs.equals(detailerSubscriptions)) {

                    Toast.makeText(getActivity(), "Buy Subscriptions To Continue", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(getActivity(), Add_Customer.class);
                    startActivity(intent);
                }

            }
        });


        enlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog();
            }
        });


        return v;
    }


    //listView.setOnItemClickListener handled here///////////////////////////
    public void listview_click(long id, int position) {

        if (id == 1) {

            make_call(phone_number[position]);

        }


    }


    String server_response = "0", server_response_text;



    //server call
    private void Load_data() {
        String urlGetServerData = Url.BaseUrl+"user/applications-done";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("api_token", api_token);
        postParam.put("detailer_id", detailer_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {

                    server_response = response.getString("success");

                    jsonObj = new JSONObject(response.toString());
                    Log.e("JObject", response.toString());

                    try {

                        jsonArray = jsonObj.getJSONArray("applications_done");
                    } catch (JSONException e) {
                        no_array = "no_array";
                        Log.e("Error", "no array");
                    }


                    server_response = jsonObj.getString("success");

                    Log.e("kka", server_response);

                    JSONObject c;


                    name = new String[(jsonArray.length())];
                    phone_number = new String[(jsonArray.length())];
                    done_date = new String[(jsonArray.length())];
                    model = new String[(jsonArray.length())];
                    year = new String[(jsonArray.length())];
                    color = new String[(jsonArray.length())];


                    JSONObject StringObj = jsonObj.getJSONObject("subscriptions");

                    String sub;
                    sub = jsonObj.getString("subscriptions");
                    if (sub.equals("No Records Found")) {
                        Toast.makeText(getActivity(), "No record", Toast.LENGTH_SHORT).show();
                    }

                    remainingSubscriptions = StringObj.getString("remaining_subscriptions");
                    detailerSubscriptions = StringObj.getString("detailer_subscriptions");
                    used_subs = StringObj.getString("used_subscriptions");

                    Log.e("subscriptions", remainingSubscriptions + "\n" + detailerSubscriptions);


                    if (server_response.equals("true")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            c = jsonArray.getJSONObject(i);

                            //news feed array
                            if (c.length() > 0) {

                                name[i] = c.getString("name");
                                phone_number[i] = c.getString("phone_number");
                                model[i] = c.getString("model");
                                year[i] = c.getString("year");
                                color[i] = c.getString("color");
                                done_date[i] = c.getString("done_date");

                                Log.e("array1", name[i] + "\n" + phone_number[i] + "\n" +
                                        model[i] + "\n" + year[i]);

                            }

                        }


                    }


                    server_check = "true";




                } catch (JSONException e) {
                    e.printStackTrace();
                    server_check = "false";
                }



                //check if server is ok
                if (server_check.equals("false")) {

                    awesomeInfoDialog.hide();

                    if (no_array.equals("") || no_array.equals("no_array")) {

                        new AwesomeErrorDialog(getActivity())
                                .setTitle("Empty List!")
                                .setMessage("No Customer Record Found.")
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

                        new AwesomeErrorDialog(getActivity())
                                .setTitle("Server Error!")
                                .setMessage("No Server Response.")
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

                } else {


                    if (server_response.equals("true")) {

                        awesomeInfoDialog.hide();


                        subscriptions.setText("Subscriptions" + " " + used_subs + " " + "/"
                                + " " + detailerSubscriptions);

                        final Detailer_Adapter adapter = new Detailer_Adapter(getActivity()
                                , name, phone_number, done_date, model, year, color);
                        listView.setAdapter(adapter);


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







    public void make_call(String number) {

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
            callIntent.setData(Uri.parse("tel:" + number));

            startActivity(callIntent);
        }

    }


    Dialog dialog;

    public void dialog() {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_enlarge);

        Button cross, call;
        cross = dialog.findViewById(R.id.cross);
        call = dialog.findViewById(R.id.btn);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                make_call("7515101");
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
