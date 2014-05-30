package com.brilliantsquid.crappermapper;

public class Utilities {

	public Utilities() {}
	public static double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
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

}
