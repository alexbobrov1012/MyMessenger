package com.example.mymessenger.news.network;

public class NewsParserRSS extends NewsParser{
    public NewsParserRSS() {
        newsTag = "item";
        newsTagTitle = "title";
        newsTagsDate.add("pubDate");
        newsTagsContents.add("description");
        newsTagLink = "link";
    }
}
// https://news.yandex.ru/movies.rss