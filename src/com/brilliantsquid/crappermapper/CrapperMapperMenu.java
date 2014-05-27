package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CrapperMapperMenu extends BaseActivity implements GetCallbackInterface
{

    QuerySingleton qs;
    boolean ready;
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent ni = new Intent(this, CrapperMapperUser.class);
        //startActivity(ni);
        ready = false;
        server_request();
    }

	@Override
	public void onDownloadFinished(String result) {
		ready = true;
		if (result != null) {
			Log.v("filter1", result);
		}
		else {
			Log.v("filter1", "It was null");
		}
	}
	
	public void server_request() {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		try {
			//obj.put("name__icontains","barn");
			obj.put("start",0);
			obj.put("end",10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//variables.put("filters", obj.toString());
		variables.put("username","toilet");
		variables.put("password","jcrowepoops667");
		
        //Log.v("filter1", variables.toString());
        
        qs = QuerySingleton.getInstance(this);
        //qs.sendGet("signin", this);
        qs.sendGet("signin/", this);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        qs.sendPost("api/user/login/", variables, this);
	}
}
