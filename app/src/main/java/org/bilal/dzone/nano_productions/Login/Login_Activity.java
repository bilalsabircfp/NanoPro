package org.bilal.dzone.nano_productions.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

import org.bilal.dzone.nano_productions.Personal_Zone.Customer.Customer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Detailer_Zone;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.URL.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.bilal.dzone.nano_productions.json.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by DzonE on 21-Jul-18.
 */

public class Login_Activity extends AppCompatActivity {

    EditText num, pass;
    Button signin;
    String id, email_, name, server_response, server_response_text, pass_, server_check, api_token,
            user_type = "";
    JSONObject jsonObj;
    CheckBox remember_me;
    RadioButton detailer, customer;
    int checked = 0;
    ImageView back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        num = findViewById(R.id.num);
        pass = findViewById(R.id.pass);
        signin = findViewById(R.id.signin);
        remember_me = findViewById(R.id.remember_me);
        customer = findViewById(R.id.customer);
        detailer = findViewById(R.id.detailer);
        back = findViewById(R.id.imageView);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login_Activity.this.finish();
            }
        });


        remember_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checked == 0) {
                    checked = 1;
                } else
                    checked = 0;

                Toast.makeText(Login_Activity.this, checked + "", Toast.LENGTH_SHORT).show();

            }
        });


        detailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_type = "detailer";
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_type = "customer";
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email_ = num.getText().toString().trim();
                pass_ = pass.getText().toString().trim();


                if ((email_.equals("")))
                    num.setError("Field Can't be empty");

                else if (pass_.equals(""))
                    pass.setError("Field Can't be empty");

                else if (user_type.equals(""))
                    Toast.makeText(Login_Activity.this, "Specify User Type", Toast.LENGTH_SHORT).show();

                else {

                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {

                        awesomeInfoDialog = new AwesomeProgressDialog(Login_Activity.this);
                        awesomeInfoDialog.setTitle("Signing In!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();
                        getUser();

                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });


    }



    AwesomeProgressDialog awesomeInfoDialog;

    //server call
    private void getUser() {
        String urlGetServerData = Url.BaseUrl + "user/login";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();
        if (user_type.equals("customer")) {
            postParam.put("phone_number", pass_);
            postParam.put("warranty_code", email_);
        } else if (user_type.equals("detailer")) {
            postParam.put("password", pass_);
            postParam.put("email", email_);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlGetServerData,
                new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", response + "");
                awesomeInfoDialog.hide();
                try {

                    server_response = response.getString("success");


                    if (server_response.equals("true")) {

                        JSONObject object = response.getJSONObject("user_data");
                        name = object.getString("name");
                        email_ = object.getString("email");
                        id = object.getString("id");
                        user_type = object.getString("user_type");
                        api_token = object.getString("api_token");


                        Log.e("results", name + "\n" + email_ + "\n" + id);


                        Toast.makeText(Login_Activity.this, "Welcome" + " " + name, Toast.LENGTH_SHORT).show();

                        if (checked == 1) {

//                        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                        editor.putString("status", "remember");
//                        editor.putString("user_type", user_type);
//                        editor.putString("name", name);
//                        editor.putString("id", id);
//                        editor.putString("token", api_token);
//
//                        editor.apply();
                            Login_Activity.this.finish();
                        } else {

                            SharedPreferences sharedPreferences = Login_Activity.this.getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("status", "no");
                            editor.putString("user_type", user_type);
                            editor.putString("name", name);
                            editor.putString("id", id);
                            editor.putString("user_type", user_type);
                            editor.putString("token", api_token);

                            editor.apply();


                            Login_Activity.this.finish();


                        }


                    } else if (server_response.equals("false")) {

                        new AwesomeErrorDialog(Login_Activity.this)
                                .setMessage("Invalid Credentials!")
                                .setTitle("Login Failed!")
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

                        new AwesomeErrorDialog(Login_Activity.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(Login_Activity.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();
    }
}
