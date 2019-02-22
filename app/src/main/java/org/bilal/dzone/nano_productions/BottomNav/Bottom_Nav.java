package org.bilal.dzone.nano_productions.BottomNav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import org.bilal.dzone.nano_productions.Home.HomeFragment;
import org.bilal.dzone.nano_productions.Login.Login_Activity;
import org.bilal.dzone.nano_productions.Login.Login_Fragment;
import org.bilal.dzone.nano_productions.Map.MapsActivity;
import org.bilal.dzone.nano_productions.Personal_Zone.Customer.Customer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Detailer.Detailer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.Importer.Importer_Zone;
import org.bilal.dzone.nano_productions.Personal_Zone.PersonalFragment;
import org.bilal.dzone.nano_productions.R;
import org.bilal.dzone.nano_productions.Search.SearchFragment;

public class Bottom_Nav extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    SparseArray<Fragment> myFragments;
    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_bottom__nav);

        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        user_type = sharedPreferences.getString("user_type", "");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Nav_Helper.disableShiftMode(bottomNavigationView);

        //make array to avoid re creating of fragments
        myFragments = new SparseArray<Fragment>();

        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }

    private void loadFragment(Fragment fragment) {

        //switching fragment
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();

    }

    int id;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        id = item.getItemId();

        if (id == R.id.navigation_home) {
            // get cached instance of the fragment
//            fragment = myFragments.get(1);

            // if fragment doesn't exist in myFragments, create one and add to it
//            if (fragment == null) {
                fragment = new HomeFragment();
                myFragments.put(1, fragment);
//            }

            loadFragment(fragment);

        } else if (id == R.id.navigation_use) {

            getFragmentManager().popBackStack();

//            if (user_type.equals("")) {
//
//
//            } else {

                // get cached instance of the fragment
//                fragment = myFragments.get(4);

                // if fragment doesn't exist in myFragments, create one and add to it
//                if (fragment == null) {


                    switch (user_type) {
                        case "detailer":

                            fragment = new Detailer_Zone();
                            myFragments.put(4, fragment);
                            loadFragment(fragment);
                            break;
                        case "customer":

                            fragment = new Customer_Zone();
                            myFragments.put(4, fragment);
                            loadFragment(fragment);
                            break;
                        case "importer":

                            fragment = new Importer_Zone();
                            myFragments.put(4, fragment);
                            loadFragment(fragment);
                            break;
                        case "empty":

                            fragment = new Login_Fragment();
                            myFragments.put(4, fragment);
                            loadFragment(fragment);
//                            Intent intent = new Intent(this, Login_Activity.class);
////                            startActivity(intent);
//                            startActivityForResult(intent , 101);
                            break;
                    }

                }

                // now load the fragment
//                loadFragment(fragment);

//            }

//        }

        else if (id == R.id.navigation_search) {
            // get cached instance of the fragment
            fragment = myFragments.get(2);

            // if fragment doesn't exist in myFragments, create one and add to it
            if (fragment == null) {
                fragment = new SearchFragment();
                myFragments.put(2, fragment);
            }

            loadFragment(fragment);

        } else if (id == R.id.navigation_map) {
            // get cached instance of the fragment
            fragment = myFragments.get(3);

            // if fragment doesn't exist in myFragments, create one and add to it
            if (fragment == null) {
                fragment = new MapsActivity();
                myFragments.put(3, fragment);
            }

            loadFragment(fragment);

        }
        return true;
    }


    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 101) {
            if (resultCode == RESULT_CANCELED) {

//                onNavigationItemSelected(R.id.navigation_home);
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        getFragmentManager().popBackStack();
        SharedPreferences sharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE);
        user_type = sharedPreferences.getString("user_type", "");

        if (user_type.equals("")){
            user_type = "empty";
        }

//        Toast.makeText(this, user_type, Toast.LENGTH_SHORT).show();
    }
}
