package org.bilal.dzone.nano_productions.Personal_Zone.Importer;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Add_Customer;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.API.UploadApi;
import org.bilal.dzone.nano_productions.Utils.FileUtils;
import org.bilal.dzone.nano_productions.Utils.ResponseData;
import org.bilal.dzone.nano_productions.Utils.Url;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_Detailer extends AppCompatActivity {

    AwesomeProgressDialog awesomeInfoDialog;
    EditText EtName, EtNumber, EtEmail, EtSubs, EtPassword, TvLocation;
    String Sname, Snumber, Semail, Ssubs, Spassword, Slocation = "", api_token;
    CircleImageView imageView;
    Button submit;
    ImageView back;
    double longitude = 0.0;
    double latitude = 0.0;

    private static final int GALLERY1 = 300;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE1 = 0x1;
    private static final int REQUEST_TAKE_PHOTO1 = 100;

    File camerafile1;
    File galleryfile1;
    Uri file_uri;
    private String mCurrentPhotoPath;
    private File compressedfile1;
    private List<MultipartBody.Part> fileParts = new ArrayList<>();
    MultipartBody.Part part1;
    MultipartBody.Part gallerypart1;
    String flag = "false";
    LinearLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__detailer);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");

        EtName = findViewById(R.id.name);
        EtNumber = findViewById(R.id.number);
        EtEmail = findViewById(R.id.email);
        EtSubs = findViewById(R.id.subs);
        EtPassword = findViewById(R.id.password);
        TvLocation = findViewById(R.id.location);
        imageView = findViewById(R.id.profile_image);
        submit = findViewById(R.id.add);
        back = findViewById(R.id.imageView);
        mainLayout = findViewById(R.id.linear);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add_Detailer.this.finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sname = EtName.getText().toString().trim();
                Snumber = EtNumber.getText().toString().trim();
                Semail = EtEmail.getText().toString().trim();
                Spassword = EtPassword.getText().toString().trim();
                Ssubs = EtSubs.getText().toString().trim();
                Slocation = TvLocation.getText().toString().trim();

                if (Sname.equals("")) {
                    EtName.setFocusable(true);
                    EtName.setError("Enter Name");
                } else if (Snumber.equals("")) {
                    EtNumber.setFocusable(true);
                    EtNumber.setError("Enter Number");
                } else if (Semail.equals("")) {
                    EtEmail.setFocusable(true);
                    EtEmail.setError("Enter Email");
                } else if (!Semail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")
                        && !Semail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+")) {
                    EtEmail.setError("Invalid Email Address");
                } else if (Spassword.equals("")) {
                    EtPassword.setFocusable(true);
                    EtPassword.setError("Enter Password");
                } else if (Ssubs.equals("")) {
                    EtSubs.setFocusable(true);
                    EtSubs.setError("Enter Subscriptions");
                } else if (Slocation.equals("")) {
                    TvLocation.setFocusable(true);
                    TvLocation.setError("Enter Location");
                } else if (flag.equals("false") || fileParts.size() == 0) {
                    Snackbar snackbar = Snackbar
                            .make(mainLayout, "Select an Image", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {


                    if (latitude == 0.0) {
                        getLocationAndSend(Slocation);
                    } else {

                        awesomeInfoDialog = new AwesomeProgressDialog(Add_Detailer.this);
                        awesomeInfoDialog.setTitle("Adding Record!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();
                        UploadCode();
                    }

                }

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileParts.clear();
                flag = "true";
                Slocation = TvLocation.getText().toString().trim();
                if (!Slocation.equals("")) {
                    getLocation(Slocation);
                }

                if (ActivityCompat.checkSelfPermission(Add_Detailer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Detailer.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Add_Detailer.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();

                } else {
                    ActivityCompat.requestPermissions(Add_Detailer.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE1);
                }
            }
        });

    }


    private void UploadCode() {

        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("name", createPartFromString(Sname));
        map.put("email", createPartFromString(Semail));
        map.put("ph_no", createPartFromString(Snumber));
        map.put("lat", createPartFromString(latitude + ""));
        map.put("log", createPartFromString(longitude + ""));
        map.put("pass", createPartFromString(Spassword));
        map.put("address", createPartFromString(Slocation));
        map.put("subscription", createPartFromString(Ssubs));
        map.put("api_token", createPartFromString(api_token));

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
        Call<ResponseData> call = rest.uploadData(map, fileParts);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                Log.e("ServerResponse", response.body().getSuccess() + "/" + response.body().getStatus()
                        + "/" + response.body().getMessage());
                Log.e("ServerResponse", response.toString());

                if (response.isSuccessful()) {
                    awesomeInfoDialog.hide();


                    if (response.body().getSuccess() != null &&
                            response.body().getMessage().equals("Please Review for All Fields")) {
                        Toast.makeText(Add_Detailer.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Add_Detailer.this, "Detailer Created Succssfully", Toast.LENGTH_SHORT).show();
                        Add_Detailer.this.finish();
                    }


                } else {
                    awesomeInfoDialog.hide();
                    Log.e("serverResponse", response.toString());
                    Toast.makeText(Add_Detailer.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("FailuerResponse", t.toString());
                awesomeInfoDialog.hide();
                Toast.makeText(Add_Detailer.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openImageDialog1() {
        final Dialog sheetDialog = new Dialog(Add_Detailer.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(Add_Detailer.this, "org.bilal.dzone.nano_productions.fileprovider", camerafile1);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {
                    compressedfile1 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile1);
                    delete(camerafile1);
                    //image parameter name
                    part1 = prepareFilePart("file", compressedfile1);
                    fileParts.add(part1);

                    imageView.setImageURI(FileUtils.getUri(compressedfile1));

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
                    galleryfile1 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart1 = prepareFilePart("file", galleryfile1);
                    fileParts.add(gallerypart1);

                    imageView.setImageURI(FileUtils.getUri(galleryfile1));

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


    public void getLocation(final String Slocation) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Geocoder gcdd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addressList = null;
                try {
                    addressList = gcdd.getFromLocationName(Slocation, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Add_Detailer.this, "Address not Found", Toast.LENGTH_SHORT).show();
                }

                try {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    longitude = latLng.longitude;
                    latitude = latLng.latitude;

                    Log.e("latLang", latLng.toString());
                } catch (Exception e) {
                    Log.e("address", "address not found");
                    latitude = 30.988731;
                    longitude = 34.927487;

                }


            }
        }, 3000);
    }


    public void getLocationAndSend(final String Slocation) {

        awesomeInfoDialog = new AwesomeProgressDialog(Add_Detailer.this);
        awesomeInfoDialog.setTitle("Adding Record!");
        awesomeInfoDialog.setMessage("Please Wait..");
        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
        awesomeInfoDialog.setCancelable(false);
        awesomeInfoDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Geocoder gcdd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addressList = null;
                try {
                    addressList = gcdd.getFromLocationName(Slocation, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    awesomeInfoDialog.hide();
                    Toast.makeText(Add_Detailer.this, "catch", Toast.LENGTH_SHORT).show();
                }


                try {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    longitude = latLng.longitude;
                    latitude = latLng.latitude;

                    awesomeInfoDialog.hide();
                    UploadCode();

                    Log.e("latLang", latLng.toString());
                } catch (Exception e) {
                    Log.e("address", "address not found");
                    latitude = 30.988731;
                    longitude = 34.927487;

                    awesomeInfoDialog.hide();
                    UploadCode();

                }


            }
        }, 3000);
    }



}
