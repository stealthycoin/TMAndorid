package com.brilliantsquid.crappermapper;

import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.os.Build;

public class CrapperMapperSignup extends BaseActivity implements PostCallbackInterface {

	private final String TAG = "SIGNUP"; 
	
	private Button signup;
	private TextView username, pass, pass2;
	private CheckBox male, female;
	
	private QuerySingleton qs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		qs = QuerySingleton.getInstance();
		

		username = (TextView)findViewById(R.id.username);
		pass = (TextView)findViewById(R.id.pass);
		pass2 = (TextView)findViewById(R.id.pass2);
		male = (CheckBox)findViewById(R.id.male);
		female = (CheckBox)findViewById(R.id.female);

		
	}

	public void sendSignupQuery(View v) {
		Log.v(TAG,"Sending query");
		if (pass.getText().toString().equals(pass2.getText().toString())) {
			if (username.getText().toString().length() < 5) {
				Toast.makeText(this, "Username must be 5 characters minimum", Toast.LENGTH_LONG);
			}
			else {
				Map<String,String> variables = new HashMap<String,String>();
				variables.put("username",username.getText().toString());
				variables.put("email", "");
				variables.put("password",pass.getText().toString());
				variables.put("male", male.isChecked() ? "1" : "0");
				variables.put("female", female.isChecked() ? "1" : "0");
				qs.sendPost("api/user/create/", variables, this);
			}
		}
		else {
			Toast.makeText(this, "Passwords must match", Toast.LENGTH_LONG).show();
			pass.setText("");
			pass2.setText("");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crapper_mapper_signup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.signup, container, false);
			return rootView;
		}
	}

	@Override
	public void onPostFinished(String result) {
		if (result.equals("\"A user with that name already exists.\"")) {
			Toast.makeText(this, "Username taken.", Toast.LENGTH_LONG).show();
			username.setText("");
		}
		else {
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
		}
		
	}

}
