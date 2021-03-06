package com.brilliantsquid.crappermapper;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CrapperMapperLocation extends BaseActivity implements 
PostCallbackInterface, 
GetCallbackInterface, 
OnInfoWindowClickListener
{    
	private static final String TAG = "MAP";
	// Google Map
    private GoogleMap googleMap;
    private double lat;
    private double lng;
    private gps location;
    
    static final String KEY_LAT = "lat";
    static final String KEY_LNG = "lng";
    static final String KEY_PK = "pk";
    static final String KEY_ID = "id";
    static final String KEY_TOILET = "toilet";
    ArrayList<HashMap<String, String>> toiletList;
    ListView list;
    LazyAdapter adapter;
    QuerySingleton qs;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        
        qs = QuerySingleton.getInstance();
        QuerySingleton.setContext(this);
        
        
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
            
            	//zoom to current location
            	CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(lat, lng)).zoom(12).build();
            	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            	//show current location with blue dot and enable find me button in top right of map
            	googleMap.setMyLocationEnabled(true); // false to disable
            	googleMap.setOnInfoWindowClickListener(this);
            }
            else{
            	Log.v("HEY", "null");
            }
            

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }        
    }
	
	public void toiletFinder(String result){
		JSONArray jArray = null;

		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				for(int i = 0; jArray!= null && i < jArray.length(); ++i){
					
					try{
						JSONObject obj = jArray.getJSONObject(i);
						JSONObject fields = obj.getJSONObject("fields");
						//Parse out json data
						String toilet = fields.getString("name");
						double lat = Double.valueOf(fields.getString("lat"));
						double lng = Double.valueOf(fields.getString("lng"));
						String pk = obj.getString("pk");
						
						MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng)).title(toilet).snippet(pk);
						googleMap.addMarker(marker);						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
	
	public void server_request() {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
	

		
		Log.v("HEY", "SERVER REQUEST");
        //variables.clear();
        variables.put("start","0");
        variables.put("current_lat", String.valueOf(location.getLatitude()));
        variables.put("current_lng", String.valueOf(location.getLongitude()));
		variables.put("end","100");
		variables.put("filters", obj.toString());
        qs.sendPost("api/Toilet/get/", variables, this);
	}

    @Override
    protected void onResume() {
        super.onResume();
        location = new gps(this);
        server_request();
        
        try {
            initializeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    protected void onPause(){
    	super.onPause();
    }


	@Override
	public void onDownloadFinished(String result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPostFinished(String result) {
		// TODO Auto-generated method stub
		
		while(googleMap == null){
			Thread.yield();
		}
		Log.v("HEY", "Finished:" + result);
		toiletFinder(result);
		
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CrapperMapperLocation.this, CrapperMapperSingleToiletView.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("id", marker.getSnippet());
		startActivity(intent);
	}

	@Override
	public void onGetError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostError(String error) {
		Log.v(TAG,"ERROR: "+ error);
		// TODO Auto-generated method stub
		
	}

}
