package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CrapperMapperSubmitReview extends BaseActivity implements PostCallbackInterface {

	private final String TAG = "REVIEW";
	
	private TextView name, reviews; 
	private RatingBar rating;
	private EditText review;
	private QuerySingleton qs;
	
	private String pk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_review);
		
		rating  = (RatingBar)findViewById(R.id.ratingBar);
		name    =  (TextView)findViewById(R.id.toiletName);
		reviews =  (TextView)findViewById(R.id.numReviews);
		review  =  (EditText)findViewById(R.id.reviewField);
		
		pk = getIntent().getStringExtra("pk");
		Log.v(TAG, "Loaded review page for: " + pk);
		String toilet = getIntent().getStringExtra("toilet");
		String num_reviews = getIntent().getStringExtra("num_reviews");
		String rank = getIntent().getStringExtra("rank");
		
		name.setText(toilet);
		reviews.setText(num_reviews + " Reviews");
		rating.setRating(3);
		rating.setStepSize(1);
		
		qs = QuerySingleton.getInstance();
	}

	public void submitButton(View view) {
		Log.v(TAG, "Hey");
		if (!qs.loggedIn()) {
			Log.v(TAG, "Not logged in");
			CrapperMapperUser.login(this);
		}
		else {
			Map<String,String> vars = new HashMap<String,String>();
			vars.put("toilet", pk);
			vars.put("content", review.getText().toString());
			vars.put("rank", String.valueOf((int)rating.getRating()));
			qs.sendPost("api/review/create/", vars, this);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crapper_mapper_submit_review, menu);
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
	public void onPostFinished(String result) {
		Log.v("REVIEW", result);
	}

	@Override
	public void onPostError(String error) {
		Log.v(TAG, error);	
	}
}
