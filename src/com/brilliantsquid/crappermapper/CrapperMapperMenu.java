package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Intent;
import android.view.View;

public class CrapperMapperMenu extends Activity
{

    ImageButton emergencyButton, listButton, addButton, recentButton, locationButton, userButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	emergencyButton = (ImageButton)findViewById(R.id.button1);
	emergencyButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperEmergency.class);
		    startActivity(intent);
		}
	    });
	listButton = (ImageButton)findViewById(R.id.button2);
	listButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperList.class);
		    startActivity(intent);
		}
	    });
	addButton = (ImageButton)findViewById(R.id.button3);
	addButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperAdd.class);
		    startActivity(intent);
		}
	    });
	recentButton = (ImageButton)findViewById(R.id.button4);
	recentButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperRecent.class);
		    startActivity(intent);
		}
	    });
	locationButton = (ImageButton)findViewById(R.id.button5);
	locationButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperLocation.class);
		    startActivity(intent);
		}
	    });
	userButton = (ImageButton)findViewById(R.id.button6);
	userButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperUser.class);
		    startActivity(intent);
		}
	    });
    }
}
