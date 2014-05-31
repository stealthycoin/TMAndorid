package com.brilliantsquid.crappermapper;

import java.io.Serializable;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class CrapperMapperMenu extends BaseActivity implements PostCallbackInterface, GetCallbackInterface
{
	private final String TAG = "MENU";
	private final int STEP_SIZE = 10;
	
    static final String KEY_ID = "id";
    static final String KEY_TOILET = "toilet";
    static final String KEY_STARS = "stars";
    static final String KEY_REVIEWS = "reviews";
    static final String KEY_PK = "pk";
    static final String KEY_MALE = "male";
    static final String KEY_FEMALE = "female";
    static final String KEY_LAT = "lat";
    static final String KEY_LNG = "lng";
    static final String KEY_DISTANCE = "distance";
 
    private ArrayList<HashMap<String, String>> toiletList;
    private PullToRefreshListView list;
    private LazyAdapter adapter;
    private QuerySingleton qs;
    private boolean firstTick;
    private boolean canLoadMore;
    private boolean callbackFromRefresh;
    private Location location;
    //location stuff
    private LocationManager lm;
    private LocationListener locationListener;
    
    private int loadedCount;
    
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadedCount = 10;
        firstTick = true;
        callbackFromRefresh = true;
        canLoadMore = false;
        
        //setup list stuff. HAHA just no problem.
		toiletList = new ArrayList<HashMap<String, String>>();
		list=(PullToRefreshListView)findViewById(R.id.list);
		
		//listener for the refreshing YEAH!
		list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            	Toast.makeText(getApplicationContext(), "Refreshing List Beezy!", Toast.LENGTH_LONG).show();
            	callbackFromRefresh = true;
            	server_request(location, 0, loadedCount);
            }
        });
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, toiletList);        
        list.setAdapter(adapter);
        
     // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//for testing the review submission TODO put this shit back
				Intent intent = new Intent(CrapperMapperMenu.this, CrapperMapperSingleToiletView.class);
				
				intent.putExtra("id", String.valueOf(id));
				intent.putExtra("data", (Serializable)toiletList.get(position));
				startActivity(intent);
				
				
				//Log.v(TAG, "GIVE ME POS: " + position + " GIVE ME PK: " + id);
			}
		});	
        
        
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (canLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount) {
					//OH GOD WE CAN SEE THE END
					//LOAD MORE
					canLoadMore = false;
					Toast.makeText(CrapperMapperMenu.this, "Loading more...", Toast.LENGTH_LONG).show();
					server_request(location, loadedCount, loadedCount + STEP_SIZE);
					loadedCount += STEP_SIZE;
				}
			}
		});
        
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
        		location = locFromGps;
        		CrapperMapperMenu.this.server_request(locFromGps, 0, 10);
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
		if (result != null && callbackFromRefresh) {
			callbackFromRefresh = false;
			toiletList.clear();
			summon_list(result);
		}
		else {
			summon_list(result);
		}
		list.onRefreshComplete();
		adapter.notifyDataSetChanged();
		canLoadMore = true;
	}
	
	
	/**
	 * This summons the toilet list view based on the HTTP Post results
	 * @param result
	 */
	public void summon_list(String result){
		JSONArray jArray = null;
		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		for (int i = 0; jArray!= null && i < jArray.length(); ++i) {
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
				double lat = Double.parseDouble(fields.getString("lat"));
				double lng = Double.parseDouble(fields.getString("lng"));
				double lat_i =  location.getLatitude();
				double lng_i =  location.getLongitude();
				
				double distance = Utilities.gps2m(lat_i, lng_i, lat, lng);
				
				//Put the objects into the listview's hashmap
				map.put(KEY_ID, String.valueOf(pk));
				map.put(KEY_TOILET, toilet);
				map.put(KEY_STARS, String.valueOf(rating));
				map.put(KEY_REVIEWS, String.valueOf(reviews));
				map.put(KEY_MALE, male);
				map.put(KEY_FEMALE, female);
				map.put(KEY_DISTANCE, String.valueOf(distance));
				map.put(KEY_LAT, String.valueOf(lat));
				map.put(KEY_LNG, String.valueOf(lng));

				toiletList.add(map);
				
				Log.v(TAG, "START!!  pk:\n" + pk + "  reviews: " + reviews + "  toilet: " + toilet + " rating: " + rating);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void server_request(Location loc, int start, int end) {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		
		location = loc;
		
		//get a few toilets, should send current location
		variables.put("start",String.valueOf(start));
        variables.put("current_lat", String.valueOf(loc.getLatitude()));
        variables.put("current_lng", String.valueOf(loc.getLongitude()));
		variables.put("end",String.valueOf(end));
		variables.put("filters", obj.toString());
        qs.sendPost("api/Toilet/get/", variables, this);
	}

	@Override
	public void onDownloadFinished(String result) {}


}
