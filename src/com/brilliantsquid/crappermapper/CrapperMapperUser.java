package com.brilliantsquid.crappermapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

public class CrapperMapperUser extends BaseActivity
{
	private final String TAG = "USER";
	
	private HttpURLConnection connection;
	private CookieManager cManager;
	private HttpCookie cookie; 
	private String csrf;
	
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
        
        createURLConnection();
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
		//do not allow login until CSRF token has been aquired
		login.setClickable(false);
		
		
		//we need to get the CSRF token so we can login
		new GetCSRFTask().execute();
    }

    
    private void createURLConnection() {
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
    }
    
    private class GetCSRFTask extends AsyncTask<String,String,String> {

		@Override
		protected String doInBackground(String... args) {
			HttpURLConnection urlConnection;
			try {
				URL url = new URL("http://toilet.brilliantsquid.com/signin/");
				urlConnection = (HttpURLConnection) url.openConnection();
	    		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	    		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    		
	    		String line, result = "";
	    		while ((line = br.readLine()) != null) {
	    			result += line;
	    		}
	    		
	    		return result;
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
			return null;
		}
		
		protected void onPostExecute(String result) {
			/* 
			 * Parse result
			 * Should look something like this:
			 * name='csrfmiddlewaretoken' value='ovCPHoz09hPuQkEKMxgbpBpR4uZqD9wm'
			 * 
			 */
			Pattern p = Pattern.compile("name='csrfmiddlewaretoken' value='(.*?)'");
			Matcher m = p.matcher(result);
			if (m.find()) {
				//group 1 should be the token
				csrf = m.group(1);
				cookie = new HttpCookie("csrftoken", m.group(1));
				cookie.setPath("/");
				cookie.setDomain("toilet.brilliantsquid.com");
				//found and set token, enable button
				Log.v(TAG, "csrf: " + m.group(1));
				login.setClickable(true);
			}
			else {
				Log.v(TAG, "Failed to get data");
				//report error
			}
		}
    }
    
    private class LoginTask extends AsyncTask<String,String,String> {

		@Override
		protected String doInBackground(String... args) {
			String submission = (String)args[0];
			
			OutputStream out;
			try {
				cManager.getCookieStore().add(new URI("toilet.brilliantsquid.com"), cookie);
				//Log.v(TAG,cManager.getCookieStore().get(new URI("toilet.brilliantsquid.com")).toString());
				connection.addRequestProperty("X-CSRFToken", csrf);
				connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				out = new BufferedOutputStream(connection.getOutputStream());
				out.write(submission.getBytes(Charset.forName("UTF-8")));
				out.close();
				
				InputStream in = new BufferedInputStream(connection.getInputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				String line, result = "";
	    		while ((line = br.readLine()) != null) {
	    			result += line;
	    		}
	    		in.close();
				
	    		//Log.v(TAG, connection.getHeaderFields().toString());
	    		
	    		//if we didnt fail to login set the sessionID cookie (hopefully, documentation sucks for this)
	    		if (result.equals("\"Success\"")) {
	    			String[] newCookie = connection.getHeaderField("Set-Cookie").toString().split(";");
	    			HttpCookie c = new HttpCookie(newCookie[0].split("=")[0],newCookie[0].split("=")[1]);
	    			for (int i = 1 ; i < newCookie.length ; i++) {
	    				if (newCookie[i].trim().equals("httponly")) {
	    					//c.setHttpOnly(true); android doesnt have this I guess...?
	    					//its in the docs but is red in eclipse
	    				}
	    				else {
	    					String[] field = newCookie[i].split("=");
	    					if (field[0].trim().equals("Max-Age")) {
	    						c.setMaxAge(Long.valueOf(field[1]));
	    					}
	    					else if (field[0].trim().equals("Max-Age")) {
	    						c.setPath(field[1]);
	    					}
	    				}
	    			}
	    			cManager.getCookieStore().add(new URI("toilet.brilliantsquid.com"), c);
	    		}
	    		
				return  result;//might have to return the sessionID in case cookies fail like I assume they will
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		protected void onPostExecute(String result) {
			Log.v(TAG, result);
			createURLConnection();
		}
    }
    

}
