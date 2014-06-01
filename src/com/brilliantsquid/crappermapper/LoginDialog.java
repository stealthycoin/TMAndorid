package com.brilliantsquid.crappermapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginDialog extends DialogFragment implements View.OnClickListener
{
	private EditText username, password;
	private Button submit, cancel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  // Inflate shit...
	      View v = inflater.inflate(R.layout.login_dialog, null);
	      getDialog().setTitle("Login Sir/MeLady");
	      Log.v("DIALOGIN", "Creating Dialog View!!!");
	      username = (EditText) v.findViewById(R.id.uname);
	      password = (EditText) v.findViewById(R.id.pword);
	      cancel = (Button) v.findViewById(R.id.dialog_cancel);
	      cancel.setOnClickListener(this);
	      submit = (Button) v.findViewById(R.id.dialogin);
	      submit.setOnClickListener(this);
	      qs = QuerySingleton.getInstance();

	      setCancelable(false);
	      return v;
	}
	
	Messenger messenger;
	QuerySingleton qs;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		messenger = (Messenger) activity;
	}

	@Override
	public void onClick(View v) {
		Log.v("DIALOGIN", "View ID = " + v.getId());
		if (v.getId() == R.id.dialogin) {
			Log.v("DIALOGIN", "Somebody pressed SUBMIT!!!");
			Map<String,String> vars = new HashMap<String,String>();
			vars.put("username", username.getText().toString());
			vars.put("password", password.getText().toString());
			messenger.onLoginDialogMessage(vars);
			dismiss();
		} else {
			dismiss();
		}
	}
	
	interface Messenger 
	{
		public void onLoginDialogMessage (Map<String, String> vars);
	}
		
}
