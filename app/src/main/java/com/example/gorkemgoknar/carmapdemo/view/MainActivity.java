package com.example.gorkemgoknar.carmapdemo.view;

import android.Manifest;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.example.gorkemgoknar.carmapdemo.R;
import com.example.gorkemgoknar.carmapdemo.model.Persistence;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;

    private FloatingActionButton refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        checkAndRequestLocationPermission();

        //Button to refresh url content
        refreshButton = (FloatingActionButton) this.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Trigger refresh

                //TODO : better inherit list and map fragments from common base for same functionality

                ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag("ListFragment");
                if (listFragment != null){
                    listFragment.fetchDataFromNet();
                    return;
                }

                MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("MapFragment");
                if (mapFragment != null){
                    mapFragment.fetchDataFromNet();
                    return;
                }
            }
        });
        //show list by default
        displaySelectedScreen(R.id.nav_list);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        String fragment_tag = null;
        //initializing the fragment object which is selected
        //Note that each time we are creating new fragment not reusing old
        //will use local cache once app got the items.

        //TODO: Save fragment state and reuse old one if performance is a problem

        switch (itemId) {
            case R.id.nav_list:

                fragment = new ListFragment();
                fragment_tag = "ListFragment";

                break;
            case R.id.nav_map:

                fragment = new MapFragment();
                fragment_tag = "MapFragment";

                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, fragment_tag);
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    public void checkAndRequestLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        //If user accepts or reject some permission handle it here
        //need to do this in activity.


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted!
                    // set it persistent
                    Persistence.setLocationPermission(true);
                } else {
                    // permission denied!
                    Persistence.setLocationPermission(false);
                }
                return;
            }
        }
    }




}
