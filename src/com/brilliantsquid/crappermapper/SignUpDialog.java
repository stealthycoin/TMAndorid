package com.brilliantsquid.crappermapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SignUpDialog extends DialogFragment implements View.OnClickListener
{
	private EditText username, password, password1;
	private TextView error;
	private Button submit;
	private CheckBox male, female;
	private Context ctx;
	private PostCallbackInterface callback;
	
	public SignUpDialog (Context _ctx) {
		ctx = _ctx;
		callback = (PostCallbackInterface) _ctx;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		  // Inflate shit...
	      View v = inflater.inflate(R.layout.signup_dialog, null);
	      getDialog().setTitle("Create an Account");
	      Log.v("DIASIGNUP", "Creating Dialog View!!!");
	      username = (EditText) v.findViewById(R.id.make_uname);
	      password = (EditText) v.findViewById(R.id.make_pword);
	      password1 = (EditText) v.findViewById(R.id.rep_pword);
	      error = (TextView) v.findViewById(R.id.pwd_error);
	      male = (CheckBox) v.findViewById(R.id.malebox);
	      female = (CheckBox) v.findViewById(R.id.femalebox);
	      password1.addTextChangedListener(new TextWatcher() {
	    	   public void afterTextChanged(Editable s) {
	    	      String strPass = password.getText().toString();
	    	      String strPass1 = password1.getText().toString();
	    	      if (strPass.equals(strPass1)) {
	    	         error.setText("Passwords OK");
	    	      } else {
	    	         error.setText("Passwords do not match");
	    	      }
	    	   }
	    	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	    	   public void onTextChanged(CharSequence s, int start, int before, int count) {}
	      });
	      
	      submit = (Button) v.findViewById(R.id.diasignup);
	      submit.setOnClickListener(this);

	      setCancelable(false);
	      return v;
	}
	
	Messenger messenger;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		messenger = (Messenger) activity;
	}

	@Override
	public void onClick(View v) {
		Log.v("DIASIGNUP", "View ID = " + v.getId());
		if (v.getId() == R.id.diasignup) {
			if (username.getText().toString().length() < 2) {
				Toast.makeText(ctx, "Username must be 2 characters minimum", Toast.LENGTH_LONG).show();
			} else {
				Map<String,String> variables = new HashMap<String,String>();
				variables.put("username",username.getText().toString());
				variables.put("email", "");
				variables.put("password",password.getText().toString());
				variables.put("male", male.isChecked() ? "1" : "0");
				variables.put("female", female.isChecked() ? "1" : "0");
				messenger.onSignUpDialogMessage(variables);
				dismiss();
			}
		}
	}
	
	interface Messenger 
	{
		public void onSignUpDialogMessage (Map<String, String> vars);
	}
		
}
