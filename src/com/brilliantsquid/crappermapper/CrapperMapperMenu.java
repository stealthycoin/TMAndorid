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
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

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
    private ListView list;
    private LazyAdapter adapter;
    private QuerySingleton qs;
    private boolean firstTick;
    private boolean canLoadMore;
    private boolean callbackFromRefresh;
    private gps location;
    private boolean sort_rating;
    
    RadioButton r_rating;
    
    private Handler toastHandler;
    private Runnable toastRunnable;
    
    private int loadedCount;
    
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        location = new gps(this);
        loadedCount = 10;
        firstTick = true;
        callbackFromRefresh = true;
        canLoadMore = false;
        sort_rating = false;
        
     // these are members in the Activity class
        toastHandler = new Handler();
        toastRunnable = new Runnable() {public void run() {Toast.makeText(CrapperMapperMenu.this,"Loading Nearby Restrooms...", Toast.LENGTH_LONG).show();}};
        //Toast.makeText(CrapperMapperMenu.this, "Loading Nearby Restrooms...", Toast.LENGTH_LONG).show();
        
        //setup list stuff. HAHA just no problem.
		toiletList = new ArrayList<HashMap<String, String>>();
		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, toiletList);        
        list.setAdapter(adapter);
        
        r_rating = (RadioButton) findViewById(R.id.radio_rating);
        
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
        toastHandler.post(toastRunnable);
        server_request(location, 0, 10);
        
        /*
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location newLocation){
                location = newLocation;
                toastHandler.post(toastRunnable);
                server_request(location, 0, 10);
                //Toast.makeText(CrapperMapperMenu.this, "Loading Nearby Restrooms...", Toast.LENGTH_LONG).show();
            }
        };
        Toast.makeText(this, "Getting Location...", Toast.LENGTH_SHORT).show();
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
        */
        //try to log the user in
        CrapperMapperUser.login(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (!firstTick) {
    		callbackFromRefresh = true;
    		toastHandler.post(toastRunnable);
    		server_request(location, 0, loadedCount);
    		//Toast.makeText(CrapperMapperMenu.this, "Loading Nearby Restrooms...", Toast.LENGTH_LONG).show();
    	}
    }
    
    @Override
    protected void onPause() {
    	firstTick = false;
    	super.onPause();
    }

    
	@Override
	public void onPostFinished(String result) {
		if (result != null && callbackFromRefresh) {
			callbackFromRefresh = false;
			toiletList.clear();
			summon_list(result);
		}
		else {
			summon_list(result);
		}
		//list.onRefreshComplete();
		adapter.notifyDataSetChanged();
		canLoadMore = true;
	}
	
	public void do_filter(View v){
		if(r_rating.isChecked()){
			sort_rating = true;
		}else{
			sort_rating = false;
		}
		toiletList.clear();
		adapter.notifyDataSetChanged();
		server_request(location, 0, 10);
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
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void server_request(gps location2, int start, int end) {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		
		location = location2;
		if (location == null) { //bad things are happening and we don't know our current location
			Toast.makeText(this, "Could not get current location.", Toast.LENGTH_LONG).show();
			return; //gtfo
		}
		//get a few toilets, should send current location
		variables.put("start",String.valueOf(start));
        variables.put("current_lat", String.valueOf(location2.getLatitude()));
        variables.put("current_lng", String.valueOf(location2.getLongitude()));
		variables.put("end",String.valueOf(end));
		variables.put("filters", obj.toString());
		if(sort_rating == true){
			variables.put("sortby", "-rating");
		}
        qs.sendPost("api/Toilet/get/", variables, this);
        //Toast.makeText(this, "Loading Nearby Restrooms...", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDownloadFinished(String result) {}

	@Override
	public void onGetError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostError(String error) {
		Toast.makeText(this, "Network Connectivity Issue...", Toast.LENGTH_LONG).show();
	}


}
