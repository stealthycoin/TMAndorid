package com.brilliantsquid.crappermapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        
        qs = QuerySingleton.getInstance();
        QuerySingleton.setContext(this);
        
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
        //log in the user if possible
		
		try {
			Log.v(TAG, "Logging in from saved data");
			FileInputStream fs = this.openFileInput("logindata");
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = fs.read()) != -1){
			    builder.append((char)ch);
			}
			qs.setSessionID(builder.toString());
		}
		catch (FileNotFoundException e) {
			Log.v(TAG, "Loggin in user as admin");
			variables.put("username", "toilet");
			variables.put("password", "jcrowepoops667");
			qs.sendPost("api/user/login/", variables, this);
		}
		catch (IOException e) {
			//it shouldn't do this unless it fails to read the file after the file exists
			e.printStackTrace();
		}
        
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
