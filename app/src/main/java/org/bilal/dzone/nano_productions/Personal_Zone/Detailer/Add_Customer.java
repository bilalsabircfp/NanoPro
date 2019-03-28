package org.bilal.dzone.nano_productions.Personal_Zone.Detailer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bilal.dzone.nano_productions.Personal_Zone.CoatingAdapter;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.CoatingModel;
import org.bilal.dzone.nano_productions.Personal_Zone.NonScrollListView;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.API.UploadApi;
import org.bilal.dzone.nano_productions.Utils.FileUtils;
import org.bilal.dzone.nano_productions.Utils.ResponseData;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Add_Customer extends AppCompatActivity {

    int index_;
    Switch PreSwitch, CoatSwitch;
    String date;

    private static final int GALLERY1 = 300;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE1 = 0x1;
    private static final int REQUEST_TAKE_PHOTO1 = 100;

    private static final int GALLERY2 = 301;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE2 = 0x2;
    private static final int REQUEST_TAKE_PHOTO2 = 102;

    private static final int GALLERY3 = 302;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE3 = 0x3;
    private static final int REQUEST_TAKE_PHOTO3 = 104;

    private static final int GALLERY4 = 304;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE4 = 0x4;
    private static final int REQUEST_TAKE_PHOTO4 = 106;

    private static final int GALLERY5 = 305;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE5 = 0x5;
    private static final int REQUEST_TAKE_PHOTO5 = 108;

    File camerafile1, camerafile2, camerafile3, camerafile4, camerafile5;
    File galleryfile1, galleryfile2, galleryfile3, galleryfile4, galleryfile5;
    Uri file_uri;
    private String mCurrentPhotoPath;
    private File compressedfile1, compressedfile2, compressedfile3, compressedfile4, compressedfile5;
    private List<MultipartBody.Part> fileParts = new ArrayList<>();
    MultipartBody.Part part1, part2, part3, part4, part5;
    MultipartBody.Part gallerypart1, gallerypart2, gallerypart3, gallerypart4, gallerypart5;


    AwesomeProgressDialog awesomeInfoDialog;
    Button maintenance, add, coating;
    EditText warranty, name, number, plate, model, year, color, email;
    TextView due_date;
    String w_code_id;
    String w_code, SproductTitle;
    String[] productTitle, Remarks;
    String api_token, detailer_id,
            warranty_, name_, number_, due_date_, plate_, model_, year_, color_, email_,
            cal_age, remarks_,
            bottom_coat_edition = "", top_coat_title = "", top_coat_edition = "";
    DatePicker datePicker;
    ImageView back, img1, img2, img3, img4, img5;
//    Spinner prepSpinner, coatStatus;
    List<String> status = new ArrayList<String>();
    String PrepStatus = "InProgress", CoatStatus = "InProgress";
    int daysBetween;
    NonScrollListView nonScrollListView;
    private ArrayList<CoatingModel> modelArrayList;
    CoatingAdapter coatingAdapter;
    ScrollView scrollView;
    ListView listView;
    EditText etSearch;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<CoatingModel> list = new ArrayList<>();
    ArrayList<String> Selecteditems = new ArrayList<>();
    String s = "";
    String temp;
    TextInputEditText remarks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__customer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");


        Log.e("token", api_token + "\n" + detailer_id);


        remarks = findViewById(R.id.etText);
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
        listView = findViewById(R.id.list);
        email = findViewById(R.id.email);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);



        // Spinner element
        PreSwitch = findViewById(R.id.switch1);
        CoatSwitch = findViewById(R.id.switch2);


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

                Toast.makeText(Add_Customer.this, position + "", Toast.LENGTH_SHORT).show();
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


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();

                } else {
                    ActivityCompat.requestPermissions(Add_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE1);
                }
            }
        });


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog2();

                } else {
                    ActivityCompat.requestPermissions(Add_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE2);
                }
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog3();

                } else {
                    ActivityCompat.requestPermissions(Add_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE3);
                }
            }
        });


        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog4();

                } else {
                    ActivityCompat.requestPermissions(Add_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE4);
                }
            }
        });


        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog5();

                } else {
                    ActivityCompat.requestPermissions(Add_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE5);
                }
            }
        });


        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                maintenance.setBackgroundResource(R.drawable.tab_dark_right);
                coating.setBackgroundResource(R.drawable.tab_light_left);
                Load_data();
            }
        });


        coating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                maintenance.setBackgroundResource(R.drawable.tab_button_light);
                coating.setBackgroundResource(R.drawable.tab_button_dark);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                listview_click(id, position);

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
                intent.putExtra("remarks", Sremarks);
                intent.putExtra("license_plate_no", Slicense_plate_no[position]);
                startActivity(intent);

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Selecteditems.clear();
                SproductTitle = "";
                temp = "";
                s = "";
                //get Selected checkboxes
                for (int i = 0; i < CoatingAdapter.modelArrayList.size(); i++) {
                    if (CoatingAdapter.modelArrayList.get(i).getSelected()) {

                        s = CoatingAdapter.modelArrayList.get(i).getTitle();
                        Selecteditems.add(s);
//                        Toast.makeText(Add_Customer.this, CoatingAdapter.modelArrayList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }


                int a = 0, x = 0;
                for (int i = 0; i < Selecteditems.size(); i++) {

                    a = a + 1;
                    temp = Selecteditems.get(i);


                    if (a == 1) {
                        SproductTitle = temp;
                    } else {
                        x = x + 1;
                    }

                    if (a > 1 && x == 1) {
                        s = SproductTitle + "," + temp;
                    } else {
                        s = s + "," + temp;

                    }

                }
                Log.e("abc", s);


                remarks_ = remarks.getText().toString().trim();
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
                } else if (email_.equals("")) {
                    email.setError("Enter Customer Email");
                    email.setFocusableInTouchMode(true);
                    email.setFocusable(true);
                    email.requestFocus();
                }else if (!email_.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")
                        && !email_.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")) {
                    email.setError("Not a valid Email");
                    email.setFocusableInTouchMode(true);
                    email.setFocusable(true);
                    email.requestFocus();
                }
                else if (date.equals("")) {
                    due_date.setError("Enter Due Date");
                    due_date.setFocusableInTouchMode(true);
                    due_date.setFocusable(true);
                    due_date.requestFocus();
                } else if (remarks_.equals("")) {
                    remarks.setError("Enter Remarks");
                    remarks.setFocusableInTouchMode(true);
                    remarks.setFocusable(true);
                    remarks.requestFocus();
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
                } else if (s.equals("")) {
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
                        UploadCode();
                        Toast.makeText(Add_Customer.this, date, Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "fileParts: " + fileParts.toString());

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
                dialog.setCanceledOnTouchOutside(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setContentView(R.layout.dialog_dob);

                datePicker = dialog.findViewById(R.id.datePicker1);
                Button dob = dialog.findViewById(R.id.btn);


//                datePicker.setMinDate(System.currentTimeMillis() - 1000);


                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        //set date to textview
                        date = getDOB();
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


    public void listview_click(long id, int position) {

        if (id == 999111) {

            make_call(Sphone_number[position]);

        }
    }


    JSONObject c;

    //get user
    public void getServerData() {
        String urlGetServerData = Url.BaseUrl + "user/getWarantyCode";
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


                            jsonObj = new JSONObject(response.toString());
                            try {
                                jsonArray = jsonObj.getJSONArray("products");
                            } catch (JSONException e) {
                                Log.e("Error", "no array");
                            }

                            JSONObject jsonObject = response.getJSONObject("waranty_code_data");
                            w_code_id = jsonObject.getString("waranty_code_id");
                            w_code = jsonObject.getString("warranty_code");


                            productTitle = new String[(jsonArray.length())];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                c = jsonArray.getJSONObject(i);
                                if (c.length() > 0) {

                                    productTitle[i] = c.getString("title");

                                    Log.e("products", productTitle[i]);
                                }
                            }


                            warranty.setText(w_code);


                            nonScrollListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                            nonScrollListView.setFocusable(false);


                            modelArrayList = getModel(false);
                            coatingAdapter = new CoatingAdapter(Add_Customer.this, modelArrayList);
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


    //get all applications done
    String server_response = "0", no_array = "", server_check;
    JSONObject jsonObj;
    JSONArray jsonArray;
    String[] Sname, Sphone_number, Sdone_date, Smodel, Syear, Scolor, Stitle,
            Sedition, Sremarks,
            Semail,
            Swarranty_code,
            Slicense_plate_no;
    Detailer_Adapter adapter;
    DetailerModelClass detailerModelClass;
    ArrayList<DetailerModelClass> detailerArrayList = new ArrayList<>();


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
                    Slicense_plate_no = new String[(jsonArray.length())];
                    Sremarks = new String[(jsonArray.length())];


                    if (server_response.equals("true")) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            c = jsonArray.getJSONObject(i);

                            //news feed array
                            if (c.length() > 0) {

                                Sremarks[i] = c.getString("remarks");
                                Sname[i] = c.getString("name");
                                Sphone_number[i] = c.getString("phone_number");
                                Smodel[i] = c.getString("model");
                                Syear[i] = c.getString("year");
                                Scolor[i] = c.getString("color");
                                Sdone_date[i] = c.getString("done_date");
                                Stitle[i] = c.getString("title");
                                Sedition[i] = c.getString("edition");
                                Semail[i] = c.getString("cust_email");
                                Swarranty_code[i] = c.getString("warranty_code");
                                Slicense_plate_no[i] = c.getString("license_plate_no");

                                Log.e("array1", Sname[i] + "\n" + Sphone_number[i] + "\n" +
                                        Smodel[i] + "\n" + Syear[i] + "\n" + Sedition[i]);

                            }

                        }


                        detailerArrayList.clear();

                        //setting values to array-list
                        for (int k = 0; k < Sname.length; k++) {
                            detailerModelClass = new DetailerModelClass(Sname[k],
                                    Sphone_number[k], Sdone_date[k], Smodel[k], Syear[k], Scolor[k],
                                    Sremarks[k], Stitle[k], Sedition[k], Semail[k], Swarranty_code[k],
                                    Slicense_plate_no[k]);
                            detailerModelClass.setName(Sname[k]);
                            detailerModelClass.setPhone_number(Sphone_number[k]);
                            detailerModelClass.setDone_date(Sdone_date[k]);
                            detailerModelClass.setModel(Smodel[k]);
                            detailerModelClass.setYear(Syear[k]);
                            detailerModelClass.setColor(Scolor[k]);
                            detailerModelClass.setColor(Sremarks[k]);
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

        final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        StringBuilder builder = new StringBuilder();
        builder.append(MONTHS[(datePicker.getMonth())] + " ");//month is 0 based
        builder.append(datePicker.getDayOfMonth() + " , ");
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


    public void init() {

//        status.add("InProgress");
//        status.add("Completed");
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, status);
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // attaching data adapter to spinner
//        prepSpinner.setAdapter(dataAdapter);
//        coatStatus.setAdapter(dataAdapter);
//
//        prepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//                if (position == 0) {
//                    PrepStatus = "InProgress";
//                } else if (position == 1) {
//                    PrepStatus = "Completed";
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//
//        });
//
//
//        coatStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//
//                if (position == 0) {
//                    CoatStatus = "InProgress";
//                } else if (position == 1) {
//                    CoatStatus = "Completed";
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//
//        });



        PreSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    PrepStatus = "Completed";
                } else {
                    PrepStatus = "InProgress";
                }
            }
        });


        CoatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    CoatStatus = "Completed";
                } else {
                    CoatStatus = "InProgress";
                }
            }
        });

    }


    //
    private ArrayList<CoatingModel> getModel(boolean isSelect) {

        for (int i = 0; i < productTitle.length; i++) {

            CoatingModel coatingModel = new CoatingModel();
            coatingModel.setSelected(isSelect);
            coatingModel.setTitle(productTitle[i]);
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


    private void UploadCode() {
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("waranty_code_id", createPartFromString(w_code_id));
        map.put("name", createPartFromString(name_));
        map.put("warranty_code", createPartFromString(warranty_));
        map.put("phone_number", createPartFromString(number_));
        map.put("done_date", createPartFromString(date));
        map.put("license_plate_no", createPartFromString(plate_));
        map.put("model", createPartFromString(model_));
        map.put("year", createPartFromString(year_));
        map.put("color", createPartFromString(color_));
        map.put("api_token", createPartFromString(api_token));
        map.put("detailer_id", createPartFromString(detailer_id));
        map.put("cust_email", createPartFromString(email_));

        if (Selecteditems.size() == 1) {
            map.put("title", createPartFromString(Selecteditems.get(0)));
        } else {
            map.put("title", createPartFromString(s));
        }
        map.put("edition", createPartFromString(s));
        map.put("remarks", createPartFromString(remarks_));
        map.put("preparation", createPartFromString(PrepStatus));
        map.put("coating", createPartFromString(CoatStatus));


        Log.e("TAG", "dataUpload: " + map.toString());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UploadApi rest = retrofit.create(UploadApi.class);
        Log.e("TAG", "fileParts: " + fileParts.toString());
        Call<ResponseData> call = rest.uploadCustomer(map, fileParts);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {

//                Log.e("ServerResponse", response.body().getSuccess()+"/"+response.body().getStatus()
//                        +"/"+response.body().getMessage());
                Log.e("ServerResponse", response.toString());

                if (response.isSuccessful()) {
                    awesomeInfoDialog.hide();
                    Add_Customer.this.finish();
                    Toast.makeText(Add_Customer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("serverResponse", response.toString());
                    awesomeInfoDialog.hide();
                    Add_Customer.this.finish();
                    Toast.makeText(Add_Customer.this, "Submitted Successfully Without Image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("FailuerResponse", t.toString());
                awesomeInfoDialog.hide();
                Toast.makeText(Add_Customer.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageDialog1() {
        final Dialog sheetDialog = new Dialog(Add_Customer.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile1 = null;
                    try {
                        camerafile1 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile1);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile1 != null) {
                        file_uri = FileProvider.getUriForFile(Add_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile1);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO1);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY1);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog2() {
        final Dialog sheetDialog = new Dialog(Add_Customer.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile2 = null;
                    try {
                        camerafile2 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile2);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile2 != null) {
                        file_uri = FileProvider.getUriForFile(Add_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile2);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO2);

                    }
                }
                sheetDialog.dismiss();
            }
        });
        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY2);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog3() {
        final Dialog sheetDialog = new Dialog(Add_Customer.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile3 = null;
                    try {
                        camerafile3 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile3);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile3 != null) {
                        file_uri = FileProvider.getUriForFile(Add_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile3);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO3);

                    }
                }
                sheetDialog.dismiss();
            }
        });
        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY3);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog4() {
        final Dialog sheetDialog = new Dialog(Add_Customer.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile4 = null;
                    try {
                        camerafile4 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile4);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile4 != null) {
                        file_uri = FileProvider.getUriForFile(Add_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile4);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO4);

                    }
                }
                sheetDialog.dismiss();
            }
        });
        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY4);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog5() {
        final Dialog sheetDialog = new Dialog(Add_Customer.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile5 = null;
                    try {
                        camerafile5 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile5);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile5 != null) {
                        file_uri = FileProvider.getUriForFile(Add_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile5);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO5);

                    }
                }
                sheetDialog.dismiss();
            }
        });
        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY5);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    ImageView imageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {

                    if (gallerypart1 != null){
                        index_ = fileParts.indexOf(gallerypart1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart1 = null;
                    }

                    //check if part is empty or not
                    if (part1 == null) {
                        Log.e("part1", "empty");
                    } else {
                        //get the index from arraylist where the part value is located
                        index_ = fileParts.indexOf(part1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part1 = null;
                    }

                    compressedfile1 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile1);
                    delete(camerafile1);
                    //image parameter name
                    part1 = prepareFilePart("before_img[]", compressedfile1);
                    fileParts.add(part1);

                    img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img1.setImageURI(FileUtils.getUri(compressedfile1));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO2 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart2 != null){
                        index_ = fileParts.indexOf(gallerypart2);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart2 = null;
                    }


                    if (part2 == null) {
                        Log.e("part2", "empty");
                    } else {
                        index_ = fileParts.indexOf(part2);
                        fileParts.remove(index_);
                        part2 = null;
                    }


                    compressedfile2 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile2);
                    delete(camerafile2);
                    part2 = prepareFilePart("before_img[]", compressedfile2);
                    fileParts.add(part2);

                    img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img2.setImageURI(FileUtils.getUri(compressedfile2));


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO3 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart3 != null){
                        index_ = fileParts.indexOf(gallerypart3);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart3 = null;
                    }


                    if (part3 == null) {
                        Log.e("part3", "empty");
                    } else {
                        index_ = fileParts.indexOf(part3);
                        fileParts.remove(index_);
                        part3 = null;
                    }

                    compressedfile3 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile3);
                    delete(camerafile3);
                    part3 = prepareFilePart("before_img[]", compressedfile3);
                    fileParts.add(part3);


                    img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img3.setImageURI(FileUtils.getUri(compressedfile3));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO4 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart4 != null){
                        index_ = fileParts.indexOf(gallerypart4);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart4 = null;
                    }


                    if (part4 == null) {
                        Log.e("part4", "empty");
                    } else {
                        index_ = fileParts.indexOf(part4);
                        fileParts.remove(index_);
                        part4 = null;
                    }

                    compressedfile4 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile4);
                    delete(camerafile4);
                    part4 = prepareFilePart("before_img[]", compressedfile4);
                    fileParts.add(part4);


                  ;
                    img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img4.setImageURI(FileUtils.getUri(compressedfile4));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO5 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart5 != null){
                        index_ = fileParts.indexOf(gallerypart5);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart5 = null;
                    }


                    if (part5 == null) {
                        Log.e("part5", "empty");
                    } else {
                        index_ = fileParts.indexOf(part5);
                        fileParts.remove(index_);
                        part5 = null;
                    }

                    compressedfile5 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile5);
                    delete(camerafile5);
                    part5 = prepareFilePart("before_img[]", compressedfile5);
                    fileParts.add(part5);


                    img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img5.setImageURI(FileUtils.getUri(compressedfile5));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == GALLERY1 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part1 != null){
                        index_ = fileParts.indexOf(part1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part1 = null;
                    }


                    if (gallerypart1 == null) {
                        Log.e("part1", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart1);
                        fileParts.remove(index_);
                        gallerypart1 = null;
                    }

                    galleryfile1 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart1 = prepareFilePart("before_img[]", galleryfile1);
                    fileParts.add(gallerypart1);


                    img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img1.setImageURI(FileUtils.getUri(galleryfile1));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY2 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part2 != null){
                        index_ = fileParts.indexOf(part2);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part2 = null;
                    }


                    if (gallerypart2 == null) {
                        Log.e("part2", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart2);
                        fileParts.remove(index_);
                        gallerypart2 = null;
                    }

                    galleryfile2 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart2 = prepareFilePart("before_img[]", galleryfile2);
                    fileParts.add(gallerypart2);


                    img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img2.setImageURI(FileUtils.getUri(galleryfile2));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY3 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part3 != null){
                        index_ = fileParts.indexOf(part3);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part3 = null;
                    }


                    if (gallerypart3 == null) {
                        Log.e("part3", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart3);
                        fileParts.remove(index_);
                        gallerypart3 = null;
                    }

                    galleryfile3 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart3 = prepareFilePart("before_img[]", galleryfile3);
                    fileParts.add(gallerypart3);


                    img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img3.setImageURI(FileUtils.getUri(galleryfile3));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY4 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part4 != null){
                        index_ = fileParts.indexOf(part4);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part4 = null;
                    }


                    if (gallerypart4 == null) {
                        Log.e("part4", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart4);
                        fileParts.remove(index_);
                        gallerypart4 = null;
                    }

                    galleryfile4 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart4 = prepareFilePart("before_img[]", galleryfile4);
                    fileParts.add(gallerypart4);


                    img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img4.setImageURI(FileUtils.getUri(galleryfile4));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY5 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part5 != null){
                        index_ = fileParts.indexOf(part5);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part5 = null;
                    }


                    if (gallerypart5 == null) {
                        Log.e("part5", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart5);
                        fileParts.remove(index_);
                        gallerypart5 = null;
                    }

                    galleryfile5 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart5 = prepareFilePart("before_img[]", galleryfile5);
                    fileParts.add(gallerypart5);


                    img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img5.setImageURI(FileUtils.getUri(galleryfile5));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_IMAGE1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog2();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog3();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;


            case MY_PERMISSIONS_REQUEST_IMAGE4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog4();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog5();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @NonNull
    private RequestBody createPartFromString(String val) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, val);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir1 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir1      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        Log.e("TAG", "createImageFile: " + mCurrentPhotoPath);
        return image;
    }

    private void delete(File file) {
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                Log.e("TAG", "IOException: " + e.toString());
            }
            if (file.exists()) {
                getApplicationContext().deleteFile(file.getName());
            }
        }
    }


}
