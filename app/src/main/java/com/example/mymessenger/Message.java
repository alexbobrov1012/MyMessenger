package com.example.mymessenger;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Message {

    private String author;

    private String content;

    private Timestamp timestamp;

    public Message() {
    }

    public Message(String author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = new Timestamp(new Date());
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
