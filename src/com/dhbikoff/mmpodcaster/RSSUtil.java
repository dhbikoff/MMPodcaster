package com.dhbikoff.mmpodcaster;

import java.util.ArrayList;

/**
 * RSS parser. Takes RSS XML string and returns download links.
 *  
 */
public class RSSUtil {
    //private String title;
    private ArrayList<String> downloadLinks;
    //private String imageLink;
    private String rssRaw;

    public RSSUtil(String xml) {
        rssRaw = xml;
        parse();
    }

    /**
     * Splits xml string and finds mp3 download links.
     * Returns ArrayList of download links 
     */
    private void parse() {
        downloadLinks = new ArrayList<String>();
        String[] splitFeed = rssRaw.split(" ");
        for (int i = 0; i < splitFeed.length; i++) {
            // Log.d("PODCASTER", splitFeed[i]);
            if (splitFeed[i].contains("blubrry")) {
                int start = splitFeed[i].indexOf("http");
                int end = splitFeed[i].indexOf(".mp3") + 4;
                if (start < end) {
                    String link = splitFeed[i].substring(start, end);
                    int badLinkIndex = link.indexOf("http", 1); 
                    if (badLinkIndex != -1) {
                        link = link.substring(badLinkIndex);
                    }
                    downloadLinks.add(link);
                    //Log.d("POD", link);
                }
            }
        }
    }

    /**
     * Returns ArrayList of download links. 
     */
    public ArrayList<String> getDownloadLinks() {
        return downloadLinks;
    }
}
