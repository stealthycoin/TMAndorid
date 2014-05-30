package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CrapperMapperSingleToiletView extends BaseActivity implements GetCallbackInterface, PostCallbackInterface {

	private QuerySingleton qs;
	
	private TextView name;
	private TextView rating;
	private String lat, lng;
	
	private final String TAG = "VIEW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crapper_mapper_single_toilet_view);

		qs = QuerySingleton.getInstance();
		
		name = (TextView)findViewById(R.id.nameField);
		rating = (TextView)findViewById(R.id.ratingField);
		
		Intent intent = getIntent();
		String pk = intent.getStringExtra("id");
		//pk = "4602";
		Log.v(TAG,"ID is: " + pk);
		Map<String,String> vars = new HashMap<String,String>();
		vars.put("start","0");
		vars.put("end", "10");
		try {
			JSONObject obj = new JSONObject();
			obj.put("pk", pk);
			vars.put("filters", obj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		qs.sendPost("api/Toilet/get/", vars, this);
		
		//start query for reviews
		Map<String,String> vars2 = new HashMap<String,String>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("toilet", pk);
			vars2.put("filters", obj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		qs.sendPost("api/Review/get/", vars2, new PostCallbackInterface() {
			@Override
			public void onPostFinished(String result) {
				Log.v(TAG, "Hey man we got a result: " + result);
			}
		});
	}

	public void getDirections(View v) {
		//gives user choice of maps or a browser
		String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s",lat,lng); 
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		this.startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crapper_mapper_single_toilet_view,
				menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDownloadFinished(String result) {
		Log.v(TAG, result);
	}

	@Override
	public void onPostFinished(String result) {
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0 ; i < array.length(); i++) {
				JSONObject o = array.getJSONObject(i);
				JSONObject fields = o.getJSONObject("fields");
				name.setText("Name: " + fields.getString("name"));
				rating.setText("Rating: " + fields.getString("rating"));
				lat = fields.getString("lat");
				lng = fields.getString("lng");
			}
		}
		catch (JSONException e) {
		}
		Log.v(TAG, result);
	}

}
