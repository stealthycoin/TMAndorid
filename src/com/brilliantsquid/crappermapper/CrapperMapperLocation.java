package com.brilliantsquid.crappermapper;

//import com.brilliantsquid.crappermapper.CrapperMapperAdd.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

public class CrapperMapperLocation extends BaseActivity
{    
 // Google Map
    private GoogleMap googleMap;
    Location currentLocation;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
 
        try {
            // Loading map
            initializeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
	private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            
            googleMap.setMyLocationEnabled(true); // false to disable
            
            //getMyLocation() is deprecated apparently, which i think is why it no work
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude())).zoom(12).build();
     
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            /*
            
            LocationListener locationListener = new MyLocationListener();
        	LocationManager lm = (LocationManager) getSystemService(CrapperMapperAdd.LOCATION_SERVICE);

        	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);
         // latitude and longitude
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();
           
            
            Log.v("stuff",String.valueOf(latitude));
            Log.v("stuff",String.valueOf(longitude));
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");
             
            // adding marker
            googleMap.addMarker(marker);*/
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        /*
*/
        
    }
	
	
    private final class MyLocationListener implements LocationListener {
    	
        @Override
        public void onLocationChanged(Location locFromGps) {
            // called when the listener is notified with a location update from the GPS
        	currentLocation = locFromGps;
        }
	
        @Override
        public void onProviderDisabled(String provider) {
	    // called when the GPS provider is turned off (user turning off the GPS on the phone)
        }
	
        @Override
        public void onProviderEnabled(String provider) {
	    // called when the GPS provider is turned on (user turning on the GPS on the phone)
        }
	
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
	    // called when the status of the GPS provider changes
        }
    }
	 
    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }
 
    @Override
    protected void onPause(){
    	super.onPause();
    }
}
