package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;


public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

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
        switch (item.getItemId()) {
        case R.id.action_search:
        	//Toast.makeText(getApplicationContext(), "SEARCH!", Toast.LENGTH_SHORT).show();
            // search action
            return true;
        case R.id.action_new:
        	Intent intent = new Intent(this,CrapperMapperAdd.class);
		    startActivity(intent);
        	return true;
        case R.id.action_help:
            // help action
            return true;
        case R.id.action_Emergency:
        	
        	return true;
        case R.id.action_Map:
        	
        	return true;
        case R.id.action_account:
        	
        	return true;
        case R.id.action_settings:
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}

}
