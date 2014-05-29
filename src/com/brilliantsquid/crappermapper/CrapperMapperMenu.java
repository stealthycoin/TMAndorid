package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.view.View;

public class CrapperMapperMenu extends BaseActivity implements PostCallbackInterface, GetCallbackInterface
{
    static final String KEY_ID = "id";
    static final String KEY_TOILET = "toilet";
    static final String KEY_STARS = "stars";
    static final String KEY_REVIEWS = "reviews";
    static final String KEY_PK = "pk";
 
    ArrayList<HashMap<String, String>> toiletList;
    ListView list;
    LazyAdapter adapter;
	private final String TAG = "MENU";
    QuerySingleton qs;
    
    /** Called when the activity is first created. 
     * @throws JSONException */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

        
        Intent ni = new Intent(this, CrapperMapperUser.class);
        //startActivity(ni);
        server_request();
    }

	@Override
	public void onPostFinished(String result) {
		//Log.v(TAG, "Finished getting toilets:\n" + result);
		
        summon_list(result);
		
	}
	
	
	/**
	 * This summons the toilet list view based on the HTTP Post results
	 * @param result
	 */
	public void summon_list(String result){
		
		JSONObject jObject = null;
		JSONArray jArray = null;
		
		ArrayList<HashMap<String, String>> toiletList = new ArrayList<HashMap<String, String>>();


		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				for(int i = 0; jArray!= null && i < jArray.length(); ++i){
					
					HashMap<String, String> map = new HashMap<String, String>();
					try{
						JSONObject obj = jArray.getJSONObject(i);
						JSONObject fields = obj.getJSONObject("fields");
						//Parse out json data
						int reviews = fields.getInt("numberOfReviews");
						String toilet = fields.getString("name");
						double rating = Double.valueOf(fields.getString("rating"));
						int pk = obj.getInt("pk");
						//String male = obj.getString("male");
						//String female = obj.getString("female");
						
						//Put the objects into the listview's hashmap
						map.put(KEY_ID, String.valueOf(pk));
						map.put(KEY_TOILET, toilet);
						map.put(KEY_STARS, String.valueOf(rating));
						map.put(KEY_REVIEWS, String.valueOf(reviews));
						Log.v(TAG,"REALLY BITCH?: " + map);
						toiletList.add(map);
						
						Log.v(TAG, "START!!  pk:\n" + pk + "  reviews: " + reviews + "  toilet: " + toilet + " rating: " + rating);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				list=(ListView)findViewById(R.id.list);
	
				// Getting adapter by passing xml data ArrayList
		        adapter=new LazyAdapter(this, toiletList);        
		        list.setAdapter(adapter);
		        
	
		        // Click event for single list row
		        list.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						Intent intent = getIntent();
						intent.putExtra("id", id);
						
						Log.v(TAG, "GIVE ME POS: " + position + " GIVE ME PK: " + id);
					}
				});		
	}
	
	public void server_request() {
		Map<String,String> variables = new HashMap<String, String>();
		JSONObject obj=new JSONObject();
		
		
		/*try {
			obj.put("name__icontains","barn");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		//variables.put("filters", obj.toString());
		
        //Log.v("filter1", variables.toString());
        
        qs = QuerySingleton.getInstance();
        //variables.put("username", "toilet");
        //variables.put("password", "jcrowepoops667");
        //qs.sendPost("api/user/login/", variables, this);
        
        //variables.clear();
        variables.put("start","0");
		variables.put("end","10");
		variables.put("filters", obj.toString());
        qs.sendPost("api/Toilet/get/", variables, this);
	}

	@Override
	public void onDownloadFinished(String result) {
		// TODO Auto-generated method stub
		
	}


}
