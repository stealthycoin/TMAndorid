xspackage com.brilliantsquid.crappermapper;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class BaseActivity extends Activity implements PostCallbackInterface {

    private QuerySingleton qs;
    private Location baseLocation;
    private gps location;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_base);
	qs = QuerySingleton.getInstance();
	location = new gps(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        // Associate searchable configuration with the SearchView
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }

	
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
	Intent intent = null;
	QuerySingleton qs = QuerySingleton.getInstance();
        switch (item.getItemId()) {
        case R.id.action_new:
	    intent = new Intent(this,CrapperMapperAdd.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    startActivity(intent);
	    return true;
        case R.id.action_help:
            // help action
            return true;
        case R.id.action_Emergency:
	    Toast.makeText(this, "Finding nearest restroom, one moment...", Toast.LENGTH_SHORT).show();
	    send_for_emergency_room(location);
	    return true;
        case R.id.action_Map:
        	
	    if(android.os.Build.VERSION.SDK_INT >= 11){
		Intent intentLoc = new Intent(this,CrapperMapperLocation.class);
		startActivity(intentLoc);
	    }else{
		Toast.makeText(this, "Minimum API 11 is required to use Maps", Toast.LENGTH_SHORT).show();
	    }
	    return true;
        case R.id.action_account:
	    if(android.os.Build.VERSION.SDK_INT >= 11){
		if (qs.loggedIn()) {
		    intent = new Intent(this,CrapperMapperUserIn.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		    startActivity(intent);
		} else {
		    intent = new Intent(this,CrapperMapperUserOut.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		    startActivity(intent);
		}
	    }else{
		Toast.makeText(this, "Minimum API 11 is required to Login", Toast.LENGTH_SHORT).show();
	    }
        	
	    return true;
        case R.id.action_settings:
        	
	    return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
    public void send_for_emergency_room(gps location2) {
    	Map<String,String> vars = new HashMap<String,String>();
    	vars.put("start","0");
    	vars.put("end","1");
    	vars.put("current_lat", String.valueOf(location2.getLatitude()));
    	vars.put("current_lng", String.valueOf(location2.getLongitude()));
    	vars.put("filters", "{}");
    	qs.sendPost("api/Toilet/get/", vars, new PostCallbackInterface() {

		@Override
		public void onPostFinished(String result) {
		    Intent intent = new Intent(BaseActivity.this ,CrapperMapperSingleToiletView.class);
		    try {
			JSONArray jarray = new JSONArray(result);
			JSONObject obj = jarray.getJSONObject(0);
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
			Map<String,String> map = new HashMap<String,String>();
			map.put(CrapperMapperMenu.KEY_ID, String.valueOf(pk));
			map.put(CrapperMapperMenu.KEY_TOILET, toilet);
			map.put(CrapperMapperMenu.KEY_STARS, String.valueOf(rating));
			map.put(CrapperMapperMenu.KEY_REVIEWS, String.valueOf(reviews));
			map.put(CrapperMapperMenu.KEY_MALE, male);
			map.put(CrapperMapperMenu.KEY_FEMALE, female);
			map.put(CrapperMapperMenu.KEY_DISTANCE, String.valueOf(distance));
			map.put(CrapperMapperMenu.KEY_LAT, String.valueOf(lat));
			map.put(CrapperMapperMenu.KEY_LNG, String.valueOf(lng));
					
			intent.putExtra("data", (Serializable)map);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
					
		    } catch (JSONException e) {
			e.printStackTrace();
		    }
		}

		@Override
		public void onPostError(String error) {
		    Toast.makeText(BaseActivity.this, "Really sorry... we couldn't find anything.", Toast.LENGTH_LONG).show();
		}
	    });
    }

    @Override
    public void onPostFinished(String result) {
	// TODO Auto-generated method stub
		
    }

    @Override
    public void onPostError(String error) {
	// TODO Auto-generated method stub
		
    }
}
