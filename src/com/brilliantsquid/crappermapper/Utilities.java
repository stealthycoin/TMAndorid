package com.brilliantsquid.crappermapper;

import java.util.ArrayList;

import android.util.Log;
import android.widget.ImageView;

public class Utilities {
	
	private final static String TAG = "UTIL";

	public Utilities() {}
	public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
	    double pk = 180/Math.PI;

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 =  (Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2));
	    double t2 =  (Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2));
	    double t3 =  (Math.sin(a1)*Math.sin(b1));
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 6366000*tt*0.000621371;
	}
	
	public static void display_stars(ArrayList<ImageView> al, double rev){
        //Display the correct star rating value
        for(int i = 0; i < 5; ++i){
        	if(rev <= 0){
        		al.get(i).setImageResource(R.drawable.star_rating_empty);
        	}else if(rev > 0 && rev < 1){
        		al.get(i).setImageResource(R.drawable.star_rating_half);
        		--rev;
        	}else{
        		al.get(i).setImageResource(R.drawable.star_rating_full);
        		--rev;
        	}
        }
	}

}
