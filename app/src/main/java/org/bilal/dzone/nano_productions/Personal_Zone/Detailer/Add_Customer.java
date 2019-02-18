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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.bilal.dzone.nano_productions.Login.Login_Activity;
import org.bilal.dzone.nano_productions.Personal_Zone.CoatingAdapter;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.CoatingModel;
import org.bilal.dzone.nano_productions.Personal_Zone.NewsfeedAdapter;
import org.bilal.dzone.nano_productions.Personal_Zone.NonScrollListView;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.FullScreen;
import org.bilal.dzone.nano_productions.URL.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.bilal.dzone.nano_productions.json.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Add_Customer extends AppCompatActivity {

    AwesomeProgressDialog awesomeInfoDialog;
    Button maintenance, add, coating;
    EditText warranty, name, number, plate, model, year, color, email;
    TextView due_date;
    String w_code_id;
    String w_code;
    String api_token, detailer_id,
            warranty_, name_, number_, due_date_, plate_, model_, year_, color_, email_,
            cal_age, bottom_coat_title = "",
            bottom_coat_edition = "", top_coat_title = "", top_coat_edition = "";
    DatePicker datePicker;
    RadioButton red, silver, black;
    CheckBox top;
    ImageView back;
    Spinner prepSpinner, coatStatus;
    List<String> status = new ArrayList<String>();
    String PrepStatus="InProgress", CoatStatus="InProgress";
    int daysBetween;
    NonScrollListView nonScrollListView;
    private  String[] Coatinglist = new String[]{"Lion", "Tiger", "Leopard", "Cat"};
    private ArrayList<CoatingModel> modelArrayList;
    CoatingAdapter coatingAdapter;
    ScrollView scrollView;
    ListView list;
    EditText etSearch;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__customer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");


        Log.e("token", api_token + "\n" + detailer_id);


        etSearch = findViewById(R.id.et_search);
        linearLayout = findViewById(R.id.l1);
        nonScrollListView = findViewById(R.id.lv_nonscroll_list);
        back = findViewById(R.id.imageView);
        maintenance = findViewById(R.id.maint);
        add = findViewById(R.id.add);
        warranty = findViewById(R.id.warranty);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        due_date = findViewById(R.id.date);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        color = findViewById(R.id.color);
        plate = findViewById(R.id.plate);
        scrollView = findViewById(R.id.scrollView);
        coating = findViewById(R.id.coating);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        list = findViewById(R.id.list);
        email = findViewById(R.id.email);
//        red = findViewById(R.id.red);
//        silver = findViewById(R.id.silver);
//        black = findViewById(R.id.black);
//        top = findViewById(R.id.top);



        // Spinner element
        coatStatus = findViewById(R.id.spinner_coat);
        prepSpinner= findViewById(R.id.spinner_prep);


        //initialize the spinners
        init();



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (new Check_internet_connection(Add_Customer.this.getApplicationContext()).isNetworkAvailable()) {

                    awesomeInfoDialog = new AwesomeProgressDialog(Add_Customer.this);
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

                    Toast.makeText(Add_Customer.this.getApplicationContext(),
                            "Check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });



        //listview click listner
        nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(Add_Customer.this, position+"", Toast.LENGTH_SHORT).show();
            }
        });


        if (new Check_internet_connection(this.getApplicationContext()).isNetworkAvailable()) {

            awesomeInfoDialog = new AwesomeProgressDialog(this);
            awesomeInfoDialog.setTitle("Loading!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();
            getServerData();

        } else {
            Toast.makeText(this.getApplicationContext(),
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Customer.this.finish();
            }
        });



