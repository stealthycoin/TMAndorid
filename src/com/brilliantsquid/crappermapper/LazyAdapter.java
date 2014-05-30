package com.brilliantsquid.crappermapper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
	
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.toiletlist, null);

        TextView title = (TextView)vi.findViewById(R.id.toilet); // title
        TextView stars = (TextView)vi.findViewById(R.id.stars); // artist name
        TextView reviews = (TextView)vi.findViewById(R.id.reviews); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> toilet = new HashMap<String, String>();
        toilet = data.get(position);
        //Double value of the reviews
        double rev = Double.valueOf(toilet.get(CrapperMapperMenu.KEY_REVIEWS));
        
        // Setting all values in listview
        title.setText(toilet.get(CrapperMapperMenu.KEY_TOILET));
        stars.setText(toilet.get(CrapperMapperMenu.KEY_STARS));
        //reviews.setText(toilet.get(CrapperMapperMenu.KEY_REVIEWS));
        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}