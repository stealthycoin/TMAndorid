package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

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
    
    
    
    
}


