package com.brilliantsquid.crappermapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class CrapperMapperSingleToiletView extends BaseActivity implements GetCallbackInterface, PostCallbackInterface {

	private QuerySingleton qs;
	
	private TextView name;
	private RatingBar rating;
	private String lat, lng;
	private ImageView gender;
	
	private HashMap<String, String> toilet; 
	private final String TAG = "VIEW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crapper_mapper_single_toilet_view);

		qs = QuerySingleton.getInstance();
		
		name = (TextView)findViewById(R.id.nameField);
		rating = (RatingBar)findViewById(R.id.ratingBar1);
		gender = (ImageView)findViewById(R.id.gender);
		
		Intent intent = getIntent();
		toilet = (HashMap<String,String>)intent.getSerializableExtra("data");
		
		//start query for reviews
		Map<String,String> vars2 = new HashMap<String,String>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("toilet", toilet.get(CrapperMapperMenu.KEY_ID));
			vars2.put("filters", obj.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		//start loading the reviews asap
		qs.sendPost("api/Review/get/", vars2, new PostCallbackInterface() {
			@Override
			public void onPostFinished(String result) {
				Log.v(TAG, "Hey man we got a result: " + result);
			}
		});
		
		Log.v(TAG, toilet.get(CrapperMapperMenu.KEY_MALE));
		Log.v(TAG, toilet.get(CrapperMapperMenu.KEY_FEMALE));
		
		//male symbol
		if (toilet.get(CrapperMapperMenu.KEY_MALE).equals("true") &&
			toilet.get(CrapperMapperMenu.KEY_FEMALE).equals("false")) {
			gender.setImageResource(R.drawable.toilet_men);
			
		}
		//female symbol
		if (toilet.get(CrapperMapperMenu.KEY_MALE).equals("false") &&
			toilet.get(CrapperMapperMenu.KEY_FEMALE).equals("true")) {
			gender.setImageResource(R.drawable.toilet_women);
		}
		
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
				rating.setRating((int)Float.parseFloat(fields.getString("rating")));
				rating.setEnabled(false);
				//rating.setText("Rating: " + fields.getString("rating"));
				lat = fields.getString("lat");
				lng = fields.getString("lng");
			}
		}
		catch (JSONException e) {
		}
		Log.v(TAG, result);
	}

}
