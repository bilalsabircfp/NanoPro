package org.bilal.dzone.nano_productions.Map;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Utils.Url;
import org.bilal.dzone.nano_productions.json.Check_internet_connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    AwesomeProgressDialog awesomeInfoDialog;
    ArrayList<Maps_ModelClass> arrayList = new ArrayList<Maps_ModelClass>();
    String all, name_, num_, email, image;
    String[] all_info;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (new Check_internet_connection(getActivity().getApplicationContext()).isNetworkAvailable()) {

            awesomeInfoDialog = new AwesomeProgressDialog(getActivity());
            awesomeInfoDialog.setTitle("Loading Data!");
            awesomeInfoDialog.setMessage("Please Wait..");
            awesomeInfoDialog.setDialogBodyBackgroundColor(R.color.bottom_nav);
            awesomeInfoDialog.setColoredCircle(R.color.dialogInfoBackgroundColor);
            awesomeInfoDialog.setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white);
            awesomeInfoDialog.setCancelable(false);
            awesomeInfoDialog.show();
            getServerData();

        } else {

            Toast.makeText(getActivity().getApplicationContext(),
                    "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }


        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //add custom map style
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_json));
            if (!success) {
                // Handle map style load failure
            }
        } catch (Resources.NotFoundException e) {
            // Oops, looks like the map style resource couldn't be found!
        }


        //show marker window
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                all = marker.getSnippet();
                all_info = all.split("%");
                name_ = all_info[0];
                num_ = all_info[1];
                email = all_info[2];
                image = all_info[3];


                marker_dialog(name_, num_, email,  "http://www.nano.nanopro-group.com/"+image);

            }
        });


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

    }


    Dialog dialog;

    public void marker_dialog(String name, final String number, final String email, String image) {


        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        window.setAttributes(wlp);
        wlp.y = 150;

        dialog.setContentView(R.layout.dialog_info);

        TextView tv_name = dialog.findViewById(R.id.name);
        TextView tv_num = dialog.findViewById(R.id.email);
        TextView tv_blood = dialog.findViewById(R.id.number);
        ImageView img = dialog.findViewById(R.id.profile_image);
        Button cross, call;
        cross = dialog.findViewById(R.id.cross);
        call = dialog.findViewById(R.id.call);


        tv_name.setText(name);
        tv_num.setText(number);
        tv_blood.setText(email);

        Log.e("ImagePath", image);

        Glide.with(getActivity()).load(image).into(img);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                make_call(email);
            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }



    //windowinfo class
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            final String all_string, d_name, d_image; String[] after_split;
            all_string = marker.getTitle();
            after_split = all_string.split("%");
            d_name = after_split[0];
            d_image = after_split[1];

            TextView tvTitle = ( myContentsView.findViewById(R.id.title));
            final ImageView imageView = ( myContentsView.findViewById(R.id.img));

            Glide.with(myContentsView).load(R.drawable.logo_new);



            tvTitle.setText(d_name);


            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }




    //server call
    private void getServerData() {
        String urlGetServerData = Url.BaseUrl + "user/map-users";
        System.out.print(urlGetServerData);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlGetServerData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", response + "");
                        awesomeInfoDialog.hide();
                        try {
                            Gson gson = new Gson();
                            JSONArray jsonArray = response.getJSONArray("detailers");

                            //get single object
                            String kss = response.getString("success");
                            Log.e("kss", kss + "");

                            for (int p = 0; p < jsonArray.length(); p++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(p);
                                Maps_ModelClass mClass = gson.fromJson(String.valueOf(jsonObject),
                                        Maps_ModelClass.class);
                                arrayList.add(mClass);

                            }


//                            Toast.makeText(getActivity(), arrayList.get(0).getLatitude() + "" +
//                                    arrayList.get(0).getLongitude(), Toast.LENGTH_SHORT).show();

                            if (arrayList.size() > 0) {


                                Marker marker_ = null;

                                for (int i = 0; i < arrayList.size(); i++) {


//                                    set markers on map
                                    final LatLng name_ = new LatLng(Double.parseDouble(arrayList.get(i).getLatitude()),
                                            Double.parseDouble(arrayList.get(i).getLongitude()));
                                    marker_ = mMap.addMarker(new MarkerOptions()
                                            .position(name_)
                                            .title(arrayList.get(i).getName() +
                                            "%"+ arrayList.get(i).getImage() )
                                            .snippet(arrayList.get(i).getName() + "%" +
                                                    arrayList.get(i).getAddress() + "%" +
                                                    arrayList.get(i).getPhone_number() +
                                                    "%" + arrayList.get(i).getImage()
                                            ));

                                }

                                int length = arrayList.size() - 1;
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                        new LatLng(Double.parseDouble(arrayList.get(length).getLatitude()),
                                                Double.parseDouble(arrayList.get(length).getLongitude()))).zoom(11).build();

                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());


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
                        Toast.makeText(getActivity(), "Server Timeout!", Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }



    public void make_call(String phone){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            // permission has been granted, continue as usual
//            Toast.makeText(this, "accessed", Toast.LENGTH_SHORT).show();

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ phone));

            startActivity(callIntent);
        }

    }


}
