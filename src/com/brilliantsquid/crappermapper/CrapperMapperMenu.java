package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.view.View;

public class CrapperMapperMenu extends BaseActivity implements PostCallbackInterface, GetCallbackInterface
{
    static final String KEY_ID = "id";
    static final String KEY_TOILET = "toilet";
    static final String KEY_STARS = "stars";
    static final String KEY_REVIEWS = "reviews";
    static final String KEY_PK = "pk";
    static final String KEY_MALE = "male";
    static final String KEY_FEMALE = "female";
    static final String KEY_DISTANCE = "distance";
 
    private ArrayList<HashMap<String, String>> toiletList;
    private ListView list;
    private LazyAdapter adapter;
	private final String TAG = "MENU";
    private QuerySingleton qs;
    private boolean firstTick;
    private Location location;
    //location stuff
    private LocationManager lm;
    private LocationListener locationListener;
    
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        firstTick = true;
        
        qs = QuerySingleton.getInstance();
        QuerySingleton.setContext(this);
        
        //register to get GPS data
        locationListener = new MyLocationListener();
    	lm = (LocationManager) getSystemService(CrapperMapperAdd.LOCATION_SERVICE);
    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, locationListener);
        
        //try to log the user in
        CrapperMapperUser.login(this);
    }

    private final class MyLocationListener implements LocationListener {
    	
        @Override
        public void onLocationChanged(Location locFromGps) {
            // called when the listener is notified with a location update from the GPS
        	if (firstTick) {
        		CrapperMapperMenu.this.server_request(locFromGps);
        		firstTick = false;
        		//after first tick of gps data we don't really want it so frequently
        		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3500, 10, locationListener);
        	}
        	else {
        		//need to make it so that it updates the list based on gps as you move around. not crucial.
        	}
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
		//Log.v(TAG, "Finished getting toilets:\n" + result);
		if (result != null) {
			summon_list(result);
		}
		
	}
	
	
	/**
	 * This summons the toilet list view based on the HTTP Post results
	 * @param result
	 */
	public void summon_list(String result){
		
		JSONObject jObject = null;
		JSONArray jArray = null;
		
		toiletList = new ArrayList<HashMap<String, String>>();


		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				for(int i = 0; jArray!= null && i < jArray.length(); ++i){
					
					HashMap<String, String> map = new HashMap<String, String>();
					try{
						JSONObject obj = jArray.getJSONObject(i);
						JSONObject fields = obj.getJSONObject("fields");
						
						//Parse out json data
						int reviews = fields.getInt("numberOfReviews");
						String toilet = fields.getString("name");
						double rating = Double.valueOf(fields.getString("rating"));
						int pk = obj.getInt("pk");
						String male = fields.getString("male");
						String female = fields.getString("female");
						
						//Get location and calculate distance
						double lat_i = Double.parseDouble(fields.getString("lat"));
						double lng_i = Double.parseDouble(fields.getString("lng"));
						double lat =  location.getLatitude();
						double lng =  location.getLongitude();
						
						double distance = Utilities.gps2m(lat_i, lng_i, lat, lng);
						
						//Put the objects into the listview's hashmap
						map.put(KEY_ID, String.valueOf(pk));
						map.put(KEY_TOILET, toilet);
						map.put(KEY_STARS, String.valueOf(rating));
						map.put(KEY_REVIEWS, String.valueOf(reviews));
						map.put(KEY_MALE, male);
						map.put(KEY_FEMALE, female);
						map.put(KEY_DISTANCE, String.valueOf(distance));

						toiletList.add(map);
						
						Log.v(TAG, "START!!  pk:\n" + pk + "  reviews: " + reviews + "  toilet: " + toilet + " rating: " + rating);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				list=(ListView)findViewById(R.id.list);
	
				// Getting adapter by passing xml data ArrayList
		        adapter=new LazyAdapter(this, toiletList);        
		        list.setAdapter(adapter);
		        
	
		        // Click event for single list row
		        list.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						Intent intent = new Intent(CrapperMapperMenu.this, CrapperMapperSingleToiletView.class);
						intent.putExtra("id", String.valueOf(id));
						intent.putExtra("data", toiletList.get(position));
						startActivity(intent);
						
						//Log.v(TAG, "GIVE ME POS: " + position + " GIVE ME PK: " + id);
					}
				});		
	}
	
	public void server_request(Location loc) {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		
		location = loc;
		
		//get a few toilets, should send current location
		variables.put("start","0");
        variables.put("current_lat", String.valueOf(loc.getLatitude()));
        variables.put("current_lng", String.valueOf(loc.getLongitude()));
		variables.put("end","10");
		variables.put("filters", obj.toString());
        qs.sendPost("api/Toilet/get/", variables, this);
	}

	@Override
	public void onDownloadFinished(String result) {
		// TODO Auto-generated method stub
		
	}


}
