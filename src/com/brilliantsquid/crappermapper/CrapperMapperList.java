package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CrapperMapperList extends Activity
{
	String search;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        System.out.println(name);
        
    }   
    
}


