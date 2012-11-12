package com.dhbikoff.mmpodcaster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Main activity for MMPodcaster. Sets UI layout and fetches RSS feed.
 */
public class MMPodcaster extends Activity {

	// RSS feed URL
	public final String address = "http://feeds.feedburner.com/BillBurr?format=xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mmpodcaster);
		LinearLayout layout = (LinearLayout) findViewById(R.id.scroll_layout);
		HttpUtil rssData = new HttpUtil(address, layout);
		rssData.execute("");

	}
}