//        red.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottom_coat_edition = "RED ROSE";
//                bottom_coat_title = "PRO9H";
//            }
//        });
//
//
//        black.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottom_coat_edition = "BLACK EDITION";
//                bottom_coat_title = "PRO9H";
//            }
//        });
//
//        silver.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bottom_coat_edition = "SILVER TITANIUM";
//                bottom_coat_title = "PRO9H";
//            }
//        });
//
//
//        top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(checked == 0){
//                    checked = 1;
//                    top_coat_edition = "TOP COAT";
//                    top_coat_title = "PRO FEATHER";
//                }
//                else if(checked == 1){
//                    checked = 0;
//                    top_coat_edition = "";
//                    top_coat_title = "";
//                }
//
//
//            }
//        });


        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.VISIBLE);
                list.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                maintenance.setBackgroundResource(R.drawable.tab_dark_right);
                coating.setBackgroundResource(R.drawable.tab_light_left);
                Load_data();
            }
        });


        coating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                maintenance.setBackgroundResource(R.drawable.tab_button_light);
                coating.setBackgroundResource(R.drawable.tab_button_dark);
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                listview_click(id,position);

                Intent intent = new Intent(Add_Customer.this, Maintain_Customer.class);
                intent.putExtra("name", Sname[position]);
                intent.putExtra("phone_number", Sphone_number[position]);
                intent.putExtra("done_date", Sdone_date[position]);
                intent.putExtra("model", Smodel[position]);
                intent.putExtra("year", Syear[position]);
                intent.putExtra("color", Scolor[position]);
                intent.putExtra("title", Stitle[position]);
                intent.putExtra("edition", Sedition[position]);
                intent.putExtra("email", Semail[position]);
                intent.putExtra("warranty_code", w_code);
                intent.putExtra("license_plate_no", Slicense_plate_no[position]);
                startActivity(intent);

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //get Selected checkboxes
                for (int i = 0; i < CoatingAdapter.modelArrayList.size(); i++){
                    if(CoatingAdapter.modelArrayList.get(i).getSelected()) {
                        Toast.makeText(Add_Customer.this, CoatingAdapter.modelArrayList.get(i).getTitle(), Toast.LENGTH_LONG).show();
                    }
                }


                warranty_ = warranty.getText().toString().trim();
                name_ = name.getText().toString().trim();
                number_ = number.getText().toString().trim();
                due_date_ = due_date.getText().toString().trim();
                plate_ = plate.getText().toString().trim();
                model_ = model.getText().toString().trim();
                year_ = year.getText().toString().trim();
                color_ = color.getText().toString().trim();
                email_ = email.getText().toString().trim();


                if (warranty_.equals("")) {
                    warranty.setError("Enter Warranty NUmber");
                    warranty.setFocusableInTouchMode(true);
                    warranty.setFocusable(true);
                    warranty.requestFocus();
                } else if (name_.equals("")) {
                    name.setError("Enter Customer Name");
                    name.setFocusableInTouchMode(true);
                    name.setFocusable(true);
                    name.requestFocus();
                } else if (number_.equals("")) {
                    number.setError("Enter Customer Number");
                    number.setFocusableInTouchMode(true);
                    number.setFocusable(true);
                    number.requestFocus();
                }else if (email_.equals("")) {
                    email.setError("Enter Customer Email");
                    email.setFocusableInTouchMode(true);
                    email.setFocusable(true);
                    email.requestFocus();
                } else if (due_date_.equals("")) {
                    due_date.setError("Enter Due Date");
                    due_date.setFocusableInTouchMode(true);
                    due_date.setFocusable(true);
                    due_date.requestFocus();
                } else if (plate_.equals("")) {
                    plate.setError("Enter License Number");
                    plate.setFocusableInTouchMode(true);
                    plate.setFocusable(true);
                    plate.requestFocus();
                } else if (model_.equals("")) {
                    model.setError("Enter Car Model");
                    model.setFocusableInTouchMode(true);
                    model.setFocusable(true);
                    model.requestFocus();
                } else if (year_.equals("")) {
                    year.setError("Enter Year");
                    year.setFocusableInTouchMode(true);
                    year.setFocusable(true);
                    year.requestFocus();
                } else if (color_.equals("")) {
                    color.setError("Enter Color");
                    color.setFocusableInTouchMode(true);
                    color.setFocusable(true);
                    color.requestFocus();
                } else if (bottom_coat_title.equals("") && top_coat_title.equals("")) {
                    Toast.makeText(Add_Customer.this, "Select a Coating to Continue", Toast.LENGTH_SHORT).show();
                } else {

                    due_date.setError(null);

                    if (new Check_internet_connection(Add_Customer.this.getApplicationContext()).isNetworkAvailable()) {

                        awesomeInfoDialog = new AwesomeProgressDialog(Add_Customer.this);
                        awesomeInfoDialog.setTitle("Adding Details!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();
                        SendData();

                    } else {

                        Toast.makeText(Add_Customer.this.getApplicationContext(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //CUSTOM DIALOG///////////////////////////////
                final Dialog dialog = new Dialog(Add_Customer.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.dialog_dob);

                datePicker = dialog.findViewById(R.id.datePicker1);
                Button dob = dialog.findViewById(R.id.btn);
                Button btn_cross = dialog.findViewById(R.id.cross);

                btn_cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //set date to textview
                        String date = getDOB();
                        due_date.setText(date);

                        //calulate months
                        calculate_age(getDOB());

                        dialog.dismiss();

                    }
                });


                dialog.show();


            }
        });


    }



    public void listview_click(long id,int position) {

        if (id == 999111) {

            make_call(Sphone_number[position]);

        }
    }


    //get user
    public void getServerData() {
        String urlGetServerData = Url.BaseUrl+"user/getWarantyCode";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("detailer_id", detailer_id);
        postParam.put("api_token", api_token);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam),
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


                            JSONObject jsonObject = response.getJSONObject("waranty_code_data");
                            w_code_id = jsonObject.getString("waranty_code_id");
                            w_code = jsonObject.getString("warranty_code");

                            warranty.setText(w_code);


                            nonScrollListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            nonScrollListView.setFocusable(false);


                            modelArrayList = getModel(false);
                            coatingAdapter = new CoatingAdapter(Add_Customer.this,modelArrayList);
                            nonScrollListView.setAdapter(coatingAdapter);


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

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Customer.this);
        requestQueue.add(jsonObjectRequest);
    }



    //add customer
    public void SendData() {
        String urlGetServerData = Url.BaseUrl+"user/register";
        System.out.print(urlGetServerData);

        Map<String, String> obj = new HashMap<String, String>();

        obj.put("waranty_code_id", w_code_id);
        obj.put("name", name_);
        obj.put("warranty_code", warranty_);
        obj.put("phone_number", number_);
        obj.put("done_date", due_date_);
        obj.put("license_plate_no", plate_);
        obj.put("model", model_);
        obj.put("year", year_);
        obj.put("color", color_);
        obj.put("api_token", api_token);
        obj.put("detailer_id", detailer_id);
        obj.put("cust_email", email_);


        if (top_coat_edition.equals("")){
            obj.put("title",  bottom_coat_title);
            obj.put("edition",  bottom_coat_edition);
        }
        else if (bottom_coat_edition.equals("")){
            obj.put("title",  top_coat_title);
            obj.put("edition",  top_coat_edition);
        }
        else {
            obj.put("title", top_coat_title + "," + bottom_coat_title);
            obj.put("edition", top_coat_edition + "," + bottom_coat_edition);
        }

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

                            Toast.makeText(Add_Customer.this,
                                    "Customer Added", Toast.LENGTH_SHORT).show();
                            Add_Customer.this.finish();



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

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Customer.this);
        requestQueue.add(jsonObjectRequest);
    }



    //get all applications done
    String server_response = "0", no_array= "", server_check;
    JSONObject jsonObj;  JSONArray jsonArray;
    String[] Sname, Sphone_number, Sdone_date, Smodel, Syear, Scolor,Stitle,
            Sedition,
            Semail,
            Swarranty_code,
            Slicense_plate_no;
    Detailer_Adapter adapter;
    DetailerModelClass detailerModelClass;
    ArrayList<DetailerModelClass> detailerArrayList = new ArrayList<>();


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


                    Sname = new String[(jsonArray.length())];
                    Sphone_number = new String[(jsonArray.length())];
                    Sdone_date = new String[(jsonArray.length())];
                    Smodel = new String[(jsonArray.length())];
                    Syear = new String[(jsonArray.length())];
                    Scolor = new String[(jsonArray.length())];
                    Stitle = new String[(jsonArray.length())];
                    Sedition = new String[(jsonArray.length())];
                    Semail = new String[(jsonArray.length())];
                    Swarranty_code = new String[(jsonArray.length())];
                    Slicense_plate_no = new String[(jsonArray.length())];;



                    String sub;
                    sub = jsonObj.getString("subscriptions");
                    if (sub.equals("No Records Found")) {
                        Toast.makeText(Add_Customer.this, "No record", Toast.LENGTH_SHORT).show();
                    }


                    if (server_response.equals("true")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            c = jsonArray.getJSONObject(i);

                            //news feed array
                            if (c.length() > 0) {

                                Sname[i] = c.getString("name");
                                Sphone_number[i] = c.getString("phone_number");
                                Smodel[i] = c.getString("model");
                                Syear[i] = c.getString("year");
                                Scolor[i] = c.getString("color");
                                Sdone_date[i] = c.getString("done_date");
                                Stitle[i] = c.getString("title");
                                Sedition[i] = c.getString("edition");
                                Semail[i] = c.getString("email");
                                Swarranty_code[i] = c.getString("warranty_code");
                                Slicense_plate_no[i] = c.getString("license_plate_no");



                            }

                        }


                        detailerArrayList.clear();

                        //setting values to array-list
                        for (int k = 0; k < Sname.length; k++) {
                            detailerModelClass = new DetailerModelClass(Sname[k],
                                    Sphone_number[k], Sdone_date[k], Smodel[k], Syear[k], Scolor[k],
                                    Stitle[k],Sedition[k], Semail[k], Swarranty_code[k],
                                    Slicense_plate_no[k]);
                            detailerModelClass.setName(Sname[k]);
                            detailerModelClass.setPhone_number(Sphone_number[k]);
                            detailerModelClass.setDone_date(Sdone_date[k]);
                            detailerModelClass.setModel(Smodel[k]);
                            detailerModelClass.setYear(Syear[k]);
                            detailerModelClass.setColor(Scolor[k]);
                            detailerModelClass.setTitle(Stitle[k]);
                            detailerModelClass.setEdition(Sedition[k]);
                            detailerModelClass.setEmail(Semail[k]);
                            detailerModelClass.setWarranty_code(Swarranty_code[k]);
                            detailerModelClass.setLicense_plate_no(Slicense_plate_no[k]);

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

                        new AwesomeErrorDialog(Add_Customer.this)
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

                        new AwesomeErrorDialog(Add_Customer.this)
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



                        adapter = new Detailer_Adapter(Add_Customer.this
                                , detailerArrayList);
                        list.setAdapter(adapter);


                        // Add Text Change Listener to EditText
                        etSearch.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // Call back the Adapter with current character to Filter
                                adapter.getFilter().filter(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });



                    } else {

                        awesomeInfoDialog.hide();

                        new AwesomeErrorDialog(Add_Customer.this)
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

                        new AwesomeErrorDialog(Add_Customer.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(Add_Customer.this);
        requestQueue.add(jsonObjectRequest);
    }


    //set dob to textview
    public String getDOB() {

        StringBuilder builder = new StringBuilder();
        builder.append(datePicker.getDayOfMonth() + "-");
        builder.append((datePicker.getMonth() + 1) + "-");//month is 0 based
        builder.append(datePicker.getYear());

        return builder.toString();
    }


    //calculate age
    public String calculate_age(String dob) {

        //get current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
//        Toast.makeText(this, formattedDate+"", Toast.LENGTH_SHORT).show();


        //calculate days
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateBefore = myFormat.parse(dob);
            Date dateAfter = myFormat.parse(formattedDate);

            daysBetween = DateTimeUtils.getDateDiff(dateAfter, dateBefore, DateTimeUnits.DAYS);
//            Toast.makeText(this, diff+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cal_age;
    }



    public void init(){

        status.add("InProgress");
        status.add("Completed");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, status);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        prepSpinner.setAdapter(dataAdapter);
        coatStatus.setAdapter(dataAdapter);

        prepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0){
                    PrepStatus = "InProgress";
                }
                else if (position == 1){
                    PrepStatus = "Completed";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



        coatStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0){
                    CoatStatus = "InProgress";
                }
                else if (position == 1){
                    CoatStatus = "Completed";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }



    //
    private ArrayList<CoatingModel> getModel(boolean isSelect){
        ArrayList<CoatingModel> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){

            CoatingModel coatingModel = new CoatingModel();
            coatingModel.setSelected(isSelect);
            coatingModel.setTitle(Coatinglist[i]);
            list.add(coatingModel);
        }
        return list;
    }



    public void make_call(String number) {

        if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(Add_Customer.this,
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


}
