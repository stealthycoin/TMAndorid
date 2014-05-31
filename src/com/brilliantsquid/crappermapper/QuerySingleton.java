package com.brilliantsquid.crappermapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

interface GetCallbackInterface {
    void onDownloadFinished(String result);
}

interface PostCallbackInterface {
	void onPostFinished(String result);
}

public class QuerySingleton implements GetCallbackInterface {
	private static final String targetSite = "http://toilet.brilliantsquid.com";
	private static QuerySingleton instance = null;
	private static Context context;
	
	private CookieManager cm;
	
	private HttpCookie csrf;
	private String sessionID;
	private HttpURLConnection connection;
	
	private final String TAG = "qs";
	
	//data to make the next post, when its pre-req get finishes up

	private Map<String,String> urlDirectory;
	
	public QuerySingleton() {
		cm = new CookieManager();
		CookieHandler.setDefault(cm);
		
		//This is what page contains the csrf we need given a particular api call. I will check to see if any csrf will do,
		//in which case this is not required, we could always just query the homepage.
		urlDirectory = new HashMap<String,String>();
		urlDirectory.put("api/Toilet/get/", "");
		urlDirectory.put("api/user/login/", "signin/");
		urlDirectory.put("api/toilet/create/", "addrestroom/");
		urlDirectory.put("api/user/create/", "signup/");
	}
	
	public static void setContext(Context ctx) {
		context = ctx;
	}
	
	public void setSessionID(String id) {
		sessionID = id;
	}
	
	public boolean loggedIn() {
		return sessionID != null;
	}
	
	public static synchronized QuerySingleton getInstance() {
		if (instance == null) {
			instance = new QuerySingleton();
		}
		return instance;
	}

	public void parseCSRF(String data) {
		Pattern p = Pattern.compile("name='csrfmiddlewaretoken' value='(.*?)'");
		Matcher m = p.matcher(data);
		if (m.find()) {
			//group 1 should be the token
			csrf = new HttpCookie("csrftoken", m.group(1));
		}
		else {
			//no cookie
		}
	}

	public void sendGet(String s_url, GetCallbackInterface callback) {
		new getTask(callback, null).execute(s_url);
	}
	
	public void sendGet(String s_url, GetCallbackInterface callback, JavaIsATerribleLanguageWrapper jiatlw) {
		new getTask(callback, jiatlw).execute(s_url);
	}
	
	private class JavaIsATerribleLanguageWrapper {
		@SuppressWarnings("unused")
		public String result;
		@SuppressWarnings("unused")
		public Map<String,String> variables;
		@SuppressWarnings("unused")
		public PostCallbackInterface callback;
		@SuppressWarnings("unused")
		public String next_url;
	}

	public void sendPost(String s_url, Map<String,String> variables, PostCallbackInterface callback) {
		Log.v(TAG,"Firing post");
		//encapsulate all these args into a JavaIsATerribleLanguageWrapper
		//and pass them along for the ride
		//that way we can get them in the callback and execute the post
		JavaIsATerribleLanguageWrapper jiatlw = new JavaIsATerribleLanguageWrapper();
		jiatlw.next_url = s_url;
		jiatlw.variables = variables;
		jiatlw.callback = callback;
		
		String pre_get = urlDirectory.get(s_url);
		if (pre_get == null) {
			pre_get = "";
		}
		//call get first to load a csrf token
		sendGet(pre_get, this, jiatlw);
	}
	
	/**
	 * This gets called when a get request that was triggered as a pre-req for a post terminates
	 */
	@Override
	public void onDownloadFinished(String result) {
		//new postTask(variables, callback).execute(url); 	
	}
	
	/**
	 *  AsyncTask for sending a get request.
	 *  
	 */
	private class getTask extends AsyncTask<String,Void,String> {

		final GetCallbackInterface callback;
		final JavaIsATerribleLanguageWrapper jiatlw;
		
