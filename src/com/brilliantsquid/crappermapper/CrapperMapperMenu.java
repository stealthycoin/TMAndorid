package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.Intent;
import android.view.View;

public class CrapperMapperMenu extends Activity
{

    ImageButton addButton;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	addButton = (ImageButton)findViewById(R.id.button3);
	addButton.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    Intent intent = new Intent(CrapperMapperMenu.this,CrapperMapperAdd.class);
		    startActivity(intent);
		}
	    });
    }
}
