package com.brilliantsquid.crappermapper;


import java.util.HashMap;
import java.util.Map;

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

public class CrapperMapperAdd extends BaseActivity implements PostCallbackInterface
{
    final String TAG = "ADD";
    EditText name;
    CheckBox male,female;
    Button submit;
    Location currentLocation;
    QuerySingleton qs;
    
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

	qs = new QuerySingleton(this);
	
	submit.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			if (currentLocation != null) {
				double lat = currentLocation.getLatitude();
				double lng = currentLocation.getLongitude();
				String n = name.getText().toString();
				boolean m = male.isChecked();
				boolean f = female.isChecked();
				
				//roll up request and send it away!
				Map<String,String> args = new HashMap<String,String>();
				args.put("name", n);
				args.put("male",   m ? "True" : "False");
				args.put("female", f ? "True" : "False");
				args.put("lat",String.valueOf(lat));
				args.put("lng",String.valueOf(lng));
				Log.v(TAG, args.toString());
				
				Map<String,String> auth = new HashMap<String,String>();
				auth.put("username","toilet");
				auth.put("password", "jcrowepoops667");
				qs.sendPost("api/user/login/", auth, new PostCallbackInterface() {
					@Override
					public void onPostFinished(String result) {
						Log.v(TAG, "Maybe logged in");
					}
				});
				qs.sendPost("api/toilet/create/", args, CrapperMapperAdd.this);
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

	@Override
	public void onPostFinished(String result) {
		// TODO Auto-generated method stub
		
	}

}
