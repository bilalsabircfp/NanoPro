package org.bilal.dzone.nano_productions.Personal_Zone.Importer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Importer_Zone extends Fragment {

    String newSubs;
    AwesomeProgressDialog awesomeInfoDialog;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String server_check;
    String[] name,
            email,
            phone_number,
            detailer_id,
            latitude,
            longitude,
            address, detailer_subscriptions, used_subscriptions;
    String remainingSubscriptions;
    String detailerSubscriptions = "", api_token, importerID, used_subs = "";
    ListView listView;
    TextView subscriptions;
    String no_array = "";
    Button enlarge;
    FloatingActionButton add;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView back;
    EditText etSearch;
    Importer_Adapter adapter;
    ImporterModelClass importerModelClass;
    ArrayList<ImporterModelClass> detailerArrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        importerID = sharedPreferences.getString("id", "");

        View v = inflater.inflate(R.layout.activity_importer__zone, container, false);

        Log.e("Fragment", "importerFragment");

        listView = v.findViewById(R.id.list);
        subscriptions = v.findViewById(R.id.subscriptions);
        add = v.findViewById(R.id.add);
        enlarge = v.findViewById(R.id.enlarge);
        swipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        back = v.findViewById(R.id.imageView);
        back.setVisibility(View.INVISIBLE);
        etSearch = v.findViewById(R.id.et_search);


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

//                Intent intent = new Intent(getActivity(), Edit_Customer.class);
//                intent.putExtra("name", name[position]);
//                intent.putExtra("phone_number", phone_number[position]);
//                intent.putExtra("done_date", done_date[position]);
//                intent.putExtra("model", model[position]);
//                intent.putExtra("year", year[position]);
//                intent.putExtra("color", color[position]);
//                intent.putExtra("title", title[position]);
//                intent.putExtra("edition", edition[position]);
//                intent.putExtra("email", email[position]);
//                intent.putExtra("warranty_code", warranty_code[position]);
//                intent.putExtra("license_plate_no", license_plate_no[position]);
//                startActivity(intent);
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
                    Intent intent = new Intent(getActivity(), Add_Detailer.class);
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

        if (id == 999111) {

            make_call(phone_number[position]);

        }
        else if (id == 999222) {

            SubsDialog( name[position],
                     email[position],
                      phone_number[position],
                     detailer_id[position],
                     latitude[position],
                      longitude[position],
                      address[position], detailer_subscriptions[position]);



        }


    }


    String server_response = "0", server_message;

    //server call
    private void Load_data() {
        String urlGetServerData = Url.BaseUrl + "user/importer_detailer";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("api_token", api_token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {

                    server_response = response.getString("success");
                    server_message = response.getString("message");

                    if (server_message.equals("No Record Found")){
                        Toast.makeText(getActivity(), server_message, Toast.LENGTH_SHORT).show();
                    }
                    else {

                        jsonObj = new JSONObject(response.toString());
                        Log.e("JObject", response.toString());

                        try {

                            jsonArray = jsonObj.getJSONArray("detailers");
                        } catch (JSONException e) {
                            no_array = "no_array";
                            Log.e("Error", "no array");
                        }


                        server_response = jsonObj.getString("success");

                        Log.e("kka", server_response);

                        JSONObject c;


                        name = new String[(jsonArray.length())];
                        phone_number = new String[(jsonArray.length())];
                        detailer_id = new String[(jsonArray.length())];
                        latitude = new String[(jsonArray.length())];
                        longitude = new String[(jsonArray.length())];
                        address = new String[(jsonArray.length())];
                        email = new String[(jsonArray.length())];
                        used_subscriptions = new String[(jsonArray.length())];
                        detailer_subscriptions = new String[(jsonArray.length())];


                        JSONObject StringObj = jsonObj.getJSONObject("subscriptions");

                        String sub;
                        sub = jsonObj.getString("subscriptions");
                        if (sub.equals("No Records Found")) {
                            Toast.makeText(getActivity(), "No record", Toast.LENGTH_SHORT).show();
                        }

                        remainingSubscriptions = StringObj.getString("remaining_subscriptions");
                        detailerSubscriptions = StringObj.getString("importer_subscriptions");
                        used_subs = StringObj.getString("used_subscriptions");

                        Log.e("subscriptions", remainingSubscriptions + "\n" + detailerSubscriptions);


                        if (server_response.equals("ture")) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                c = jsonArray.getJSONObject(i);

                                //news feed array
                                if (c.length() > 0) {

                                    name[i] = c.getString("name");
                                    phone_number[i] = c.getString("phone_number");
                                    email[i] = c.getString("email");
                                    detailer_id[i] = c.getString("detailer_id");
                                    latitude[i] = c.getString("latitude");
                                    longitude[i] = c.getString("longitude");
                                    address[i] = c.getString("address");
                                    used_subscriptions[i] = c.getString("used_subscriptions");
                                    detailer_subscriptions[i] = c.getString("detailer_subscriptions");

                                    Log.e("array1", name[i] + "\n" + phone_number[i]
                                    );

                                }

                            }


                            detailerArrayList.clear();

                            //setting values to array-list
                            for (int k = 0; k < name.length; k++) {
                                importerModelClass = new ImporterModelClass(name[k],
                                        email[k],
                                        phone_number[k],
                                        detailer_id[k],
                                        latitude[k],
                                        longitude[k],
                                        address[k], detailer_subscriptions[k],
                                        used_subscriptions[k]);
                                importerModelClass.setName(name[k]);
                                importerModelClass.setEmail(email[k]);
                                importerModelClass.setPhone_number(phone_number[k]);
                                importerModelClass.setDetailer_id(detailer_id[k]);
                                importerModelClass.setLatitude(latitude[k]);
                                importerModelClass.setLongitude(longitude[k]);
                                importerModelClass.setAddress(address[k]);
                                importerModelClass.setDetailer_subscriptions(detailer_subscriptions[k]);
                                importerModelClass.setUsed_subscriptions(used_subscriptions[k]);

                                detailerArrayList.add(importerModelClass);
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


                    if (server_response.equals("ture")) {

                        awesomeInfoDialog.hide();


                        subscriptions.setText("Subscriptions" + " " + used_subs + " " + "/"
                                + " " + detailerSubscriptions);

                        adapter = new Importer_Adapter(getActivity()
                                , detailerArrayList);
                        listView.setAdapter(adapter);

                        Log.e("listSize", detailerArrayList.size()+"");

                        // Add Text Change Listener to EditText
                        etSearch.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Call back the Adapter with current character to Filter
                                adapter.getFilter().filter(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });


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




    public void SendData(String name,
                         String email,
                         String  phone_number,
                         String detailer_id,
                         String latitude,
                         String  longitude,
                         String  address, String detailerSubscriptions) {

        String urlGetServerData = Url.BaseUrl+"user/edit_detailer";
        System.out.print(urlGetServerData);

        int dSubscriptions = Integer.parseInt(detailerSubscriptions) + Integer.parseInt(newSubs);


        Map<String, String> obj = new HashMap<String, String>();

        obj.put("name", name);
        obj.put("email", email);
        obj.put("ph_no", phone_number);
        obj.put("lat", latitude);
        obj.put("log", longitude);
        obj.put("address", address);
        obj.put("subscription", dSubscriptions+"");
        obj.put("api_token", api_token);
        obj.put("id", detailer_id);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(obj),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        awesomeInfoDialog.hide();
                        Log.e("response", response + "");
                        try {
                            Gson gson = new Gson();

                            //get single object
                            String kss = response.getString("success");
                            Log.e("kss", kss + "");

                            Toast.makeText(getActivity(),
                                    "Subscriptions Added", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                            Load_data();



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
        TextView link1, link2, link3;
        call = dialog.findViewById(R.id.btn);
        link1 = dialog.findViewById(R.id.link1);
        link2 = dialog.findViewById(R.id.link2);
        link3 = dialog.findViewById(R.id.link3);

        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.NANOPRO-Group.com/subscriptions"));
                startActivity(browserIntent);
            }
        });


        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.NANOPRO-Group.com/importers"));
                startActivity(browserIntent);
            }
        });


        link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@NANOPRO-Group.com" });
                startActivity(Intent.createChooser(intent, ""));
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                make_call("7515101");
                dialog.dismiss();
            }
        });





        dialog.show();
    }



    public void SubsDialog(final String name,
                           final String email,
                           final String  phone_number,
                           final String detailer_id,
                           final String latitude,
                           final String  longitude,
                           final String  address, final String  detailerSubscriptions) {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_subscription);

        Button cross, submit; final EditText subs_;
        subs_ = dialog.findViewById(R.id.subs);
        submit = dialog.findViewById(R.id.btn);
        TextView TVname = dialog.findViewById(R.id.name);
        TVname.setText(name);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newSubs = subs_.getText().toString().trim();

                Log.e("subs", newSubs + "/" + remainingSubscriptions
                        + "/" + detailerSubscriptions);

                if (newSubs.equals("") || newSubs.equals("0")) {

                    Toast.makeText(getActivity(), "Cannot Be Empty", Toast.LENGTH_SHORT).show();

                }
                else if (Integer.parseInt(newSubs) > Integer.parseInt(remainingSubscriptions) ){

                    Toast.makeText(getActivity(), "You have"+" "+remainingSubscriptions +
                         " "+   "Subscriptions Remaining", Toast.LENGTH_SHORT).show();
                }
                else {

                    awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
                    awesomeInfoDialog.setTitle("Adding Subscriptions..");
                    awesomeInfoDialog.setMessage("Please Wait..");
                    awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                    awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                    awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                    awesomeInfoDialog.setCancelable(false);
                    awesomeInfoDialog.show();

                    SendData(name,
                            email,
                            phone_number,
                            detailer_id,
                            latitude,
                            longitude,
                            address, detailerSubscriptions);
                }

            }
        });




        dialog.show();
    }


    public void filter(String text) {
        ArrayList<ImporterModelClass> complaintsListCopy = new ArrayList<ImporterModelClass>
                (detailerArrayList);
        detailerArrayList.clear();
        if (text.isEmpty()) {
            detailerArrayList.addAll(complaintsListCopy);
        } else {
            text = text.toLowerCase();
            for (ImporterModelClass item : complaintsListCopy) {
                if (item.getName().toLowerCase().contains(text)) {
                    detailerArrayList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }




    @Override
    public void onResume() {
        super.onResume();

        if (!used_subs.equals("")) {
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
    }


}
