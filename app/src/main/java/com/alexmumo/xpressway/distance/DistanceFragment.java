package com.alexmumo.xpressway.distance;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.alexmumo.xpressway.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DistanceFragment extends FragmentActivity implements OnMapReadyCallback {
    static final String TAG = "MapsActivity";
    private GoogleMap map;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;

    SearchView svLocationOne, svLocationTwo;
    ImageView ivMyLocation, ivClear;
    Button btnUseGoogle, btnFindDistance;
    Polyline polyline = null;
    double polylineDistance;

    LatLng locationOne, locationTwo, currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_distance);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        client = LocationServices.getFusedLocationProviderClient(this);

        //assign variables
        svLocationOne = findViewById(R.id.search_view_location_one);
        svLocationTwo = findViewById(R.id.search_view_location_two);
        ivMyLocation = findViewById(R.id.image_view_my_location);
        ivClear = findViewById(R.id.image_view_clear_map);
        btnFindDistance = findViewById(R.id.button_draw_route);
        btnUseGoogle = findViewById(R.id.button_google_maps);

        //set up listener
        svLocationOne.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //find the location string, create list of addresses
                String sLocationOne = svLocationOne.getQuery().toString().trim();
                startLocationQueryThread(sLocationOne, 1);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        svLocationTwo.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //find the location string, create list of addresses
                String sLocationTwo = svLocationTwo.getQuery().toString().trim();
                startLocationQueryThread(sLocationTwo, 2);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

        ivMyLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getCurrentLocation();
                svLocationOne.setQuery("my location", false);
                svLocationOne.clearFocus();
            }
        });

        ivClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //clear map and searchviews
                svLocationOne.setQuery("", false);
                svLocationTwo.setQuery("", false);
                map.clear();
            }
        });

        btnFindDistance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //if searchview not empty
                if (checkSvForQuery())
                {
                    //if "my location", then find location
                    if (svLocationOne.getQuery().toString().trim().equals("my location"))
                    {
                        locationOne = currentLocation;
                    }
                    //remove old polyline and draw new
                    if (polyline != null)
                    {
                        polyline.remove();
                    }
                    PolylineOptions polylineOptions = new PolylineOptions().add(locationOne, locationTwo).clickable(true);
                    polyline = map.addPolyline(polylineOptions);

                    //calculate polyline length
                    List<LatLng> polylineList = polyline.getPoints();
                    LatLng firstLocation = polylineList.get(0);
                    LatLng secondLocation = polylineList.get(polylineList.size() - 1);

                    polylineDistance = calculatePolylineDistance(firstLocation, secondLocation);

                    Toast.makeText(DistanceFragment.this, "Distance: " + String.format("%.3f", polylineDistance) + "km", Toast.LENGTH_LONG).show();
                }
            }

            private double calculatePolylineDistance(LatLng firstLocation, LatLng secondLocation)
            {
                //Haversine formula
                double earthsRadiusKm = 6371.0088;
                double distanceLat = Math.toRadians(secondLocation.latitude - firstLocation.latitude);
                double distanceLong = Math.toRadians(secondLocation.longitude - firstLocation.longitude);
                double a = Math.sin(distanceLat / 2) * Math.sin(distanceLat / 2) + Math.cos(Math.toRadians(firstLocation.latitude)) * Math.cos(Math.toRadians(secondLocation.latitude)) * Math.sin(distanceLong / 2) * Math.sin(distanceLong / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double d = earthsRadiusKm * c;
                return d;
            }
        });

        btnUseGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //if searchview not empty
                if (checkSvForQuery())
                {
                    //sending to google maps requires a string, not a LatLng
                    String myLocation = svLocationOne.getQuery().toString().trim();

                    //if "my location", then find location
                    if (myLocation.equals("my location"))
                    {
                        Geocoder newMyLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> myList = null;
                        try
                        {
                            myList = newMyLocation.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Address address = (Address) myList.get(0);
                        String addressStr = "";
                        addressStr += address.getAddressLine(0) + ", ";

                        myLocation = addressStr;
                    }

                    String myDestination = svLocationTwo.getQuery().toString().trim();
                    sendToGoogleMaps(myLocation, myDestination);
                }
            }
        });
    }

    private void sendToGoogleMaps(String myLocation, String myDestination)
    {
        //phone requires google maps to send locations
        //check if google maps installed
        try
        {
            //if maps is installed, send data to google maps app
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + myLocation + "/" + myDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException ex)
        {
            //if maps not installed, create intent to playstore
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void getCurrentLocation()
    {
        //check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }

        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if (location != null)
                {
                    mapFragment.getMapAsync(new OnMapReadyCallback()
                    {
                        @Override
                        public void onMapReady(GoogleMap googleMap)
                        {
                            //create new lat and long
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            //create markers
                            MarkerOptions options = new MarkerOptions().position(latLng).title("You are here!");

                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocation();
            }
        }
    }

    private boolean checkSvForQuery()
    {
        boolean queryPresent;

        if (svLocationOne.getQuery().toString().trim().equals("") || svLocationTwo.getQuery().toString().trim().equals(""))
        {
            Toast.makeText(DistanceFragment.this, "location and destination are required", Toast.LENGTH_LONG).show();
            queryPresent = false;
        } else
        {
            queryPresent = true;
        }

        return queryPresent;
    }


    class QueryLocationThread extends Thread
    {
        //We use a new thread to process the location retrieval for performance
        String location;
        int locationNumber;

        //constructor
        QueryLocationThread(String location, int locationNumber)
        {
            this.location = location;
            this.locationNumber = locationNumber;
        }

        @Override
        public void run()
        {
            //Geocoding translates an address or other identifier into a lat and long. Reverse geocoding takes a lat and long and translates it to an address
            Geocoder geocoder = new Geocoder(DistanceFragment.this);
            try
            {
                List<Address> addressList = null;

                //initialize list to location name from geocoder
                //find first location associated with user input
                addressList = geocoder.getFromLocationName(location, 1);

                //if results returned for that location
                if (addressList.size() > 0)
                {
                    Address address = addressList.get(0);

                    //assign new lat and long
                    LatLng locationUsed;
                    if (locationNumber == 1)
                    {
                        locationOne = new LatLng(address.getLatitude(), address.getLongitude());
                        locationUsed = locationOne;
                    } else
                    {
                        locationTwo = new LatLng(address.getLatitude(), address.getLongitude());
                        locationUsed = locationTwo;
                    }

                    //adding markers can only be done from the main thread, therefore we have to use runOnUiThread to go back to main
                    runOnUiThread(() ->
                    {
                        //add new marker and zoom
                        map.addMarker(new MarkerOptions().position(locationUsed).title(location));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(locationUsed, 8));
                    });
                } else
                {
                    runOnUiThread(() ->
                    {
                        Toast.makeText(DistanceFragment.this, "Location not found", Toast.LENGTH_LONG).show();
                    });
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void startLocationQueryThread(String location, int locationNumber)
    {
        if ((location != null) || !location.equals(""))
        {
            QueryLocationThread newThread = new QueryLocationThread(location, locationNumber);
            newThread.start();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;

        //Optional OnMapClickListener to create a marker at any clicked position, displays lat & long in the marker title
        /*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //create marker object at clicked position
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(String.format("Lat: %s. Long: %s", String.format("%.2f", latLng.latitude), String.format("%.2f", latLng.longitude)));
                mMap.addMarker(options);
            }
        });
         */

        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener()
        {
            @Override
            public void onPolylineClick(Polyline polyline)
            {
                Toast.makeText(DistanceFragment.this, "Distance: " + String.format("%.3f", polylineDistance) + "km", Toast.LENGTH_LONG).show();
            }
        });
    }
}