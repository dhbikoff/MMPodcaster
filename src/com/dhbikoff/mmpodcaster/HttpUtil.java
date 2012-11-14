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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * Utility class for http requests. Fetches and parses RSS feed. Downloads
 * files.
 */
public class HttpUtil extends AsyncTask<String, Void, String> {
	private String url;
	private LinearLayout layout; // main activity layout
	private RSSUtil rss;

	public HttpUtil(String url, LinearLayout layout) {
		this.url = url;
		this.layout = layout;
		this.layout.setOrientation(LinearLayout.VERTICAL);
	}

	/**
	 * Downloads rss xml in background process. Returns the xml in a string.
	 */
	@Override
	protected String doInBackground(String... params) {
		BufferedReader in = null;
		String data = null;

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
			data = sb.toString();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return data;
	}

	// Listener for download link buttons
	View.OnClickListener buttonClickHandler = new View.OnClickListener() {

		public void onClick(View v) {
			Log.d("POD", "CLICKED");
			if (v.isClickable()) {
				Button link = (Button) v;
				String linkURL = (String) link.getText();
				
				DownloadOrStreamDialog dialog = new DownloadOrStreamDialog(v.getContext(), linkURL);
				dialog.build();
				dialog.show();

			}
		}
	};

	/**
	 * Executes after doInBackground is finished. Parses RSS XML. Populates
	 * Scroll Layout with buttons to download podcasts.
	 * 
	 */
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
