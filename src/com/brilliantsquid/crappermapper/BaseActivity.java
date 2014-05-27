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
		Intent intent = null;
        switch (item.getItemId()) {
        case R.id.action_search:
        	intent = new Intent(this,CrapperMapperMenu.class);
		    startActivity(intent);
            return true;
        case R.id.action_new:
        	intent = new Intent(this,CrapperMapperAdd.class);
		    startActivity(intent);
        	return true;
        case R.id.action_help:
            // help action
            return true;
        case R.id.action_Emergency:
        	intent = new Intent(this,CrapperMapperEmergency.class);
		    startActivity(intent);
        	return true;
        case R.id.action_Map:

	    Intent intentLoc = new Intent(this,CrapperMapperLocation.class);
	    startActivity(intentLoc);

        	return true;
        case R.id.action_account:
        	intent = new Intent(this,CrapperMapperUser.class);
		    startActivity(intent);
        	return true;
        case R.id.action_settings:
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
