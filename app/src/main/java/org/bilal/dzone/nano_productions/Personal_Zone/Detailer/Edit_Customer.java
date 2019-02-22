package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.Personal_Zone.CoatingAdapter;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.CoatingModel;
import org.bilal.dzone.nano_productions.Personal_Zone.NonScrollListView;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Edit_Customer extends AppCompatActivity {

    AwesomeProgressDialog awesomeInfoDialog;
    Button add;
    EditText warranty, name, number, plate, model, year, color, email;
    TextView due_date;
    String w_code_id;
    String w_code;
    String api_token, detailer_id,
            warranty_, name_, number_, due_date_, plate_, model_, year_, color_,
            date, coatingDetails, email_;
    ImageView back;
    Spinner prepSpinner, coatStatus;
    List<String> status = new ArrayList<String>();
    String PrepStatus = "InProgress", CoatStatus = "InProgress";
    NonScrollListView nonScrollListView;
    private String[] Coatinglist = new String[]{"Lion", "Tiger", "Leopard", "Cat"};
    private ArrayList<CoatingModel> modelArrayList;
    CoatingAdapter coatingAdapter;
    ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__customer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");


        Log.e("token", api_token + "\n" + detailer_id);


        nonScrollListView = findViewById(R.id.lv_nonscroll_list);
        back = findViewById(R.id.imageView);
        add = findViewById(R.id.add);
        warranty = findViewById(R.id.warranty);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        due_date = findViewById(R.id.date);
        model = findViewById(R.id.model);
        year = findViewById(R.id.year);
        color = findViewById(R.id.color);
        plate = findViewById(R.id.plate);
        email = findViewById(R.id.email);
        scrollView = findViewById(R.id.scrollView);


        // Spinner element
        coatStatus = findViewById(R.id.spinner_coat);
        prepSpinner = findViewById(R.id.spinner_prep);


        name_ = getIntent().getStringExtra("name");
        number_ = getIntent().getStringExtra("phone_number");
        warranty_ = getIntent().getStringExtra("warranty_code");
        plate_ = getIntent().getStringExtra("license_plate_no");
        model_ = getIntent().getStringExtra("model");
        year_ = getIntent().getStringExtra("year");
        color_ = getIntent().getStringExtra("color");
        date = getIntent().getStringExtra("done_date");
        coatingDetails = getIntent().getStringExtra("edition");


        warranty.setText(warranty_);
        name.setText(name_);
        number.setText(number_);
        plate.setText(plate_);
        model.setText(model_);
        year.setText(year_);
        color.setText(color_);
        due_date.setText(date);


        //initialize the spinners
        init();


        //listview click listner
        nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(Edit_Customer.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Customer.this.finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //get Selected checkboxes
                for (int i = 0; i < CoatingAdapter.modelArrayList.size(); i++) {
                    if (CoatingAdapter.modelArrayList.get(i).getSelected()) {
                        Toast.makeText(Edit_Customer.this, CoatingAdapter.modelArrayList.get(i).getTitle(), Toast.LENGTH_LONG).show();
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
                } else {

                    due_date.setError(null);

                    if (new Check_internet_connection(Edit_Customer.this.getApplicationContext()).isNetworkAvailable()) {

                        awesomeInfoDialog = new AwesomeProgressDialog(Edit_Customer.this);
                        awesomeInfoDialog.setTitle("Adding Details!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();
                        SendData();

                    } else {

                        Toast.makeText(Edit_Customer.this.getApplicationContext(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


    }




    //add customer
    public void SendData() {
        String urlGetServerData = Url.BaseUrl + "user/register";
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

                            Toast.makeText(Edit_Customer.this,
                                    "Customer Added", Toast.LENGTH_SHORT).show();
                            Edit_Customer.this.finish();


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

        RequestQueue requestQueue = Volley.newRequestQueue(Edit_Customer.this);
        requestQueue.add(jsonObjectRequest);
    }


    public void init() {

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

                if (position == 0) {
                    PrepStatus = "InProgress";
                } else if (position == 1) {
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

                if (position == 0) {
                    CoatStatus = "InProgress";
                } else if (position == 1) {
                    CoatStatus = "Completed";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }


    //for checkbox selection
    private ArrayList<CoatingModel> getModel(boolean isSelect) {
        ArrayList<CoatingModel> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {

            CoatingModel coatingModel = new CoatingModel();
            coatingModel.setSelected(isSelect);
            coatingModel.setTitle(Coatinglist[i]);
            list.add(coatingModel);
        }
        return list;
    }


    public void make_call(String number) {

        if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(Edit_Customer.this,
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
