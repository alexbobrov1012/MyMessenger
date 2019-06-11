package com.example.mymessenger.models;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "messages_table")
public class Message {

    @PrimaryKey
    @NonNull
    private String id;

    private String author;

    private String content;

    private String timestamp;

    public Message() {
    }

    public Message(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.timestamp = SimpleDateFormat.getDateInstance().format(new Date());
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
