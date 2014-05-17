package com.brilliantsquid.crappermapper;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.CookieManager;
import java.nio.charset.Charset;

public class CrapperMapperUser extends Activity
{
	private final String TAG = "USER";
	
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
			url = new URL("http://toilet.brilliantsquid.com/api/user/login/");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
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
				new LoginTask().execute(submission);
			}
		});
    }

    private class LoginTask extends AsyncTask {

		@Override
		protected Object doInBackground(Object... args) {
			String submission = (String)args[0];
			
			OutputStream out;
			try {
				out = new BufferedOutputStream(connection.getOutputStream());
				out.write(submission.getBytes(Charset.forName("UTF-8")));
				
				String response = connection.getResponseMessage();
				Log.v(TAG,response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		protected void onPostExecute(String result) {
			
		}
    	
    }
    
}
