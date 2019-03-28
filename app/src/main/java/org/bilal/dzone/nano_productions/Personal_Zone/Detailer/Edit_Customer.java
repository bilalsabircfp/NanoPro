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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bilal.dzone.nano_productions.Personal_Zone.FullImage;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.API.UploadApi;
import org.bilal.dzone.nano_productions.Utils.FileUtils;
import org.bilal.dzone.nano_productions.Utils.ResponseData;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
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


public class Edit_Customer extends AppCompatActivity {

    Call<ResponseData> call;
    Intent intent;

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


    private static final int GALLERY6 = 306;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE6 = 0x6;
    private static final int REQUEST_TAKE_PHOTO6 = 100;

    private static final int GALLERY7 = 307;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE7 = 0x7;
    private static final int REQUEST_TAKE_PHOTO7 = 102;

    private static final int GALLERY8 = 308;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE8 = 0x8;
    private static final int REQUEST_TAKE_PHOTO8 = 104;

    private static final int GALLERY9 = 309;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE9 = 0x9;
    private static final int REQUEST_TAKE_PHOTO9 = 106;

    private static final int GALLERY10 = 310;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE10 = 0x10;
    private static final int REQUEST_TAKE_PHOTO10 = 108;


    File camerafile1, camerafile2, camerafile3, camerafile4, camerafile5, camerafile6, camerafile7, camerafile8, camerafile9, camerafile10;
    File galleryfile1, galleryfile2, galleryfile3, galleryfile4, galleryfile5, galleryfile6, galleryfile7, galleryfile8, galleryfile9, galleryfile10;
    Uri file_uri;
    private String mCurrentPhotoPath;
    private File compressedfile1, compressedfile2, compressedfile3, compressedfile4, compressedfile5, compressedfile6, compressedfile7, compressedfile8, compressedfile9, compressedfile10;
    private List<MultipartBody.Part> fileParts = new ArrayList<>();
    private List<MultipartBody.Part> fileParts2 = new ArrayList<>();
    MultipartBody.Part part1, part2, part3, part4, part5, part6, part7, part8, part9, part10;
    MultipartBody.Part gallerypart1, gallerypart2, gallerypart3, gallerypart4, gallerypart5, gallerypart6, gallerypart7, gallerypart8, gallerypart9, gallerypart10;


