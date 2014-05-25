package com.brilliantsquid.crappermapper;

import android.os.Bundle;

public class CrapperMapperMenu extends BaseActivity
{

    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
    }
    

    
    /*
     * Handles the searching from top of menu
     */
    /*
    public void do_search(View v){
    	Intent intent = new Intent();
    	intent.setClass(this, CrapperMapperList.class);
    	TextView tv = (TextView)findViewById(R.id.username);
    	String text = tv.getText().toString();
    	tv.setText("");
    	intent.putExtra("key", text);
    	startActivity(intent);
    }
    */
}
