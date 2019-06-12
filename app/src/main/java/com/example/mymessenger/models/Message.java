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

    private String contentText;

    private String contentImage;

    private String contentFile;

    private Date date;

    public Message() {
    }


    public Message(@NonNull String id, String author, String contentText, String contentImage,
                   String contentFile, Date date) {
        this.id = id;
        this.author = author;
        this.contentText = contentText;
        this.contentImage = contentImage;
        this.contentFile = contentFile;
        this.date = date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public String getContentFile() {
        return contentFile;
    }

    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