    AwesomeProgressDialog awesomeInfoDialog;
    Button add;
    Switch PreSwitch, CoatSwitch;
    EditText warranty, name, number, plate, model, year, color, email;
    TextInputEditText remarks;
    TextView due_date, TvcoatingDetails;
    String api_token, detailer_id, remarks_,
            warranty_, name_, number_, due_date_, plate_, model_, year_, color_,
            date, coatingDetails, email_, cust_id, car_details_id, images, after_images;
    ImageView back;
    Spinner prepSpinner, coatStatus;
    List<String> status = new ArrayList<String>();
    String PrepStatus = "InProgress", CoatStatus = "InProgress" , coating, preparation;
    ScrollView scrollView;
    ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10;
    LinearLayout mainLayout;
    int index_, beforeImgCount, afterImgCount;
    ArrayList<String> beforeImg = new ArrayList<>();
    ArrayList<String> afterImg = new ArrayList<>();
    String hasImage1="no",hasImage2="no",hasImage3="no",hasImage4="no",hasImage5="no",
            hasImage6="no",hasImage7="no",hasImage8="no"
            ,hasImage9="no",hasImage10="no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__customer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");
        detailer_id = sharedPreferences.getString("id", "");


        Log.e("token", api_token + "\n" + detailer_id);


        TvcoatingDetails = findViewById(R.id.coating_details);
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
        remarks = findViewById(R.id.etText);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);
        mainLayout = findViewById(R.id.main);


        // Spinner element
        PreSwitch = findViewById(R.id.switch1);
        CoatSwitch = findViewById(R.id.switch2);


        cust_id = getIntent().getStringExtra("cust_id");
        name_ = getIntent().getStringExtra("name");
        email_ = getIntent().getStringExtra("email");
        number_ = getIntent().getStringExtra("phone_number");
        warranty_ = getIntent().getStringExtra("warranty_code");
        plate_ = getIntent().getStringExtra("license_plate_no");
        model_ = getIntent().getStringExtra("model");
        year_ = getIntent().getStringExtra("year");
        color_ = getIntent().getStringExtra("color");
        date = getIntent().getStringExtra("done_date");
        remarks_ = getIntent().getStringExtra("remarks");
        coatingDetails = getIntent().getStringExtra("title");
        car_details_id = getIntent().getStringExtra("car_details_id");
        images = getIntent().getStringExtra("images");
        after_images = getIntent().getStringExtra("after_images");
        coating = getIntent().getStringExtra("coating");
        preparation = getIntent().getStringExtra("preparation");


        if (coating.equals("Completed")){
            CoatSwitch.setChecked(true);
        }
        if (preparation.equals("Completed")){
            PreSwitch.setChecked(true);
        }



        if (!images.equals("")) {

            String complete_String = (images);

            StringTokenizer stringTokenizer = new StringTokenizer(complete_String, ",");
            while (stringTokenizer.hasMoreTokens()) {
                beforeImg.add(stringTokenizer.nextToken());
            }

            beforeImgCount = beforeImg.size();
            loadBeforeImages();

        }


        if (!after_images.equals("")) {

            String complete_String = (after_images);

            StringTokenizer stringTokenizer = new StringTokenizer(complete_String, ",");
            while (stringTokenizer.hasMoreTokens()) {
                afterImg.add(stringTokenizer.nextToken());
            }

            afterImgCount = afterImg.size();
            loadAfterImages();

        }


        warranty.setText(warranty_);
        name.setText(name_);
        number.setText(number_);
        plate.setText(plate_);
        model.setText(model_);
        year.setText(year_);
        color.setText(color_);
        due_date.setText(date);
        TvcoatingDetails.setText(coatingDetails);
        remarks.setText(remarks_);
        email.setText(email_);


        //initialize the spinners
        init();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Customer.this.finish();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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
