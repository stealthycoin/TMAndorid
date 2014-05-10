package com.brilliantsquid.crappermapper;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class CrapperMapperAdd extends Activity
{
    
    EditText name;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

	name = (EditText)findViewById(R.id.editText1);
    }
}
