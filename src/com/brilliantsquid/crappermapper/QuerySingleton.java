package com.brilliantsquid.crappermapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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

public class QuerySingleton {
	private static final String targetSite = "http://toilet.brilliantsquid.com";
	private static QuerySingleton instance = null;
	private static Context context;
	
	private CookieManager cm;
	
	private HttpCookie csrf, sessionID;
	private HttpURLConnection connection;
	
	
	public QuerySingleton(Context ctx) {
		context = ctx;
		cm = new CookieManager();
		CookieHandler.setDefault(cm);
	}
	
	public static boolean hasBeenInit() {
		return context != null;
	}
	
	public static synchronized QuerySingleton getInstance(Context ctx) {
		if (context == null) {
			instance = new QuerySingleton(ctx);
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
		new getTask(callback).execute(s_url);
	}
	
	
	
	public void sendPost(String s_url, Map<String,String> variables, GetCallbackInterface callback) {
		new postTask(variables, callback).execute(s_url);
	}
	
	
	
	/**
	 *  AsyncTask for sending a get request.
	 *  
	 */
	private class getTask extends AsyncTask<String,Void,String> {

		final GetCallbackInterface callback;
		getTask(GetCallbackInterface _callback) {
			callback = _callback;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL(targetSite + "/" + arg0[0]);
				connection = (HttpURLConnection) url.openConnection();
				if (sessionID != null) {
					cm.getCookieStore().add(new URI(targetSite), sessionID);
					connection.addRequestProperty("X-CSRFToken", csrf.getValue());
				}
				connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
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
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			callback.onDownloadFinished(result);
		}
	 }
	
	private class postTask extends AsyncTask<String,Void,String> {

		final GetCallbackInterface callback;
		final Map<String, String> variables;
		
		postTask(Map<String, String> _variables, GetCallbackInterface _callback) {
			callback = _callback;
			variables = _variables;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			try {
				URL url = new URL(targetSite + "/" + arg0[0]);
				connection = (HttpURLConnection) url.openConnection();
				if (sessionID != null) {
					cm.getCookieStore().add(new URI(targetSite), csrf);
					cm.getCookieStore().add(new URI(targetSite), sessionID);
					connection.addRequestProperty("X-CSRFToken", csrf.getValue());
				}

				connection.addRequestProperty("X-Requested-With", "XMLHttpRequest");
				
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
				}
				
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
	    		if (result.equals("\"Success\"") && arg0.equals("http://toilet.brilliantsquid.com/api/user/login/")) {
	    			String[] newCookie = connection.getHeaderField("Set-Cookie").toString().split(";");
	    			sessionID = new HttpCookie(newCookie[0].split("=")[0],newCookie[0].split("=")[1]);	
	    		}
	    		
	    		return result;
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			callback.onDownloadFinished(result);
		}
	}
}
