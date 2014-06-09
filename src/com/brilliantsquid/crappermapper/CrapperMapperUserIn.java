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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CrapperMapperUserIn extends BaseActivity
{
	private String username, password;
	private Button btnLogout;
	private TextView headerName;
	
	QuerySingleton qs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);
        
		btnLogout = (Button)findViewById(R.id.btnLogout);
		headerName = (TextView)findViewById(R.id.lgin_name);
		try {
			FileInputStream fs = getBaseContext().openFileInput("logindata");
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = fs.read()) != -1){
			    builder.append((char)ch);
			}
			String[] cookies = builder.toString().split("\n");
			username = cookies[0];
		}
		catch (FileNotFoundException e) {
			Intent intent = new Intent(this, CrapperMapperUserOut.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
            QuerySingleton.getInstance().setSessionID(null);
            getBaseContext().deleteFile("logindata");
            Intent intent = new Intent(getApplicationContext(), CrapperMapperUserOut.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
			}
		});
		
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	headerName.setText(username);
    }

}
