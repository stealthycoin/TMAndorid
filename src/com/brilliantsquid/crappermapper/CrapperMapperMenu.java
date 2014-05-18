package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class CrapperMapperMenu extends Activity
{

    ImageButton emergencyButton, listButton, addButton, recentButton, locationButton, userButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        emergencyButton = (ImageButton)findViewById(R.id.button1);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperEmergency.class);
		    startActivity(intent);
		}
	    });
	listButton = (ImageButton)findViewById(R.id.button2);
	listButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperList.class);
		    intent.putExtra("key", "");
		    startActivity(intent);
		}
	    });
	addButton = (ImageButton)findViewById(R.id.button3);
	addButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperAdd.class);
		    startActivity(intent);
		}
	    });
	recentButton = (ImageButton)findViewById(R.id.button4);
	recentButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperRecent.class);
		    startActivity(intent);
		}
	    });
	locationButton = (ImageButton)findViewById(R.id.button5);
	locationButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperLocation.class);
		    startActivity(intent);
		}
	    });
	userButton = (ImageButton)findViewById(R.id.button6);
	userButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperUser.class);
		    startActivity(intent);
		}
	    });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }
    
    /*
     * Handles the searching from top of menu
     */
    public void do_search(View v){
    	Intent intent = new Intent();
    	intent.setClass(this, CrapperMapperList.class);
    	TextView tv = (TextView)findViewById(R.id.editText1);
    	String text = tv.getText().toString();
    	tv.setText("");
    	intent.putExtra("key", text);
    	startActivity(intent);
    }
}
