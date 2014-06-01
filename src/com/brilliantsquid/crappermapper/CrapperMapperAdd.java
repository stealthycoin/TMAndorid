package com.brilliantsquid.crappermapper;


import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.Toast;
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

		name = (EditText)findViewById(R.id.username);
		male = (CheckBox)findViewById(R.id.checkBox1);
		female = (CheckBox)findViewById(R.id.checkBox2);
		submit = (Button)findViewById(R.id.button1);
	
		qs = QuerySingleton.getInstance();
		
		MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
		    @Override
		    public void gotLocation(Location location){
		        currentLocation = location;
		    }
		};
		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult, true);
		
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
					args.put("male",   m ? "True" : "False"); //server rejects these 
					args.put("female", f ? "True" : "False"); //and copies user settings
					args.put("lat",String.valueOf(lat));
					args.put("lng",String.valueOf(lng));
					if (qs.loggedIn()) {
						qs.sendPost("api/toilet/create/", args, CrapperMapperAdd.this);
					}
					else {
						Toast.makeText(CrapperMapperAdd.this, "You must be logged in.", Toast.LENGTH_LONG).show();
						CrapperMapperUser.login(CrapperMapperAdd.this);
					}
				}
			}
	    });
    }

	@Override
	public void onPostFinished(String result) {
		if (result == null) result = "null";
		Log.v(TAG, "Add query finished " + result);
	}

	@Override
	public void onPostError(String error) {
		
	}

}