//                }else if (fileParts.size() == 0) {
//                    Toast.makeText(Edit_Customer.this, "Upload Images", Toast.LENGTH_SHORT).show();
                } else {

                    due_date.setError(null);

                    if (new Check_internet_connection(Edit_Customer.this.getApplicationContext()).isNetworkAvailable()) {

                        awesomeInfoDialog = new AwesomeProgressDialog(Edit_Customer.this);
                        awesomeInfoDialog.setTitle("Updating Details!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();

//                        if (fileParts.size() < 1){
//                            Snackbar snackbar = Snackbar
//                                    .make(mainLayout, "Select an Image", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                        }
//                        else {
//                            Log.e("TAG", "fileParts: "+fileParts.toString() );
                        UploadCode();
//                        }


                    } else {

                        Toast.makeText(Edit_Customer.this.getApplicationContext(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasImage1.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", afterImg.get(0));
                    startActivity(intent);
                }else

                    if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        openImageDialog1();

                    } else {
                        ActivityCompat.requestPermissions(Edit_Customer.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_IMAGE1);
                    }

            }
        });


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage2.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", afterImg.get(1));
                    startActivity(intent);
                }else

                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog2();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE2);
                }
            }
        });


        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage3.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", afterImg.get(2));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog3();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE3);
                }
            }
        });


        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage4.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", afterImg.get(3));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog4();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE4);
                }
            }
        });


        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage5.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", afterImg.get(4));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog5();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE5);
                }
            }
        });


        //before image
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage6.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", beforeImg.get(0));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog6();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE1);
                }
            }
        });


        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage7.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", beforeImg.get(1));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog7();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE2);
                }
            }
        });


        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage8.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", beforeImg.get(2));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog8();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE3);
                }
            }
        });


        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage9.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", beforeImg.get(3));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog9();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE4);
                }
            }
        });


        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hasImage10.equals("yes")){
                    intent = new Intent(Edit_Customer.this, FullImage.class);
                    intent.putExtra("url", beforeImg.get(4));
                    startActivity(intent);
                }else


                if (ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Edit_Customer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog10();

                } else {
                    ActivityCompat.requestPermissions(Edit_Customer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE5);
                }
            }
        });


    }


    private void UploadCode() {
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("name", createPartFromString(name_));
        map.put("coating", createPartFromString(coating));
        map.put("preparation", createPartFromString(preparation));
        map.put("email", createPartFromString(email_));
        map.put("ph_no", createPartFromString(number_));
        map.put("api_token", createPartFromString(api_token));
        map.put("remarks", createPartFromString(remarks_));
        map.put("car_detail_id", createPartFromString(car_details_id));
        map.put("customer_id", createPartFromString(cust_id));
        map.put("pre_before_img", createPartFromString(images));
        map.put("pre_after_img", createPartFromString(after_images));


        Log.e("Values", remarks_ + "/" + CoatStatus + "/" + PrepStatus + "/" + email_
                + "/" + number_ + "/" + cust_id + "/" + car_details_id);

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

        if (!images.equals("")) {
            call = rest.updateCustomer(map, fileParts);
        }

        call = rest.updateCustomer(map, fileParts);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {

//                Log.e("ServerResponse", response.body().getSuccess()+"/"+response.body().getStatus()
//                        +"/"+response.body().getMessage());
                Log.e("ServerResponse", response.toString());

                if (response.isSuccessful()) {
                    awesomeInfoDialog.hide();

                    if (response.body().getMessage().equals("Please Review All Fields")) {
                        Toast.makeText(Edit_Customer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        Edit_Customer.this.finish();
                        Toast.makeText(Edit_Customer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Log.e("serverResponse", response.toString());
                    awesomeInfoDialog.hide();
                    Toast.makeText(Edit_Customer.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("FailuerResponse", t.toString());
                awesomeInfoDialog.hide();
                Toast.makeText(Edit_Customer.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageDialog1() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile1);
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
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile2);
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
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile3);
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
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile4);
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
                startActivityForResult(photoPickerIntent, GALLERY4);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog5() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile5);
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


    //before images
    private void openImageDialog6() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                    camerafile6 = null;
                    try {
                        camerafile6 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile6);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile6 != null) {
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile6);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO6);

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
                startActivityForResult(photoPickerIntent, GALLERY6);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog7() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                    camerafile7 = null;
                    try {
                        camerafile7 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile7);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile7 != null) {
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile7);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO7);

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
                startActivityForResult(photoPickerIntent, GALLERY7);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog8() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                    camerafile8 = null;
                    try {
                        camerafile8 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile8);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile8 != null) {
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile8);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO8);

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
                startActivityForResult(photoPickerIntent, GALLERY8);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog9() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                    camerafile9 = null;
                    try {
                        camerafile9 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile9);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile9 != null) {
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile9);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO9);

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
                startActivityForResult(photoPickerIntent, GALLERY9);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }

    private void openImageDialog10() {
        final Dialog sheetDialog = new Dialog(Edit_Customer.this, R.style.dialog_theme);
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
                    camerafile10 = null;
                    try {
                        camerafile10 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile10);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile10 != null) {
                        file_uri = FileProvider.getUriForFile(Edit_Customer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile10);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO10);

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
                startActivityForResult(photoPickerIntent, GALLERY10);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart1 != null) {
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
                    part1 = prepareFilePart("after_img[]", compressedfile1);
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


                    if (gallerypart2 != null) {
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
                    part2 = prepareFilePart("after_img[]", compressedfile2);
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


                    if (gallerypart3 != null) {
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
                    part3 = prepareFilePart("after_img[]", compressedfile3);
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


                    if (gallerypart4 != null) {
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
                    part4 = prepareFilePart("after_img[]", compressedfile4);
                    fileParts.add(part4);

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


                    if (gallerypart5 != null) {
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
                    part5 = prepareFilePart("after_img[]", compressedfile5);
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


                    if (part1 != null) {
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
                    gallerypart1 = prepareFilePart("after_img[]", galleryfile1);
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


                    if (part2 != null) {
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
                    gallerypart2 = prepareFilePart("after_img[]", galleryfile2);
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


                    if (part3 != null) {
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
                    gallerypart3 = prepareFilePart("after_img[]", galleryfile3);
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


                    if (part4 != null) {
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
                    gallerypart4 = prepareFilePart("after_img[]", galleryfile4);
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


                    if (part5 != null) {
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
                    gallerypart5 = prepareFilePart("after_img[]", galleryfile5);
                    fileParts.add(gallerypart5);

                    img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img5.setImageURI(FileUtils.getUri(galleryfile5));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY6 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part6 != null) {
                        index_ = fileParts.indexOf(part6);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part6 = null;
                    }


                    if (gallerypart6 == null) {
                        Log.e("part6", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart6);
                        fileParts.remove(index_);
                        gallerypart6 = null;
                    }


                    galleryfile6 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart6 = prepareFilePart("before_img[]", galleryfile6);
                    fileParts.add(gallerypart6);

                    img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img6.setImageURI(FileUtils.getUri(galleryfile6));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY7 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part7 != null) {
                        index_ = fileParts.indexOf(part7);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part7 = null;
                    }


                    if (gallerypart7 == null) {
                        Log.e("part7", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart7);
                        fileParts.remove(index_);
                        gallerypart7 = null;
                    }


                    galleryfile7 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart7 = prepareFilePart("before_img[]", galleryfile7);
                    fileParts.add(gallerypart7);

                    img7.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img7.setImageURI(FileUtils.getUri(galleryfile7));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY8 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part8 != null) {
                        index_ = fileParts.indexOf(part8);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part8 = null;
                    }


                    if (gallerypart8 == null) {
                        Log.e("part8", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart8);
                        fileParts.remove(index_);
                        gallerypart8 = null;
                    }


                    galleryfile8 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart8 = prepareFilePart("before_img[]", galleryfile8);
                    fileParts.add(gallerypart8);

                    img8.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img8.setImageURI(FileUtils.getUri(galleryfile8));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY9 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part9 != null) {
                        index_ = fileParts.indexOf(part9);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part9 = null;
                    }


                    if (gallerypart9 == null) {
                        Log.e("part5", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart9);
                        fileParts.remove(index_);
                        gallerypart9 = null;
                    }


                    galleryfile9 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart9 = prepareFilePart("before_img[]", galleryfile9);
                    fileParts.add(gallerypart9);

                    img9.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img9.setImageURI(FileUtils.getUri(galleryfile9));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY10 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part10 != null) {
                        index_ = fileParts.indexOf(part10);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part10 = null;
                    }


                    if (gallerypart10 == null) {
                        Log.e("part10", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart10);
                        fileParts.remove(index_);
                        gallerypart10 = null;
                    }


                    galleryfile10 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart10 = prepareFilePart("before_img[]", galleryfile10);
                    fileParts.add(gallerypart10);

                    img10.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    img10.setImageURI(FileUtils.getUri(galleryfile10));

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

            case MY_PERMISSIONS_REQUEST_IMAGE6:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog6();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE7:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog7();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE8:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog8();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE9:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog9();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSIONS_REQUEST_IMAGE10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog10();
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


    public void init() {


        PreSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    preparation = "Completed";
                } else {
                    preparation = "InProgress";
                }
            }
        });


        CoatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    coating = "Completed";
                } else {
                    coating = "InProgress";
                }
            }
        });


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

    }


    public void loadBeforeImages() {

        if (beforeImgCount < 2) {
            hasImage6 = "yes";
            img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(beforeImg.get(0)).into(img6);
        } else if (beforeImgCount < 3) {
            hasImage6 = "yes";
            hasImage7 = "yes";
            img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img7.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(beforeImg.get(0)).into(img6);
            Glide.with(Edit_Customer.this).load(beforeImg.get(1)).into(img7);
        } else if (beforeImgCount < 4) {
            hasImage6 = "yes";
            hasImage7 = "yes";
            hasImage8 = "yes";
            img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img7.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img8.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(beforeImg.get(0)).into(img6);
            Glide.with(Edit_Customer.this).load(beforeImg.get(1)).into(img7);
            Glide.with(Edit_Customer.this).load(beforeImg.get(2)).into(img8);
        } else if (beforeImgCount < 5) {
            hasImage6 = "yes";
            hasImage7 = "yes";
            hasImage8 = "yes";
            hasImage9 = "yes";
            img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img7.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img8.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img9.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(beforeImg.get(0)).into(img6);
            Glide.with(Edit_Customer.this).load(beforeImg.get(1)).into(img7);
            Glide.with(Edit_Customer.this).load(beforeImg.get(2)).into(img8);
            Glide.with(Edit_Customer.this).load(beforeImg.get(3)).into(img9);
        } else if (beforeImgCount == 5) {
            hasImage6 = "yes";
            hasImage7 = "yes";
            hasImage8 = "yes";
            hasImage9 = "yes";
            hasImage10 = "yes";
            img6.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img7.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img8.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img9.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img10.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(beforeImg.get(0)).into(img6);
            Glide.with(Edit_Customer.this).load(beforeImg.get(1)).into(img7);
            Glide.with(Edit_Customer.this).load(beforeImg.get(2)).into(img8);
            Glide.with(Edit_Customer.this).load(beforeImg.get(3)).into(img9);
            Glide.with(Edit_Customer.this).load(beforeImg.get(4)).into(img10);
        }
    }


    public void loadAfterImages() {

        if (afterImgCount < 2) {
            hasImage1 = "yes";
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(afterImg.get(0)).into(img1);
        } else if (afterImgCount < 3) {
            hasImage1 = "yes";
            hasImage2 = "yes";
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(afterImg.get(0)).into(img1);
            Glide.with(Edit_Customer.this).load(afterImg.get(1)).into(img2);
        } else if (afterImgCount < 4) {
            hasImage1 = "yes";
            hasImage2 = "yes";
            hasImage3 = "yes";
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(afterImg.get(0)).into(img1);
            Glide.with(Edit_Customer.this).load(afterImg.get(1)).into(img2);
            Glide.with(Edit_Customer.this).load(afterImg.get(2)).into(img3);
        } else if (afterImgCount < 5) {
            hasImage1 = "yes";
            hasImage2 = "yes";
            hasImage3 = "yes";
            hasImage4 = "yes";
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(afterImg.get(0)).into(img1);
            Glide.with(Edit_Customer.this).load(afterImg.get(1)).into(img2);
            Glide.with(Edit_Customer.this).load(afterImg.get(2)).into(img3);
            Glide.with(Edit_Customer.this).load(afterImg.get(3)).into(img4);
        } else if (afterImgCount == 5) {
            hasImage1 = "yes";
            hasImage2 = "yes";
            hasImage3 = "yes";
            hasImage4 = "yes";
            hasImage5 = "yes";
            img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img3.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img4.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img5.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(Edit_Customer.this).load(afterImg.get(0)).into(img1);
            Glide.with(Edit_Customer.this).load(afterImg.get(1)).into(img2);
            Glide.with(Edit_Customer.this).load(afterImg.get(2)).into(img3);
            Glide.with(Edit_Customer.this).load(afterImg.get(3)).into(img4);
            Glide.with(Edit_Customer.this).load(afterImg.get(4)).into(img5);
        }
    }


}



