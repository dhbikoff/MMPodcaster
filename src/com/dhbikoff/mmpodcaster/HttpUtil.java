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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * Utility class for fetching rss feed via http. Fetches http in background
 * process.
 */
public class HttpUtil extends AsyncTask<String, Void, String> {
	private String url;
	private LinearLayout layout;

	public HttpUtil(String url, LinearLayout layout) {
		this.url = url;
		this.layout = layout;
		this.layout.setOrientation(LinearLayout.VERTICAL);
	}

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

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		RSSUtil rss = new RSSUtil(result);
		ArrayList<String> downloadLinks = rss.getDownloadLinks();
		for (String s : downloadLinks) {
			TextView tv = new TextView(layout.getContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			tv.setLayoutParams(lp);
			tv.setText(s);
			layout.addView(tv);
		}
	}
}