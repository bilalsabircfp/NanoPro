package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Detailer_Zone extends Fragment {

    AwesomeProgressDialog awesomeInfoDialog;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String server_check;
    String[] name, phone_number, done_date, model, year, color, title, remarks,
            edition, car_details_id,
            email, cust_id, images, after_images,
            warranty_code,
            license_plate_no, coat, preparation;
    String remainingSubscriptions, addedByName, addedByEmail, addedByAddress, addedByNumber, addedByImage;
    String detailerSubscriptions = "", api_token, detailer_id, used_subs = "";
    ListView listView;
    TextView subscriptions, name_, address, number;
    String no_array = "";
    Button enlarge, call;
    FloatingActionButton add;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView back, profileImage;
    EditText etSearch;
    Detailer_Adapter adapter;
    DetailerModelClass detailerModelClass;
    ArrayList<DetailerModelClass> detailerArrayList = new ArrayList<>();


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
        etSearch = v.findViewById(R.id.et_search);
        name_ = v.findViewById(R.id.name);
        address = v.findViewById(R.id.address);
        number = v.findViewById(R.id.number);
        profileImage = v.findViewById(R.id.profile_image);
        call = v.findViewById(R.id.call);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!addedByNumber.equals("")) {
                    make_call(addedByNumber);
                }
            }
        });


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


                if (id == 999111) {

                    make_call(phone_number[position]);

                } else {

                    Intent intent = new Intent(getActivity(), Edit_Customer.class);
                    intent.putExtra("name", name[position]);
                    intent.putExtra("phone_number", phone_number[position]);
                    intent.putExtra("done_date", done_date[position]);
                    intent.putExtra("model", model[position]);
                    intent.putExtra("year", year[position]);
                    intent.putExtra("color", color[position]);
                    intent.putExtra("title", title[position]);
                    intent.putExtra("edition", edition[position]);
                    intent.putExtra("email", email[position]);
                    intent.putExtra("remarks", remarks[position]);
                    intent.putExtra("cust_id", cust_id[position]);
                    intent.putExtra("warranty_code", warranty_code[position]);
                    intent.putExtra("license_plate_no", license_plate_no[position]);
                    intent.putExtra("car_details_id", car_details_id[position]);
                    intent.putExtra("images", images[position]);
                    intent.putExtra("after_images", after_images[position]);
                    intent.putExtra("coating", coat[position]);
                    intent.putExtra("preparation", preparation[position]);
                    startActivity(intent);

                }
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


//        // Add Text Change Listener to EditText
//        etSearch.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Call back the Adapter with current character to Filter
//                adapter.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


        return v;
    }


    String server_response = "0";

    //server call
    private void Load_data() {
        String urlGetServerData = Url.BaseUrl + "user/applications-done";
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
                    title = new String[(jsonArray.length())];
                    edition = new String[(jsonArray.length())];
                    email = new String[(jsonArray.length())];
                    warranty_code = new String[(jsonArray.length())];
                    license_plate_no = new String[(jsonArray.length())];
                    remarks = new String[(jsonArray.length())];
                    cust_id = new String[(jsonArray.length())];
                    car_details_id = new String[(jsonArray.length())];
                    images = new String[(jsonArray.length())];
                    after_images = new String[(jsonArray.length())];
                    coat = new String[(jsonArray.length())];
                    preparation = new String[(jsonArray.length())];


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


                    JSONObject deatialsObj = jsonObj.getJSONObject("user_details");
                    addedByName = deatialsObj.getString("user_name");
                    addedByEmail = deatialsObj.getString("user_email");
                    addedByNumber = deatialsObj.getString("user_phn_nmbr");
                    addedByAddress = deatialsObj.getString("user_address");
                    addedByImage = deatialsObj.getString("user_img");

                    if (addedByName.equals("Admin")) {
                        address.setText("info@NANOPRO-Group.com");
                    } else {
                        address.setText(addedByEmail);
                    }

                    name_.setText(addedByName);
                    number.setText(addedByNumber);
                    if (addedByName.equals("Admin")) {
                        profileImage.setImageResource(R.drawable.logo_new);
                    } else {
                        Glide.with(getActivity()).load(Url.BaseUrl + addedByImage).into(profileImage);
                    }


                    if (server_response.equals("true")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            c = jsonArray.getJSONObject(i);

                            //news feed array
                            if (c.length() > 0) {

                                car_details_id[i] = c.getString("car_details_id");
                                remarks[i] = c.getString("remarks");
                                name[i] = c.getString("name");
                                phone_number[i] = c.getString("phone_number");
                                model[i] = c.getString("model");
                                year[i] = c.getString("year");
                                color[i] = c.getString("color");
                                done_date[i] = c.getString("done_date");
                                title[i] = c.getString("title");
                                edition[i] = c.getString("edition");
                                email[i] = c.getString("cust_email");
                                cust_id[i] = c.getString("customer_id");
                                warranty_code[i] = c.getString("warranty_code");
                                license_plate_no[i] = c.getString("license_plate_no");
                                images[i] = c.getString("before_img");
                                after_images[i] = c.getString("after_img");
                                coat[i] = c.getString("coating");
                                preparation[i] = c.getString("preparation");

                                Log.e("array1", name[i] + "\n" + phone_number[i] + "\n" +
                                        model[i] + "\n" + year[i]);

                            }

                        }


                        detailerArrayList.clear();

                        //setting values to array-list
                        for (int k = 0; k < name.length; k++) {
                            detailerModelClass = new DetailerModelClass(name[k],
                                    phone_number[k], done_date[k], model[k], year[k], color[k],
                                    remarks[k], title[k], edition[k], email[k], warranty_code[k],
                                    license_plate_no[k]);
                            detailerModelClass.setName(name[k]);
                            detailerModelClass.setPhone_number(phone_number[k]);
                            detailerModelClass.setDone_date(done_date[k]);
                            detailerModelClass.setModel(model[k]);
                            detailerModelClass.setYear(year[k]);
                            detailerModelClass.setColor(color[k]);
                            detailerModelClass.setColor(remarks[k]);
                            detailerModelClass.setTitle(title[k]);
                            detailerModelClass.setEdition(edition[k]);
                            detailerModelClass.setEmail(email[k]);
                            detailerModelClass.setWarranty_code(warranty_code[k]);
                            detailerModelClass.setLicense_plate_no(license_plate_no[k]);

                            detailerArrayList.add(detailerModelClass);
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

                        adapter = new Detailer_Adapter(getActivity()
                                , detailerArrayList);
                        listView.setAdapter(adapter);


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

        Button call;
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
                        Uri.parse("ttp://www.NANOPRO-Group.com/importers"));
                startActivity(browserIntent);
            }
        });


        link3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@NANOPRO-Group.com"});
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


    public void filter(String text) {
        ArrayList<DetailerModelClass> complaintsListCopy = new ArrayList<DetailerModelClass>
                (detailerArrayList);
        detailerArrayList.clear();
        if (text.isEmpty()) {
            detailerArrayList.addAll(complaintsListCopy);
        } else {
            text = text.toLowerCase();
            for (DetailerModelClass item : complaintsListCopy) {
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
