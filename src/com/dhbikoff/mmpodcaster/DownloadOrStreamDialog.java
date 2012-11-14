package com.dhbikoff.mmpodcaster;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class DownloadOrStreamDialog {
	private AlertDialog dialog;
	private String url;
	private Context context;

	public DownloadOrStreamDialog(Context context, String url) {
		this.url = url;
		this.context = context;
	}
	
	public void build() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle("MMPodcaster");

		// set dialog message
		alertDialogBuilder
				.setMessage("Would you like to download or stream this podcast?")
				.setCancelable(false)
				.setPositiveButton("STREAM",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(android.content.Intent.ACTION_VIEW);
								i.setDataAndType(Uri.parse(url), "audio/*");
								context.startActivity(i);
							}
						})
				.setNegativeButton("DOWNLOAD", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (Downloader.isDownloadManagerAvailable(context)) {
							Downloader downloader = new Downloader(context,url);
							downloader.download();
						}
					}
				});

		// create alert dialog
		dialog = alertDialogBuilder.create();
	}

	public AlertDialog getDialog() {
		return dialog;

	}
	
	public void show() {
		dialog.show();
	}
}
