package com.cmpe277.sjsu.ishare;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MapActivity extends android.support.v4.app.FragmentActivity implements LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_map);
        mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        googleMap = mapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = /*LocationManager.GPS_PROVIDER;*/  locationManager.getBestProvider(criteria, true);
        mLocation = locationManager.getLastKnownLocation(bestProvider);
        if (mLocation != null) {
            onLocationChanged(mLocation);
        }
        // locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        locationManager.requestSingleUpdate(bestProvider,this, null);
        Log.i("MAP", "test");
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition position) {
                // When the camera changes, update the query
                doMapQuery();
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick (Marker marker){
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" +
                                mLocation.getLatitude() + "," +
                                mLocation.getLongitude() + "&daddr=" +
                                marker.getPosition().latitude + "," +
                                marker.getPosition().longitude));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        //TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        mLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        /*googleMap.addMarker(new MarkerOptions().position(latLng));*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private void doMapQuery() {
        ParseQuery<IShareItem> mapQuery = ParseQuery.getQuery(IShareItem.class);
        /*
        mapQuery.whereWithinKilometers("location", myPoint, MAX_POST_SEARCH_DISTANCE);
        // 5
        mapQuery.include("user");
        mapQuery.orderByDescending("createdAt");

        mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
        */
        // 6
        mapQuery.whereEqualTo("requested", "false");
        mapQuery.findInBackground(new FindCallback<IShareItem>() {
            @Override
            public void done(List<IShareItem> objects, ParseException e) {

                if(e != null)
                    Log.d("MAP", e.getMessage());
                // Handle the results
                for (IShareItem item : objects){
                    Log.d("MAP", "item added");
                    try {
                        mapFragment.getMap().addMarker((new MarkerOptions())
                            .position(new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude()))
                            .title(item.getTitle())
                            .snippet(item.getDescription())
                        );
                    }
                    catch(Exception ex){
                        Log.w("MAP", "wasn't able to add marker");
                    };

                }

            }
        });
    }
}