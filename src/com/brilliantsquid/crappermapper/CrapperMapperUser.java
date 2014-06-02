package com.brilliantsquid.crappermapper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CrapperMapperUser extends BaseActivity implements PostCallbackInterface
{
	
	private EditText username, password;
	private Button btnLogin, btnLogout, btnSignup;
	
	QuerySingleton qs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);
        
        qs = QuerySingleton.getInstance();
        
        if (!qs.loggedIn()) {
        	Intent intent = new Intent(CrapperMapperUser.this, CrapperMapperUserOut.class);
			startActivity(intent);
        } else {
        	Intent intent = new Intent(CrapperMapperUser.this, CrapperMapperUserIn.class);
			startActivity(intent);
        }
        
    }

	@Override
	public void onPostFinished(String result) {
		if (result == null) {
			Toast.makeText(this, "Something went wrong. Try agian.", Toast.LENGTH_LONG).show();
			
		}
		else if (result.equals("\"Success\"")) {
			Toast.makeText(this, "Welcome " + username.getText().toString(), Toast.LENGTH_LONG).show();
			finish();
		}
		else {
			Toast.makeText(this, "Login failed.", Toast.LENGTH_LONG).show();
			password.setText("");
		}
	}
	
	/**
	 * 
	 * Attempts to log the user in. If it cannot, it brings up the User activity.
	 * 
	 * @param ctx Context making the login request
	 */
	public static void login(Context ctx) {
		if (!QuerySingleton.getInstance().loggedIn()) {
			Map<String,String> variables = new HashMap<String, String>();
			try {
				FileInputStream fs = ctx.openFileInput("logindata");
				StringBuilder builder = new StringBuilder();
				int ch;
				while((ch = fs.read()) != -1){
				    builder.append((char)ch);
				}
				String[] cookies = builder.toString().split("\n");
				variables.put("username", cookies[0]);
				variables.put("password", cookies[1]);
				//now we have cookies that lead to a successful login in the past
				QuerySingleton.getInstance().sendPost("api/user/login/", variables, new PostCallbackInterface() {
	
					public void onPostFinished(String result) {
						Log.v("LOGIN", result);
					}

					@Override
					public void onPostError(String error) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			catch (FileNotFoundException e) {
				Intent intent = new Intent(ctx, CrapperMapperUserOut.class);
				ctx.startActivity(intent);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPostError(String error) {
		// TODO Auto-generated method stub
		
	}
}
