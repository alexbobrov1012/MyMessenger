package com.example.mymessenger.news.room;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "news_table")
public class News implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String date;
    private String title;
    private String content;
    private String link;

    public News(String date, String title, String content, String link) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.link = link;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
