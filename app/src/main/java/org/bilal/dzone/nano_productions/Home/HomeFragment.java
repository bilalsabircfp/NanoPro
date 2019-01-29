package org.bilal.dzone.nano_productions.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.bilal.dzone.nano_productions.BottomNav.Bottom_Nav;
import org.bilal.dzone.nano_productions.Gallery.Gallery;
import org.bilal.dzone.nano_productions.Login.Login_Activity;
import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Detailer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.PersonalFragment;
import org.bilal.dzone.nano_productions.Personal_Zone.Products.Products;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.SearchFragment;

import io.fabric.sdk.android.Fabric;


public class HomeFragment extends Fragment {

    Button login, logout, share;
    RelativeLayout cars;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        String user_type = sharedPreferences.getString("user_type", "");

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize the crashlytics
        Fabric.with(getActivity(), new Crashlytics());

        login = v.findViewById(R.id.login);
        cars  = v.findViewById(R.id.r_cars);
        logout = v.findViewById(R.id.logout);
        share = v.findViewById(R.id.share);



        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "NANOPRO");
                    String sAux = "\nLet me recommend you this application\n";
                    sAux = sAux + "http://www.nanopro-group.com/app";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });



        if (user_type.equals("")){
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }else {
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new PersonalFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), Login_Activity.class);
                startActivity(intent);

            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        getFragmentManager().popBackStack();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);

        String s = sharedPreferences.getString("name", "");
        String ss = sharedPreferences.getString("user_type", "");

        if (s.equals("")){
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }else {
            logout.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);
        }



    }


    Fragment fragment = null;

    private void logout(){

        //Creating an alert dialog to confirm logout

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Logout!");
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getActivity().getSharedPreferences("DataStore", Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Putting blank value to number
                        editor.putString("name", "");
                        editor.putString("user_type", "");

                        //Saving the sharedpreferences
                        editor.clear();
                        editor.apply();

                        login.setVisibility(View.GONE);
                        logout.setVisibility(View.VISIBLE);

                        intent = new Intent(getActivity(), Login_Activity.class);
                        startActivity(intent);

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




}