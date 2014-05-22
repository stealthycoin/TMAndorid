package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.util.Log;

public class CrapperMapperAdd extends BaseActivity
{
    
    EditText name;
    CheckBox male,female;
    Button submit;
    Location currentLocation;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

	LocationListener locationListener = new MyLocationListener();
	LocationManager lm = (LocationManager) getSystemService(CrapperMapperAdd.LOCATION_SERVICE);

	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, locationListener);

	name = (EditText)findViewById(R.id.username);
	male = (CheckBox)findViewById(R.id.checkBox1);
	female = (CheckBox)findViewById(R.id.checkBox2);
	submit = (Button)findViewById(R.id.button1);

	submit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			if (currentLocation != null) {
				double lat = currentLocation.getLatitude();
				double lng = currentLocation.getLongitude();
				String n = name.getText().toString();
				boolean m = male.isChecked();
				boolean f = female.isChecked();
				
				//roll up request and send it away!
			}
		}
	    });
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

}
