package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class CrapperMapperSingleToiletView extends BaseActivity implements GetCallbackInterface, PostCallbackInterface {

	private QuerySingleton qs;
	
	private TextView name;
	private TextView rating;
	
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
		pk = "4602";
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_crapper_mapper_single_toilet_view,
					container, false);
			return rootView;
		}
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
			}
		}
		catch (JSONException e) {
			
		}
		Log.v(TAG, result);
		
	}

}
