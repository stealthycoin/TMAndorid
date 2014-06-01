package com.brilliantsquid.crappermapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
	private LazyReviewAdapter adapter;
	
	private ArrayList<HashMap<String, String>> reviewlist;
	private ListView list;
	
	private final String TAG = "VIEW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crapper_mapper_single_toilet_view);
		
		reviewlist = new ArrayList<HashMap<String, String>>();
		list = (ListView)findViewById(R.id.list_reviews);
		adapter=new LazyReviewAdapter(this, reviewlist); 
		list.setAdapter(adapter);

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
		
		//apply javascript design pattern, GO TEAM AQUA FORCE
		final Context that = this;
		//start loading the reviews asap
		qs.sendPost("api/Review/get/", vars2, new PostCallbackInterface() {
			@Override
			public void onPostFinished(String result) {
				Log.v(TAG, "Hey man we got a result: " + result);
				summon_list(result);
			}

			@Override
			public void onPostError(String error) {
				Toast.makeText(that, "Failed to download reviews...", Toast.LENGTH_LONG).show();
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
		}else{
			gender.setImageResource(R.drawable.toilet_both);
		}
		
		name.setText(toilet.get(CrapperMapperMenu.KEY_TOILET));
		
		lat = toilet.get(CrapperMapperMenu.KEY_LAT);
		lng = toilet.get(CrapperMapperMenu.KEY_LNG);
	}

	public void getDirections(View v) {
		//gives user choice of maps or a browser
		String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s",lat,lng); 
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		this.startActivity(intent);
	}
	
	public void addReview(View v){
		Intent intent = new Intent(this, CrapperMapperSubmitReview.class);
		intent.putExtra("toilet", toilet.get(CrapperMapperMenu.KEY_TOILET));
		intent.putExtra("pk", toilet.get("id"));
		intent.putExtra("num_reviews", toilet.get(CrapperMapperMenu.KEY_REVIEWS));
		intent.putExtra("rank", toilet.get(CrapperMapperMenu.KEY_STARS));
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
		//summon_list(result);
	}
	
	public void summon_list(String result){
		
		JSONObject jObject = null;
		JSONArray jArray = null;



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
						String rank = fields.getString("rank");
						String content = fields.getString("content");
						String date_t = fields.getString("date");
						String updown = fields.getString("up_down_rank");
						
						// This does things. Things m'lady wouldn't understand
						// For real though, it just parses out the usless data at the first "T"
						String date = date_t.substring(0, date_t.indexOf("T"));
						//Put the objects into the listview's hashmap
						map.put("rank", rank);
						map.put("content", content);
						map.put("date", date);
						map.put("up_down_rank", updown);
						

						
						reviewlist.add(map);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				adapter.notifyDataSetChanged();
				//list=(ListView)findViewById(R.id.list_reviews);
	
				// Getting adapter by passing xml data ArrayList
		               
		        
		        		
	}

	@Override
	public void onPostError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetError(String error) {
		// TODO Auto-generated method stub
		
	}

}
