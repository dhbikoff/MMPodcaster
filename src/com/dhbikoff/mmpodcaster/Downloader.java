package com.dhbikoff.mmpodcaster;

import java.util.List;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

/**
 * DownloadManager wrapper class. Manages the downloading of a file in a
 * background process.
 * 
 **/
public class Downloader {

	private String url;
	private String fileName;
	private Context context;

	/**
	 * Constructor. Takes an Android Context and a URL string for the download
	 * link. Calls a parsing helper function to pull out the filename.
	 * 
	 * @param context
	 *            Android context
	 * 
	 * @param url
	 *            download link
	 * 
	 **/
	public Downloader(Context context, String url) {
		this.context = context;
		this.url = url;
		parseURL(url);
	}

	/**
	 * Gets filename from URL.
	 * 
	 **/
	private void parseURL(String url) {
		int fileNameIndex = 0;

		// find final slash in url to get filename
		for (int i = 0; i < url.length(); i++) {
			int slashIndex = url.indexOf("/", i);
			if (slashIndex != -1) {
				fileNameIndex = slashIndex;
			}
		}

		fileName = url.substring(++fileNameIndex);
	}

	/**
	 * Checks Android API level for DownloadManager usage.
	 * 
	 * TODO: use AsyncTask to download for lower API versions.
	 * 
	 * @param context
	 *            Android Context
	 * 
	 * @return true if we can use DownloadManager
	 * 
	 **/
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Launches download thread.
	 * 
	 **/
	public void download() {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(url));
		request.setDescription("MMPodcast");
		request.setTitle(fileName);
		request.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_PODCASTS, fileName);

		// get download service and enqueue file
		DownloadManager manager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);
	}
}
