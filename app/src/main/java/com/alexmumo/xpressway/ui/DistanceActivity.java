package com.alexmumo.xpressway.ui;

import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.alexmumo.xpressway.R;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

public class DistanceActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Marker entryMarker, exitMarker;
    private DatabaseReference databaseReference;
    TextView distanceText;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        distanceText = findViewById(R.id.tvDistance);
        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String distance = distanceText.getText().toString();
                Intent intent = new Intent(DistanceActivity.this, TripDetailActivity.class);
                intent.putExtra("distance", distance);
                startActivity(intent);
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double entrylatitude = -1.3340096;
        double entrylongitude = 36.8379499;

        //-1.3330374,36.8533269
        //-1.3464533,36.7504017

        double exitlatitude  = -1.2998277;
        double exitlongitude = 36.7607878;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(entrylatitude, entrylongitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Entry"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(entrylatitude, exitlongitude), 15.0f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(entrylatitude, entrylongitude), 15.0f);


        //Calculate distance
        float[] results = new float[1];
        Location.distanceBetween(entrylatitude, entrylongitude, exitlatitude, exitlongitude, results);
        float distance = results[0];

        int kilometres = (int) (distance/1000);
        distanceText.setText("Distance: " + kilometres + "km");
        Toast.makeText(this, String.valueOf(kilometres)+ "Kilometres", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}