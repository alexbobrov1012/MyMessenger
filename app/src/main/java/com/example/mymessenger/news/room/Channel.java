package com.example.mymessenger.news.room;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "channel_table")
public class Channel implements Serializable {

    private int id;

    private String name;

    @PrimaryKey
    @NonNull
    private String link;

    public Channel(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
