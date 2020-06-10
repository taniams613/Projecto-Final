package com.example.a2casopratico;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.net.Uri;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RssDownloadHelper {
    public static void updateRssData(String rssUrl, ContentResolver resolver, Uri uri, SharedPreferences prefs) {
        try {
            URL url = new URL(rssUrl);
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

            RssHandler handler = new RssHandler(resolver, uri);
            parser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);

            XMLReader xr = parser.getXMLReader();
            xr.setContentHandler(handler);

            InputSource is = new InputSource(url.openStream());

            is.setEncoding(prefs.getString(uri.toString(), "UTF-8"));
            xr.parse(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
