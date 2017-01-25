package com.example.fuad.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private FirebaseAuth firebaseAuth;
    private TextView email;
    GoogleMap Googlemaps;
    GoogleApiClient GoogleApiClients;
    Location mLastLocation;
    private DatabaseReference mDatabase;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        email = (TextView) header.findViewById(R.id.email);

        if (user != null) {
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_LONG).show();
            email.setText(user.getEmail());
        }

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Connection Successffuly", Toast.LENGTH_LONG).show();
            //setContentView(R.layout.activity_main);
            initMap();
        } else {

        }

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);

            Toast.makeText(getApplicationContext(), "not granted", Toast.LENGTH_LONG).show();
        } else {
            // permission has been granted, continue as usual
            Toast.makeText(getApplicationContext(), "already granted", Toast.LENGTH_LONG).show();
        }


    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.itemlogout) {
            firebaseAuth.signOut();
            finish();

            startActivity(new Intent(this, LoginActivity.class));
            //startActivity(new Intent(this, TestActivity.class));


        } else if (id == R.id.nav_partner) {
            startActivity(new Intent(this, ListCafe.class));


        } else if (id == R.id.nav_bonus) {
            startActivity(new Intent(this, BonusProgram.class));


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can not connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Googlemaps = googleMap;

        GoogleApiClients = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        GoogleApiClients.connect();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.child("places").getChildren()) {
                        Double lat = data.child("lat").getValue(Double.class);
                        Double lon = data.child("log").getValue(Double.class);

                        LatLng latLng = new LatLng(lat, lon);

                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .title(data.child("name").getValue().toString())
                                .snippet(data.child("title").getValue().toString()));


                    }
                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //venues.

                        //int position = (int)(marker.getTag());
//                        //Using position get Value from arraylist
                        BottomSheetDialog bottomsheetdialog = new BottomSheetDialog(MainActivity.this);
                        View parentView = getLayoutInflater().inflate(R.layout.bottomsheet, null);
                        bottomsheetdialog.setContentView(parentView);

                        TextView snippet = (TextView) parentView.findViewById(R.id.btextViewtitle);
                        snippet.setText(marker.getSnippet());

                        TextView name = (TextView) parentView.findViewById(R.id.textView2);
                        name.setText(marker.getTitle());

                        BottomSheetBehavior bottomsheetbehavior = BottomSheetBehavior.from((View) parentView.getParent());
                        bottomsheetbehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                        bottomsheetbehavior.setPeekHeight(
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
                        bottomsheetdialog.show();
                        return true;
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    LocationRequest LocationRequests;

    public void onConnected(Bundle bundle) {
        LocationRequests = LocationRequest.create();
        LocationRequests.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationRequests.setInterval(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling
             return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(GoogleApiClients, LocationRequests, this);
        }


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Can not get your current location", Toast.LENGTH_LONG).show();

        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                    ) {

                Toast.makeText(getApplicationContext(), "You allowed get your location", Toast.LENGTH_LONG).show();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    Googlemaps.setMyLocationEnabled(true);
                    Googlemaps.getUiSettings().setMyLocationButtonEnabled(true);
                    Googlemaps.getUiSettings().setMapToolbarEnabled(true);
                }
//                Googlemaps.setMyLocationEnabled(true);
//              Googlemaps.getUiSettings().setMyLocationButtonEnabled(true);
//

               LocationRequests = LocationRequest.create();
               LocationRequests.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
               LocationRequests.setInterval(1000);
               onLocationChanged(mLastLocation);

               if (GoogleApiClients != null) {
                   //noinspection MissingPermission
                   LocationServices.FusedLocationApi.requestLocationUpdates(GoogleApiClients, LocationRequests, this);
               }
           } else {
               // Permission was denied or request was cancelled
               Toast.makeText(getApplicationContext(), "You denied get your location", Toast.LENGTH_LONG).show();
           }
       }
   }

    }





