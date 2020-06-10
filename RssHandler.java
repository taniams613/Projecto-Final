package com.example.a2casopratico;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.Date;

public class RssHandler extends DefaultHandler implements LexicalHandler {

    private ContentValues rssItem;

    private boolean in_item = false;
    private boolean in_title = false;
    private boolean in_link = false;
    private boolean in_guid = false;
    private boolean in_comments = false;
    private boolean in_pubDate = false;
    private boolean in_dcCreator = false;
    private boolean in_description = false;
    private boolean in_CDATA;

    private ContentResolver contentProv;
    private Uri uri;

    public RssHandler(ContentResolver resolver, Uri uri) {
        contentProv = resolver;
        this.uri = uri;
    }

    private RssHandler() { }

    public void startElement (String namespaceURI, String localName,
                              String qName, Attributes attributes) throws SAXException {
        if (localName.equalsIgnoreCase("item")) {
            in_item = true;
            rssItem = new ContentValues();
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.TITLE)) {
            in_title = true;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.LINK)) {
            in_link = true;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.GUID)) {
            in_guid = true;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.COMMENTS)) {
            in_comments = true;
        }
        else if (localName.equalsIgnoreCase("pubDate")) {
            in_pubDate = true;
        }
        else if (localName.equalsIgnoreCase("dc:creator")) {
            in_dcCreator = true;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.DESCRIPTION)) {
            in_description = true;
        }
    }

    public void endElement (String namespaceURI, String localName,
                            String qName) throws SAXException {
        if (localName.equalsIgnoreCase("item")) {
            contentProv.insert(uri, rssItem);
            rssItem = new ContentValues();
            in_item = false;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.TITLE)) {
            in_title = false;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.LINK)) {
            in_link = false;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.GUID)) {
            in_guid = false;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.COMMENTS)) {
            in_comments = false;
        }
        else if (localName.equalsIgnoreCase("pubDate")) {
            in_pubDate = false;
        }
        else if (localName.equalsIgnoreCase("dc:creator")) {
            in_dcCreator = false;
        }
        else if (localName.equalsIgnoreCase(FeedsDB.Posts.DESCRIPTION)) {
            in_description = false;
        }
    }

    public void characters(char[] ch, int start, int length) {
        if (in_item) {
            if (in_title) {
                rssItem.put(FeedsDB.Posts.TITLE, new String(ch, start, length));
            }
            else if (in_link) {
                rssItem.put(FeedsDB.Posts.LINK, new String(ch, start, length));
            }
            else if (in_guid) {
                rssItem.put(FeedsDB.Posts.GUID, new String(ch, start, length));
            }
            else if (in_description) {
                rssItem.put(FeedsDB.Posts.DESCRIPTION, new String(ch, start, length));
            }
            else if (in_pubDate) {
                String strDate = new String(ch, start, length);
                try {
                    rssItem.put(FeedsDB.Posts.PUB_DATE, Date.parse(strDate));
                } catch (Exception e) {
                    Log.d("RssHandler", "Erro na análise da data: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void startDTD(String name, String publicId, String systemId) throws SAXException {}

    @Override
    public void endDTD() throws SAXException {}

    @Override
    public void startEntity(String name) throws SAXException {}

    @Override
    public void endEntity(String name) throws SAXException {}

    @Override
    public void startCDATA() throws SAXException {}

    @Override
    public void endCDATA() throws SAXException {}

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {}

}
