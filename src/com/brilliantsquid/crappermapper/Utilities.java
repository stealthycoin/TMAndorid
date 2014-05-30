package com.brilliantsquid.crappermapper;

public class Utilities {

	public Utilities() {}
	public static double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
	    float pk = (float) (180/3.14169);

	    float a1 = lat_a / pk;
	    float a2 = lng_a / pk;
	    float b1 = lat_b / pk;
	    float b2 = lng_b / pk;

	    float t1 = (float) (Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2));
	    float t2 = (float) (Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2));
	    float t3 = (float) (Math.sin(a1)*Math.sin(b1));
	    double tt = Math.acos(t1 + t2 + t3);
	   
	    return 6366000*tt*0.000621371;
	}

}
