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
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Intent ni = new Intent(this, CrapperMapperUser.class);
        //startActivity(ni);
        server_request();
    }

	@Override
	public void onDownloadFinished(String result) {
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
		/*try {
			//obj.put("name__icontains","barn");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//variables.put("filters", obj.toString());
		variables.put("start","0");
		variables.put("end","10");
		variables.put("filters", obj.toString());
		
        //Log.v("filter1", variables.toString());
        
        qs = QuerySingleton.getInstance(this);
        ///THis is important, it acquires a csrf token that we can use to make a request.
        qs.sendGet("", this);
        //qs.sendGet("signin/", this);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        qs.sendPost("api/Toilet/get/", variables, this);
	}
}