		getTask(GetCallbackInterface _callback, JavaIsATerribleLanguageWrapper _jiatlw) {
			callback = _callback;
			jiatlw = _jiatlw;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL(targetSite + "/" + arg0[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(1000);
				if (sessionID != null) {
					connection.setRequestProperty("Cookies", "sessionid=" + sessionID);
				}
				if (csrf != null) {
					connection.addRequestProperty("X-CSRFToken", csrf.getValue());
					connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				}
	    		InputStream in = new BufferedInputStream(connection.getInputStream());
	    		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    		StringBuilder sb = new StringBuilder();
	    		
	    		String line, result = "";
	    		while ((line = br.readLine()) != null) {
	    			sb.append(line);
	    		}
	    		result = sb.toString();
	    		parseCSRF(result);
	    		
	    		return result;
	    	}
	    	catch (IOException e) {
	    		e.printStackTrace();
	    	}
			
			return null;
		}
		
		protected void onPostExecute(String result) {
			if (jiatlw == null) {
				callback.onDownloadFinished(result);
			}
			else {
				//this was a pre-get to a post call. Now we can make the post call
				new postTask(jiatlw.variables, jiatlw.callback).execute(jiatlw.next_url);
			}
		}
	 }
	
	private class postTask extends AsyncTask<String,Void,String> {

		final PostCallbackInterface callback;
		final Map<String, String> variables;
		
		postTask(Map<String, String> _variables, PostCallbackInterface _callback) {
			callback = _callback;
			variables = _variables;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL(targetSite + "/" + arg0[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(1000);
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				if (sessionID != null) {
					Log.v(TAG,sessionID.toString());
					connection.addRequestProperty("Cookies", "sessionid="+sessionID);
				}
				else {
					Log.v(TAG,"Sessionid null");
				}
				if (csrf != null) {
					cm.getCookieStore().add(new URI(targetSite), csrf);
					connection.addRequestProperty("X-CSRFToken", csrf.getValue());
					connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				}
					
				String userpass = "";
				StringBuilder queryset = new StringBuilder();
				//translate map to post request
				boolean first = true;
				for (String key : variables.keySet()) {
					if (!first) {
						queryset.append("&");
					}
					queryset.append(key);
					queryset.append("=");
					queryset.append(variables.get(key));
					first = false;
					//if this is a login it has a uername, and we can grab the username/pass to save for the future
					if (key.equals("username")) {
						userpass = variables.get("username") + "\n" + variables.get("password");
					}
				}
				Log.v("qs", queryset.toString());
				Log.v("qs", connection.getRequestProperties().toString());
				Log.v("qs", connection.toString());
				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				out.write(queryset.toString().getBytes(Charset.forName("UTF-8")));
				out.close();
				
				InputStream in = new BufferedInputStream(connection.getInputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(in));

				String line, result = "";
	    		while ((line = br.readLine()) != null) {
	    			result += line;
	    		}

	    		in.close();
	    		
	    		//if we need to set the session id cookie
	    		if (result.equals("\"Success\"")) {
	    			String[] newCookie = connection.getHeaderField("Set-Cookie").toString().split(";");
	    			if (newCookie[0].split("=")[0].equals("sessionid")) {
	    				Log.v(TAG, "Acquired sessionid = " + newCookie[0].split("=")[1]);
	    				sessionID = newCookie[0].split("=")[1];
	    				FileOutputStream outputStream;
	    				try {
	    				  outputStream = context.openFileOutput("logindata", Context.MODE_PRIVATE);
	    				  Log.v(TAG, "Saving: " + userpass);
	    				  outputStream.write(userpass.getBytes());
	    				  outputStream.close();
	    				} catch (Exception e) {
	    				  e.printStackTrace();
	    				}
	    			}
	    		}
	    		
	    		return result;
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
			catch (FileNotFoundException e) {
				Toast.makeText((Context)callback, "Network connectivitiy issue.", Toast.LENGTH_LONG).show();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			if (callback != null) {
				callback.onPostFinished(result);
			}
		}
	}
}
