package com.brilliantsquid.crappermapper;


import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultsActivity extends BaseActivity {

	private TextView txtQuery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_search_results);
	 
	        // get the action bar
	        ActionBar actionBar = getActionBar();
	 
	        // Enabling Back navigation on Action Bar icon
	        actionBar.setDisplayHomeAsUpEnabled(true);
	 
	        txtQuery = (TextView) findViewById(R.id.txtQuery);
	 
	        handleIntent(getIntent());
	}

	@Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
	
	private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
 
            /**
             * Use this query to display search results like 
             * 1. Getting the data from SQLite and showing in listview 
             * 2. Making webrequest and displaying the data 
             * For now we just display the query only
             */
            Toast.makeText(getApplicationContext(), "SEARCH!", Toast.LENGTH_SHORT).show();
            txtQuery.setText("Search Query: " + query);
 
        }
 
    }


}