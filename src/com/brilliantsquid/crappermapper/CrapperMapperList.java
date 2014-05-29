package com.brilliantsquid.crappermapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CrapperMapperList extends BaseActivity
{
	String search;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Intent intent = getIntent();
        String search_key = intent.getStringExtra("key");
        System.out.println("OUTPUT TEXT: ");
        System.out.println(search_key);
        
    }   
    
    /*
     * Handles the searching from top of menu
     */
    public void do_search(View v){
    	TextView tv = (TextView)findViewById(R.id.username);
    	String text = tv.getText().toString();
    	tv.setText("");
    	//Query the site and post the results to listing
    }
    
}


