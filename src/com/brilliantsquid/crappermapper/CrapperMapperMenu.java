package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class CrapperMapperMenu extends BaseActivity implements PostCallbackInterface, GetCallbackInterface
{
	
	private final String TAG = "MENU";
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
	public void onPostFinished(String result) {
		Log.v(TAG, "Finished trying to login:\n" + result);
	}
	
	public void server_request() {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		/*try {
			obj.put("name__icontains","barn");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		//variables.put("filters", obj.toString());
		
        //Log.v("filter1", variables.toString());
        
        qs = QuerySingleton.getInstance(this);
        variables.put("username", "toilet");
        variables.put("password", "jcrowepoops667");
        qs.sendPost("api/user/login/", variables, this);
        
        /*variables.clear();
        variables.put("start","0");
		variables.put("end","10");
		variables.put("filters", obj.toString());
        qs.sendPost("api/Toilet/get/", variables, this);*/
	}

	@Override
	public void onDownloadFinished(String result) {
		// TODO Auto-generated method stub
		
	}


}
