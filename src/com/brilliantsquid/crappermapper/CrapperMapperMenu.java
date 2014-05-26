package com.brilliantsquid.crappermapper;

import android.os.Bundle;
import android.util.Log;

public class CrapperMapperMenu extends BaseActivity implements GetCallbackInterface
{

    QuerySingleton qs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        
        qs = QuerySingleton.getInstance(this);
        qs.sendGet("signin", this);
    }

	@Override
	public void onDownloadFinished(String result) {
		Log.v("HEY GUYS!", result);
	}
}
