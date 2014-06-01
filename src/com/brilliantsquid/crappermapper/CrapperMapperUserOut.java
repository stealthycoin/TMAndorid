package com.brilliantsquid.crappermapper;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CrapperMapperUserOut extends BaseActivity implements PostCallbackInterface, SignUpDialog.Messenger, LoginDialog.Messenger
{
	private String username, password;
	private Button btnLogin, btnSignup;
	
	QuerySingleton qs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_out);
        
        qs = QuerySingleton.getInstance();
        
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnSignup = (Button)findViewById(R.id.btnSignup);
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fman = getFragmentManager();
				LoginDialog myLD = new LoginDialog();
				myLD.show(fman, "LoginDialog");
			}
		});
		
		btnSignup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager fman = getFragmentManager();
				SignUpDialog myLD = new SignUpDialog(CrapperMapperUserOut.this);
				myLD.show(fman, "SignUpDialog");
			}
		});
    }
    

	@Override
	public void onPostFinished(String result) {
		if (result == null) {
			Toast.makeText(this, "Something went wrong. Try agian.", Toast.LENGTH_LONG).show();
			
		}
		else if (result.equals("\"Success\"")) {
			Toast.makeText(this, "Welcome " + username, Toast.LENGTH_LONG).show();
			finish();
		}
		else {
			Toast.makeText(this, "Login failed.", Toast.LENGTH_LONG).show();
		}
	}
	

	@Override
	public void onLoginDialogMessage(Map<String, String> vars) {
		username = vars.get("username");
		password = vars.get("password");
		Log.v("DMSG", "Got Dialog Msg for login of user: " + username + ", pword: " + password);
		qs.sendPost("api/user/login/", vars, CrapperMapperUserOut.this);
	}
	
	@Override
	public void onSignUpDialogMessage(Map<String, String> vars) {
		Map<String,String> variables = new HashMap<String,String>();
		username = vars.get("username");
		password = vars.get("password");
		variables.put("username", username);
		variables.put("password", password);
		Log.v("DMSG", "Got Dialog Msg for login of user: " + username + ", pword: " + password);
		qs.sendPost("api/user/create/", vars, CrapperMapperUserOut.this);
		qs.sendPost("api/user/login/", variables, CrapperMapperUserOut.this);
	}
    

}
