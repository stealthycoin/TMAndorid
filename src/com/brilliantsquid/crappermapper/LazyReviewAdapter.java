package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyReviewAdapter extends BaseAdapter {
		
	    
	    private Activity activity;
	    private ArrayList<HashMap<String, String>> data;
	    private static LayoutInflater inflater=null;
	    
	    public LazyReviewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
	        activity = a;
	        data=d;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }

	    public int getCount() {
	        return data.size();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return Integer.valueOf(data.get(position).get("id"));
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View vi=convertView;
	        if(convertView==null)
	            vi = inflater.inflate(R.layout.review, null);
	        
	        ArrayList<ImageView> al = new ArrayList<ImageView>();
	        ImageView stars1 = (ImageView)vi.findViewById(R.id.star1_rev); //stars
	        ImageView stars2 = (ImageView)vi.findViewById(R.id.star2_rev);
	        ImageView stars3 = (ImageView)vi.findViewById(R.id.star3_rev);
	        ImageView stars4 = (ImageView)vi.findViewById(R.id.star4_rev);
	        ImageView stars5 = (ImageView)vi.findViewById(R.id.star5_rev);
	        TextView date = (TextView)vi.findViewById(R.id.review_date);
	        TextView reviews = (TextView)vi.findViewById(R.id.review_reviews); // reviews
	        TextView review_text = (TextView)vi.findViewById(R.id.review_text);
	        
	        
	        //LOL WTF IS HAPPENING
	        al.add(stars1);al.add(stars2);al.add(stars3);al.add(stars4);al.add(stars5);
	        
	        HashMap<String, String> review = new HashMap<String, String>();
	        review = data.get(position);
	        
	        //Double value of the reviews
	        double rev = Double.valueOf(review.get("rank"));
	        date.setText(review.get("date"));
	        reviews.setText(review.get("up_down_rank"));
	        review_text.setText(review.get("content"));
	        
	        //Get review
	        //String review_text = review.get(CrapperMapperMenu.KEY_TEXT);
	        Utilities.display_stars(al, rev);


	        return vi;
	    }
	    

}
