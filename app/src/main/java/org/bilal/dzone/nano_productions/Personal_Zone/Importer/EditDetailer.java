package org.bilal.dzone.nano_productions.Personal_Zone.Importer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditDetailer extends AppCompatActivity {

    EditText name, email, number;
    String Sname, Semail, Snumber, Ssubs, Saddress, api_token, detailerId, lat, lan;
    Button update;
    AwesomeProgressDialog awesomeInfoDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detailer);


        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("token", "");


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        update = findViewById(R.id.update);


        Sname = getIntent().getStringExtra("name");
        Semail = getIntent().getStringExtra("email");
        Snumber = getIntent().getStringExtra("phone_number");
        Ssubs = getIntent().getStringExtra("subs");
        Saddress = getIntent().getStringExtra("address");
        detailerId = getIntent().getStringExtra("detailer_id");
        lat = getIntent().getStringExtra("lat");
        lan = getIntent().getStringExtra("lan");


        name.setText(Sname);
        email.setText(Semail);
        number.setText(Snumber);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Sname = name.getText().toString().trim();
                Semail = email.getText().toString().trim();
                Snumber = number.getText().toString().trim();


                awesomeInfoDialog = new AwesomeProgressDialog(EditDetailer.this);
                awesomeInfoDialog.setTitle("Updating Record!");
                awesomeInfoDialog.setMessage("Please Wait..");
                awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
                awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
                awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
                awesomeInfoDialog.setCancelable(false);
                awesomeInfoDialog.show();
                SendData(Sname, Semail, Snumber, detailerId, lat, lan, Saddress, Ssubs);
            }
        });


    }



    public void SendData(String name,
                         String email,
                         String phone_number,
                         String detailer_id,
                         String latitude,
                         String longitude,
                         String address, String detailerSubscriptions) {

        String urlGetServerData = Url.BaseUrl + "user/edit_detailer";
        System.out.print(urlGetServerData);


        Map<String, String> obj = new HashMap<String, String>();

        obj.put("name", name);
        obj.put("email", email);
        obj.put("ph_no", phone_number);
        obj.put("lat", latitude);
        obj.put("log", longitude);
        obj.put("address", address);
        obj.put("subscription", detailerSubscriptions);
        obj.put("api_token", api_token);
        obj.put("id", detailer_id);


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

                            Toast.makeText(EditDetailer.this,
                                    "Record Updated", Toast.LENGTH_SHORT).show();

                            EditDetailer.this.finish();


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

        RequestQueue requestQueue = Volley.newRequestQueue(EditDetailer.this);
        requestQueue.add(jsonObjectRequest);
    }

}
