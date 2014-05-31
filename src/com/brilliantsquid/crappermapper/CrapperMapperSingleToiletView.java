package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CrapperMapperSingleToiletView extends BaseActivity implements GetCallbackInterface, PostCallbackInterface {

	private QuerySingleton qs;
	
	private TextView name;
	
	private TextView distance;
	
	private TextView reviews;
	
	//private RatingBar rating;
	private String lat, lng;
	private ImageView gender;
	private ImageView stars1;
	private ImageView stars2;
	private ImageView stars3;
	private ImageView stars4;
	private ImageView stars5;
	
	private HashMap<String, String> toilet; 
	private final String TAG = "VIEW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crapper_mapper_single_toilet_view);

		qs = QuerySingleton.getInstance();
		
		name = (TextView)findViewById(R.id.toilet_page);
		gender = (ImageView)findViewById(R.id.gender_page);
		
        distance = (TextView)findViewById(R.id.distance_page); //distance
        
        reviews = (TextView)findViewById(R.id.reviews_page); // reviews
		
		ArrayList<ImageView> al = new ArrayList<ImageView>();
        stars1 = (ImageView)findViewById(R.id.star1_page); //stars
        stars2 = (ImageView)findViewById(R.id.star2_page);
        stars3 = (ImageView)findViewById(R.id.star3_page);
        stars4 = (ImageView)findViewById(R.id.star4_page);
        stars5 = (ImageView)findViewById(R.id.star5_page);
        
        al.add(stars1);al.add(stars2);al.add(stars3);al.add(stars4);al.add(stars5);
		
		Intent intent = getIntent();
		toilet = (HashMap<String,String>)intent.getSerializableExtra("data");
		
		double rev = Double.valueOf(toilet.get(CrapperMapperMenu.KEY_STARS));
        
        String dist = toilet.get(CrapperMapperMenu.KEY_DISTANCE);
        
        name.setText(toilet.get(CrapperMapperMenu.KEY_TOILET));
        
        reviews.setText("Reviews: " + toilet.get(CrapperMapperMenu.KEY_REVIEWS));
        distance.setText(String.format("%.1f mi", Double.valueOf(dist)));
        
        Utilities.display_stars(al, rev);
		
		Log.v("TAG","Toilet: " + toilet.toString());
		
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
		//else it gets left as the combo.
		
		//rating.setRating(Float.parseFloat(toilet.get(CrapperMapperMenu.KEY_STARS)));
		//rating.setEnabled(false);
		
		name.setText("Name: " + toilet.get(CrapperMapperMenu.KEY_TOILET));
		
		lat = toilet.get(toilet.get(CrapperMapperMenu.KEY_LAT));
		lng = toilet.get(toilet.get(CrapperMapperMenu.KEY_LNG));
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
				//rating.setRating((int)Float.parseFloat(fields.getString("rating")));
				//rating.setEnabled(false);
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
