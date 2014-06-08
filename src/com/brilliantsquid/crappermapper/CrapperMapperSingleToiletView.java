package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
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

public class CrapperMapperSingleToiletView extends BaseActivity implements GetCallbackInterface, PostCallbackInterface {

	private QuerySingleton qs;
	private gps location;
	
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
	
	private HashMap<String, String> toilet; //toilet data
	private LazyReviewAdapter adapter;
	private HashMap<String,String> vars; //filter data
	
	private ArrayList<HashMap<String, String>> reviewlist; //review data
	private ArrayList<ImageView> al; //to store the stars
	private ListView list;
	
	private final String TAG = "VIEW";
	private boolean listPosted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crapper_mapper_single_toilet_view);
		
		reviewlist = new ArrayList<HashMap<String, String>>();
		list = (ListView)findViewById(R.id.list_reviews);
		adapter=new LazyReviewAdapter(this, R.layout.review, reviewlist); 
		list.setAdapter(adapter);

		qs = QuerySingleton.getInstance();
		
		name = (TextView)findViewById(R.id.toilet_page);
		gender = (ImageView)findViewById(R.id.gender_page);
		
        distance = (TextView)findViewById(R.id.distance_page); //distance
        
        reviews = (TextView)findViewById(R.id.reviews_page); // reviews
        
		
		al = new ArrayList<ImageView>();
        stars1 = (ImageView)findViewById(R.id.star1_page); //stars
        stars2 = (ImageView)findViewById(R.id.star2_page);
        stars3 = (ImageView)findViewById(R.id.star3_page);
        stars4 = (ImageView)findViewById(R.id.star4_page);
        stars5 = (ImageView)findViewById(R.id.star5_page);
        
        al.add(stars1);al.add(stars2);al.add(stars3);al.add(stars4);al.add(stars5);
		
		Intent intent = getIntent();
		toilet = (HashMap<String,String>)intent.getSerializableExtra("data");
		
		if (toilet != null) {
			//loaded from the main menu, we are passed a map object so we can easily load everything
			double rev = Double.valueOf(toilet.get(CrapperMapperMenu.KEY_STARS));
	        
	        String dist = toilet.get(CrapperMapperMenu.KEY_DISTANCE);
	        
	        name.setText(toilet.get(CrapperMapperMenu.KEY_TOILET));
	        
	        reviews.setText("Reviews: " + toilet.get(CrapperMapperMenu.KEY_REVIEWS));
	        distance.setText(String.format("%.1f mi", Double.valueOf(dist)));
	        
	        Utilities.display_stars(al, rev);
			
			Log.v(TAG,"Toilet: " + toilet.toString());
			
			//set filter for query of current toilet
			vars = new HashMap<String,String>();
			try {
				JSONObject obj = new JSONObject();
				obj.put("toilet", toilet.get(CrapperMapperMenu.KEY_ID));
				vars.put("filters", obj.toString());
				queryReviews();
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(0 < Integer.parseInt(toilet.get(CrapperMapperMenu.KEY_REVIEWS))){
				Toast.makeText(this, "Loading Reviews...", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this, "No Reviews Exist...", Toast.LENGTH_LONG).show();
			}
			
			
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
		else {
			//loaded from a map, we only know the pk in this case need to make a query for the data
			JSONObject obj = new JSONObject();
			try {
				Log.v(TAG, intent.getStringExtra("id"));
				obj.put("pk", intent.getStringExtra("id"));
				vars = new HashMap<String,String>();
				vars.put("start","0");
				vars.put("end","1");
				vars.put("filters", obj.toString());

		        qs.sendPost("api/Toilet/get/", vars, new PostCallbackInterface() {

					@Override
					public void onPostFinished(String result) {
						try {
							JSONArray jarr = new JSONArray(result);
							JSONObject data = jarr.getJSONObject(0);
							JSONObject tData = data.getJSONObject("fields"); 
							//get the data from the JSON object to the interface
							name.setText(tData.getString("name"));
							boolean male = Boolean.valueOf(tData.getString("male"));
							boolean female = Boolean.valueOf(tData.getString("female"));
							//male symbol
							if (male && !female) {
								gender.setImageResource(R.drawable.toilet_men);
							}
							//female symbol
							else if (!male && female) {
								gender.setImageResource(R.drawable.toilet_women);
							}else{
								gender.setImageResource(R.drawable.toilet_both);
							}
							//review value
							Utilities.display_stars(al, tData.getDouble("rating"));
							//lat and lng
							lat = tData.getString("lat");
							lng = tData.getString("lng");
							//get number of reviews
							reviews.setText("Reviews: " + tData.getString("numberOfReviews"));
							
							vars.clear();
							try {
								JSONObject obj = new JSONObject();
								obj.put("toilet", CrapperMapperSingleToiletView.this.getIntent().getStringExtra("id"));
								vars.put("filters", obj.toString());
								queryReviews();
							}
							catch (JSONException e) {
								e.printStackTrace();
							}
							
						} catch (JSONException e) {
							Toast.makeText(CrapperMapperSingleToiletView.this, "Invalid server response.", Toast.LENGTH_LONG).show();
							finish();
						}
					}

					@Override
					public void onPostError(String error) {
						Toast.makeText(CrapperMapperSingleToiletView.this, "", Toast.LENGTH_LONG).show();
						finish();
					}
		        });
			}
			catch (JSONException e) {
				Toast.makeText(this, "Error on with google maps.", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	public void getDirections(View v) {
		//gives user choice of maps or a browser
		String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s",lat,lng); 
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		this.startActivity(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// Refresh star rating, # of reviews, distance, and review list
		
		location = new gps(this);
		
		//queryReviews();
		
		//query server for updated info on this toilet
		HashMap<String, String> variables = new HashMap<String,String>();
		
		variables.put("start", "0");
		variables.put("current_lat", toilet.get(CrapperMapperMenu.KEY_LAT));
		variables.put("current_lng", toilet.get(CrapperMapperMenu.KEY_LNG));
		variables.put("end",String.valueOf(1));
		variables.put("filters", "{}");
		qs.sendPost("api/Toilet/get/", variables, CrapperMapperSingleToiletView.this);
	}
	
	@Override
	public void onPause() {
		adapter.clear();
		super.onPause();
	}
	
	public void queryReviews() {
		//query server for updated review list
		final Context that = this;
		qs.sendPost("api/Review/get/", vars, new PostCallbackInterface() {
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
	}
	
	public void addReview(View v){
		if (!qs.loggedIn()) {
			Toast.makeText(this, "You have to log in to leave a review.", Toast.LENGTH_LONG).show();
		}
		else {
			Intent intent = new Intent(this, CrapperMapperSubmitReview.class);
			intent.putExtra("toilet", toilet.get(CrapperMapperMenu.KEY_TOILET));
			intent.putExtra("pk", toilet.get("id"));
			intent.putExtra("num_reviews", toilet.get(CrapperMapperMenu.KEY_REVIEWS));
			intent.putExtra("rank", toilet.get(CrapperMapperMenu.KEY_STARS));
			this.startActivity(intent);
		}
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
		//Refresh star rating, # of reviews, distance for this toilet
		try {
			JSONArray array = new JSONArray(result);
			JSONObject o = array.getJSONObject(0);
			JSONObject fields = o.getJSONObject("fields");
			
			double rating = fields.getDouble("rating");
			String numrevs = fields.getString("numberOfReviews");
			
			double lat = fields.getDouble("lat");
			double lng = fields.getDouble("lng");
			double lat_i = location.getLatitude();
			double lng_i = location.getLongitude();
			
			double dist = Utilities.gps2m(lat_i, lng_i, lat, lng);
			
			ArrayList<ImageView> al = new ArrayList<ImageView>();
	        
	        al.add(stars1);al.add(stars2);al.add(stars3);al.add(stars4);al.add(stars5);
	        Utilities.display_stars(al, rating);
	        
	        reviews.setText("Reviews: " + numrevs);
	        distance.setText(String.format("%.1f mi", Double.valueOf(dist)));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		Log.v("VW-PostFinished with:", result+"\n\n");
		
		queryReviews();
	}
	
	public void summon_list(String result){
		JSONArray jArray = null;

		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				for(int i = jArray.length()-1; jArray!= null && i >= 0; --i){

					HashMap<String, String> map = new HashMap<String, String>();
					try{

						JSONObject obj = jArray.getJSONObject(i);
						JSONObject fields = obj.getJSONObject("fields");

						
						//Parse out json data
						String rank = fields.getString("rank");
						String content = fields.getString("content");
						String date_t = fields.getString("date");
						String updown = fields.getString("up_down_rank");
						String review_pk = obj.getString("pk");
						
						// This does things. Things m'lady wouldn't understand
						// For real though, it just parses out the usless data at the first "T"
						String date = date_t.substring(0, date_t.indexOf("T"));
						//Put the objects into the listview's hashmap
						map.put("rank", rank);
						map.put("content", content);
						map.put("date", date);
						map.put("up_down_rank", updown);
						map.put("pk",review_pk);
						
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
