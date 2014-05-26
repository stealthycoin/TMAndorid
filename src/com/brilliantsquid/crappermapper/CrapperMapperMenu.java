package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
        
        server_request();
    }

	@Override
	public void onDownloadFinished(String result) {
		Log.v("filter1", result);
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
		
		variables.put("filters", obj.toString());
		
        //Log.v("filter1", variables.toString());
        
        qs = QuerySingleton.getInstance(this);
        //qs.sendGet("signin", this);
        qs.sendPost("api/Toilet/get", variables, this);
	}
}
