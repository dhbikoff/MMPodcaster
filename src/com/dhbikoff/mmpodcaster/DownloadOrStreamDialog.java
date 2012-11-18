package com.dhbikoff.mmpodcaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * Dialog class that prompts the user, allowing them to choose to stream or
 * download a file.
 * 
 **/
public class DownloadOrStreamDialog {
	private AlertDialog dialog;
	private String url;
	private Context context;

	/**
	 * Constructor. Launches helper function to build AlertDialog object.
	 * 
	 * @param context
	 *            Android Context
	 * 
	 * @param url
	 *            file URL
	 * 
	 **/
	public DownloadOrStreamDialog(Context context, String url) {
		this.url = url;
		this.context = context;
		build();
	}

	/**
	 * Constructs a dialog object. Sets the text and button functions of the
	 * dialog pop up.
	 * 
	 **/
	private void build() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		alertDialogBuilder.setTitle("MMPodcaster");

		alertDialogBuilder
				.setMessage(
						"Would you like to download or stream this podcast?")
				.setCancelable(true)
				.setPositiveButton("CANCEL",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								// do nothing
								// empty function to cancel dialog
							}
						})
				.setNegativeButton("STREAM",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Android Intent to open music player activity
								// for streaming
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setDataAndType(Uri.parse(url), "audio/*");
								context.startActivity(i);
							}
						})
				.setNeutralButton("DOWNLOAD",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (Downloader
										.isDownloadManagerAvailable(context)) {
									Downloader downloader = new Downloader(
											context, url);
									// download file to sdcard
									downloader.download();
								}
							}
						});

		// create alert dialog
		dialog = alertDialogBuilder.create();
	}

	/**
	 * Display AlertDialog on screen.
	 * 
	 **/
	public void show() {
		dialog.show();
	}
}
