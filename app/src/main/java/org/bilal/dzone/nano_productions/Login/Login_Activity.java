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

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.URL.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.bilal.dzone.nano_productions.json.JsonParser;
import org.json.JSONObject;


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

                        new LOginUser().execute();

                    } else {

                        Toast.makeText(getApplicationContext(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });


    }


    //ASYNTASK JSON//////////////////////////////////////////
    public class LOginUser extends AsyncTask<String, Void, String> {

        AwesomeProgressDialog awesomeInfoDialog;

        @Override
        protected void onPreExecute() {

            awesomeInfoDialog = new AwesomeProgressDialog(Login_Activity.this);
            awesomeInfoDialog.setTitle("Signing In!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();


                if (user_type.equals("customer")) {
                    obj.put("phone_number", pass_);
                    obj.put("warranty_code", email_);
                } else if (user_type.equals("detailer")) {
                    obj.put("password", pass_);
                    obj.put("email", email_);
                }


                String str_req = JsonParser.multipartFormRequestForFindFriends(Url.BaseUrl + "user/login", "UTF-8", obj, null);

                jsonObj = new JSONObject(str_req);


                String c1;


                c1 = jsonObj.getString("success");


                server_response = c1;
                server_response_text = jsonObj.getString("message");


                if (server_response.equals("false")) {
                    server_response_text = jsonObj.getString("message");

                }


                if (server_response.equals("true")) {

                    JSONObject object = jsonObj.getJSONObject("user_data");
                    name = object.getString("name");
                    email_ = object.getString("email");
                    id = object.getString("id");
                    user_type = object.getString("user_type");
                    api_token = object.getString("api_token");


                    Log.e("results", name + "\n" + email_ + "\n" + id);

                }


                server_check = "true";

            } catch (Exception e) {
                e.printStackTrace();

                //server response/////////////////////////
                server_check = "false";
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            //check if server is ok
            if (server_check.equals("false")) {

                awesomeInfoDialog.hide();

                new AwesomeErrorDialog(Login_Activity.this)
                        .setTitle("WARNING")
                        .setMessage("DADAA" +
                                "ADADDD" +
                                "D")
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

            } else

            {


                if (server_response.equals("true")) {

                    awesomeInfoDialog.hide();
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

                        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
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


                } else {

                    awesomeInfoDialog.hide();

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


            }

        }
    }


    @Override
    public void onBackPressed() {

        setResult(RESULT_CANCELED);
        finish();
    }
}
