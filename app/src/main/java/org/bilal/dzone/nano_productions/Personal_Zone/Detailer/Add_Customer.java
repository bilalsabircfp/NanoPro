package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RadioButton;
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
    Button maintenance, add;
    EditText warranty, name, number, plate, model, year, color;
    TextView due_date;
    String w_code_id;
    String w_code;
    String api_token, detailer_id,
            warranty_, name_, number_, due_date_, plate_, model_, year_, color_,
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__customer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");


        Log.e("token", api_token + "\n" + detailer_id);


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
//        red = findViewById(R.id.red);
//        silver = findViewById(R.id.silver);
//        black = findViewById(R.id.black);
//        top = findViewById(R.id.top);


        // Spinner element
        coatStatus = findViewById(R.id.spinner_coat);
        prepSpinner= findViewById(R.id.spinner_prep);


        //initialize the spinners
        init();


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
                Toast.makeText(Add_Customer.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
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




}
