package com.dhbikoff.mmpodcaster;

import java.util.ArrayList;

/**
 * RSS parser. Takes RSS XML string and pulls MP3 links for podcasts.
 * 
 **/
public class RSSUtil {
	private ArrayList<String> downloadLinks;
	private String rssRaw;

	/**
	 * Constructor. Takes XML string and pulls MP3 links.
	 * 
	 * @param xml
	 *            RSS feed
	 * 
	 **/
	public RSSUtil(String xml) {
		rssRaw = xml;
		parse();
	}

	/**
	 * Splits XML string and searches for mp3 download links.
	 * Collects found links in an ArrayList<String>.
	 * 
	 **/
	private void parse() {
		downloadLinks = new ArrayList<String>();
		String[] splitFeed = rssRaw.split(" ");
		for (int i = 0; i < splitFeed.length; i++) {
			// find file hosting site
			if (splitFeed[i].contains("blubrry")) {
				// pull link
				int start = splitFeed[i].indexOf("http");
				int end = splitFeed[i].indexOf(".mp3") + 4;
				if (start < end) {
					String link = splitFeed[i].substring(start, end);
					int badLinkIndex = link.indexOf("http", 1);
					if (badLinkIndex != -1) {
						link = link.substring(badLinkIndex);
					}
					downloadLinks.add(link);
				}
			}
		}
	}

	/**
	 * Getter for MP3 links.
	 * 
	 * @return ArrayList of MP3 links.
	 * 
	 **/
	public ArrayList<String> getDownloadLinks() {
		return downloadLinks;
	}
}
