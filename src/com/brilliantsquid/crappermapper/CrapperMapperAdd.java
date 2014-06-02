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
    private gps location;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        location = new gps(this);
        
		name = (EditText)findViewById(R.id.username);
		male = (CheckBox)findViewById(R.id.checkBox1);
		female = (CheckBox)findViewById(R.id.checkBox2);
		submit = (Button)findViewById(R.id.button1);
	
		qs = QuerySingleton.getInstance();
		
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				if (location != null) {
					double lat = location.getLatitude();
					double lng = location.getLongitude();
					String n = name.getText().toString();
					boolean m = male.isChecked();
					boolean f = female.isChecked();
					Toast.makeText(CrapperMapperAdd.this, "CurrentLoc is not null", Toast.LENGTH_LONG).show();
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
				}else{
					Toast.makeText(CrapperMapperAdd.this, "Unable to Find Current Location", Toast.LENGTH_LONG).show();
				}
			}
	    });
    }

	@Override
	public void onPostFinished(String result) {
		Toast.makeText(this, "Created " + name.getText().toString(), Toast.LENGTH_LONG).show();
		finish();
	}

	@Override
	public void onPostError(String error) {
		Toast.makeText(this, "Failed to create restroom", Toast.LENGTH_LONG).show();
	}

}
