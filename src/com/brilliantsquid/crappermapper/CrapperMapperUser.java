package com.brilliantsquid.crappermapper;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.CookieManager;

public class CrapperMapperUser extends Activity
{
	private HttpURLConnection connection;
	private CookieManager cManager;
	private HttpCookie cookie; 
	
	private EditText username, password;
	private Button login;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        
        //cookie manager setup
        cManager = new CookieManager();
        CookieHandler.setDefault(cManager);
        
        //create a connection to toilet.brilliantsquid.com
        URL url;
		try {
			url = new URL("toilet.brilliantsquid.com/api/user/login/");
			connection = (HttpURLConnection)url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.pass);
		login = (Button)findViewById(R.id.login);
		
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String submission = "username=" + username.getText().toString() + "&password=" + password.getText().toString();
			}
		});
    }
}
