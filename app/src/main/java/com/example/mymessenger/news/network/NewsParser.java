package com.example.mymessenger.news.network;

import android.util.Log;
import android.util.Xml;

import androidx.core.text.HtmlCompat;

import com.example.mymessenger.news.Utils;
import com.example.mymessenger.news.room.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class NewsParser {
    static final String LOG_TAG = "Parser";

    String newsTag;

    String newsTagTitle;

    List<String> newsTagsDate;

    List<String> newsTagsContents;

    String newsTagLink;

    NewsParser() {
        newsTagsContents = new ArrayList<>();
        newsTagsDate = new ArrayList<>();
    }

    public final List<News> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d(LOG_TAG, "in parse");
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List newsItems = new ArrayList();
        Log.d(LOG_TAG, "in readFeed");
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().
                    equals(newsTag)) {
                newsItems.add(readNews(parser));
            }
        }
        return newsItems;
    }

    private News readNews(XmlPullParser parser) throws IOException, XmlPullParserException {
        String title = null;
        String date = null;
        String contents = null;
        String link = null;
        Log.d(LOG_TAG, "in readNews");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (compareTags(name, newsTagTitle)) {
                title = readTitle(parser);
            } else if(compareTags(name, newsTagsDate)) {
                date = readDate(parser);
            } else if(compareTags(name, newsTagsContents)) {
                contents = readContents(parser);
            } else if(compareTags(name, newsTagLink)) {
                String nul = readLink(parser);
                if (nul != null) {
                    link = nul;
                }
            }
            else {
                skip(parser);
            }
        }
        if (title != null) {
            title = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
        }
        if (contents != null) {
            contents = HtmlCompat.fromHtml(contents, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
        }
        date = Utils.parseDate(date);
        return new News(date,title,contents,link);
    }

    private boolean compareTags(String name, String tag) {
        return name.equals(tag);
    }

    private boolean compareTags(String name, List<String> tags) {
        for (String tok : tags) {
            if (name.equals(tok)) {
                return true;
            }
        }
        return false;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.d(LOG_TAG, "in skip");
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            Log.d(LOG_TAG, "in skip.." + parser.getName());
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        Log.d(LOG_TAG, "in readText");
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
        }
        parser.nextTag();
        return result;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    private String readContents(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

    protected String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readText(parser);
    }

}
