package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LazyReviewAdapter extends ArrayAdapter<HashMap<String, String>> {
		
	    
	    private Context context;
	    private int resourceid;
	    private ArrayList<HashMap<String, String>> data;
	    private static LayoutInflater inflater=null;
	    private double updownNum;
	    private QuerySingleton qs;
	    private String pk;
        private TextView reviews;
    	HashMap<String, String> variables;
	    
	    public LazyReviewAdapter(Context ctx, int resid, ArrayList<HashMap<String, String>> d) {
	    	super(ctx, resid, d);
	        context = ctx;
	        resourceid = resid;
	        data = d;
	        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        qs = QuerySingleton.getInstance();
	        if(convertView==null)
	            vi = inflater.inflate(resourceid, null);
	        
	        ArrayList<ImageView> al = new ArrayList<ImageView>();
	        ImageView stars1 = (ImageView)vi.findViewById(R.id.star1_rev); //stars
	        ImageView stars2 = (ImageView)vi.findViewById(R.id.star2_rev);
	        ImageView stars3 = (ImageView)vi.findViewById(R.id.star3_rev);
	        ImageView stars4 = (ImageView)vi.findViewById(R.id.star4_rev);
	        ImageView stars5 = (ImageView)vi.findViewById(R.id.star5_rev);
	        TextView date = (TextView)vi.findViewById(R.id.review_date);
	        reviews = (TextView)vi.findViewById(R.id.review_reviews); // reviews
	        TextView review_text = (TextView)vi.findViewById(R.id.review_text);
	        ImageButton good = (ImageButton)vi.findViewById(R.id.imageButton1);
	        ImageButton bad = (ImageButton)vi.findViewById(R.id.imageButton2);
	        
	        //LOL WTF IS HAPPENING
	        al.add(stars1);al.add(stars2);al.add(stars3);al.add(stars4);al.add(stars5);
	        
	        HashMap<String, String> review = new HashMap<String, String>();
	        review = data.get(position);
	        
	        //Double value of the reviews
	        double rev = Double.valueOf(review.get("rank"));
	        updownNum = Double.valueOf(review.get("up_down_rank"));
	        date.setText(review.get("date"));
	        reviews.setText(review.get("up_down_rank"));
	        review_text.setText(review.get("content"));
	        
	        variables = new HashMap<String,String>();
			variables.put("review_pk", review.get("pk"));
	        
	        
	        //call server url to do stuff with the review_pk and then set up clicklistener


	        good.setOnClickListener(new View.OnClickListener() {             	
	        	public void onClick(View v) {
	        		
	        		if(qs.loggedIn()){
	        			updownNum++;	        			
	        			reviews.setText(String.valueOf(updownNum));
	        			qs.sendPost("api/review/upvote/", variables, new PostCallbackInterface() {
	        				@Override
	        				public void onPostFinished(String result) {
	        					Log.v("Fucker", "Hey man we got a result: " + result);
	        				}

	        				@Override
	        				public void onPostError(String error) {
	        					Toast.makeText(context, "Failed to download reviews...", Toast.LENGTH_LONG).show();
	        				}
	        			});
	        		}else{
	        			Toast.makeText(context, "You must be logged in.", Toast.LENGTH_LONG).show();
	        		}
	        		
	        		
	        	}                
            });
	        
	        bad.setOnClickListener(new View.OnClickListener() {	        	
	        	public void onClick(View v) {
	        		
	        		if(qs.loggedIn()){
	        			updownNum--;	        			
	        			reviews.setText(String.valueOf(updownNum));
	        			qs.sendPost("api/review/downvote/", variables, new PostCallbackInterface() {
	        				@Override
	        				public void onPostFinished(String result) {
	        					Log.v("Fucker", "Hey man we got a result: " + result);
	        				}

	        				@Override
	        				public void onPostError(String error) {
	        					Toast.makeText(context, "Failed to download reviews...", Toast.LENGTH_LONG).show();
	        				}
	        			});
	        		}else{
	        			Toast.makeText(context, "You must be logged in.", Toast.LENGTH_LONG).show();
	        		}
	        		
	        	}
            });


	        
	        Log.v("review", "WTF!");
	        //Get review
	        //String review_text = review.get(CrapperMapperMenu.KEY_TEXT);
	        Utilities.display_stars(al, rev);


	        return vi;
	    }
	    

}
