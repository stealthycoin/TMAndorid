package com.brilliantsquid.crappermapper;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

public class EndLessScroll implements OnScrollListener {

	private int visibleThreshold = 5;
	private int currentPage = 0;
	private int previousTotal = 0;
	private boolean loading = true;
	private Context context;

	public EndLessScroll(Context context) {
		this.context = context;
	}
	public EndLessScroll(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// I load the next page of gigs using a background task,
			// but you can call any function here.
			Toast.makeText(context, "Booger", Toast.LENGTH_LONG).show();
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}