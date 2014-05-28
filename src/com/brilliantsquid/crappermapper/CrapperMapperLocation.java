package com.brilliantsquid.crappermapper;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

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

public class CrapperMapperLocation extends BaseActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener
{    
 // Google Map
    private GoogleMap googleMap;

    private Location currentLocation;
    private double lat;
    private double lng;
    private boolean coordinates;
    private LocationClient mLocationClient;
    private gps location;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        //coordinates = false;
        location = new gps(this);
        
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


            
            if (location != null){
            	lat = location.getLatitude();
            	lng = location.getLongitude();
            
            	CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(lat, lng)).zoom(12).build();
	 
            	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }else{
            	Log.v("HEY", "FUCKER");
            }
            googleMap.setMyLocationEnabled(true); // false to disable

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
        //initializeMap();
    }
 
    @Override
    protected void onPause(){
    	super.onPause();
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
}
