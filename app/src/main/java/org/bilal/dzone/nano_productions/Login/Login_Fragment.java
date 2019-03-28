package org.bilal.dzone.nano_productions.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.bilal.dzone.nano_productions.Personal_Zone.Customer.Customer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Detailer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Importer.Importer_Zone;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by DzonE on 21-Jul-18.
 */

public class Login_Fragment extends Fragment {

    EditText num, pass;
    Button signin;
    String id, email_, name, server_response, server_response_text, pass_, server_check, api_token,
            user_type = "", FbToken = "";
    CheckBox remember_me;
    RadioButton detailer, customer;
    int checked = 0;
    ImageView back;
    Fragment fragment = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.login, container, false);


        num = v.findViewById(R.id.num);
        pass = v.findViewById(R.id.pass);
        signin = v.findViewById(R.id.signin);
        remember_me = v.findViewById(R.id.remember_me);
        customer = v.findViewById(R.id.customer);
        detailer = v.findViewById(R.id.detailer);
        back = v.findViewById(R.id.imageView);


        //get token firebase
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(getActivity(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                FbToken = instanceIdResult.getToken();
                Log.e("Token", FbToken);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        remember_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checked == 0) {
                    checked = 1;
                } else
                    checked = 0;

                Toast.makeText(getActivity(), checked + "", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity(), "Specify User Type", Toast.LENGTH_SHORT).show();

                else {

                    if (new Check_internet_connection(getActivity()).isNetworkAvailable()) {

                        awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
                        awesomeInfoDialog.setTitle("Signing In!");
                        awesomeInfoDialog.setMessage("Please Wait..");
                        awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                        awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                        awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                        awesomeInfoDialog.setCancelable(false);
                        awesomeInfoDialog.show();
                        getUser();

                    } else {

                        Toast.makeText(getActivity(),
                                "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });

        return v;

    }


    private void loadFragment(Fragment fragment) {

        //switching fragment
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();

    }


    AwesomeProgressDialog awesomeInfoDialog;

    //server call
    private void getUser() {
        String urlGetServerData = Url.BaseUrl + "user/login";
        System.out.print(urlGetServerData);

        Map<String, String> postParam = new HashMap<String, String>();

        postParam.put("fb_token", FbToken);

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


                        Toast.makeText(getActivity(), "Welcome" + " " + name, Toast.LENGTH_SHORT).show();

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
                            getActivity().finish();
                        } else {

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("status", "no");
                            editor.putString("user_type", user_type);
                            editor.putString("name", name);
                            editor.putString("id", id);
                            editor.putString("token", api_token);

                            editor.apply();

                            if (user_type.equals("customer")) {

                                //remove fragments from backstack
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }

                                fragment = new Customer_Zone();
                                loadFragment(fragment);
                            } else if (user_type.equals("detailer")) {

                                //remove fragments from backstack
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }

                                fragment = new Detailer_Zone();
                                loadFragment(fragment);
                            } else if (user_type.equals("importer")) {

                                //remove fragments from backstack
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                    fm.popBackStack();
                                }

                                fragment = new Importer_Zone();
                                loadFragment(fragment);
                            }


                        }


                    } else if (server_response.equals("false")) {

                        new AwesomeErrorDialog(getActivity())
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


}
