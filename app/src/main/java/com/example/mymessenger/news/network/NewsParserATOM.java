package com.example.mymessenger.news.network;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class NewsParserATOM extends NewsParser {

    public NewsParserATOM() {
        newsTag = "entry";
        newsTagTitle = "title";
        newsTagsDate.add("created");
        newsTagsDate.add("updated");
        newsTagsContents.add("summary");
        newsTagsContents.add("content");
        newsTagLink = "link";
    }

    @Override
    public String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals(newsTagLink)) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
                return link;
            }
        }
        parser.nextTag();
        return null;
    }
}
