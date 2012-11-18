package com.dhbikoff.mmpodcaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Utility class to handle asynchronous HTTP functions and update main UI.
 * 
 **/
public class HttpUtil extends AsyncTask<String, Void, String> {
	private String url;
	private LinearLayout layout; // main activity layout
	private RSSUtil rss;

	View.OnClickListener buttonClickHandler = new View.OnClickListener() {

		/**
		 * Responds to download button clicks. Pulls link from button text and
		 * launches an AlertDialog to prompt the user to stream or download file
		 * 
		 * @param v
		 *            clicked button
		 * 
		 **/
		public void onClick(View v) {
			if (v.isClickable()) {
				Button link = (Button) v;
				String linkURL = (String) link.getText();
				DownloadOrStreamDialog dialog = new DownloadOrStreamDialog(
						v.getContext(), linkURL);
				dialog.show();

			}
		}
	};

	/**
	 * Constructor.
	 * 
	 * @param url
	 *            file URL
	 * 
	 * @param layout
	 *            layout object for displaying UI objects
	 * 
	 **/
	public HttpUtil(String url, LinearLayout layout) {
		this.url = url;
		this.layout = layout;
		// allow scrolling
		this.layout.setOrientation(LinearLayout.VERTICAL);
	}

	/**
	 * Opens HTTP connection and fetches page contents in String form.
	 * 
	 * @param params
	 *            URL
	 * 
	 * @return web page String
	 * 
	 **/
	@Override
	protected String doInBackground(String... params) {
		BufferedReader in = null;
		String page = null;

		try {
			HttpClient client = new DefaultHttpClient();
			URI webAddress = new URI(url);
			HttpGet request = new HttpGet();
			request.setURI(webAddress);
			HttpResponse response = client.execute(request);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer();
			String line = "";

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			in.close();
			page = sb.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// make sure the buffer is released
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return page;
	}

	/**
	 * Executes after doInBackground() is finished. Parses RSS XML. Populates
	 * Scroll Layout with buttons to download or stream files.
	 * 
	 * @param result
	 *            RSS feed XML page
	 * 
	 **/
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		rss = new RSSUtil(result);
		ArrayList<String> downloadLinks = rss.getDownloadLinks();
		for (String s : downloadLinks) {
			Button tv = new Button(layout.getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.setLayoutParams(lp);
			tv.setText(s);
			tv.setOnClickListener(buttonClickHandler);
			layout.addView(tv);
		}
	}
}
