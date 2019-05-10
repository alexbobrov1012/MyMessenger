package com.example.mymessenger.presentation;

import android.net.Uri;

public class User {
    private String id;

    private String name;

    private String pic_url;

    private String status;

    public User() {
    }

    public User(String id, String name, Uri pic_url) {
        this.id = id;
        this.name = name;
        try {
            this.pic_url = pic_url.toString();
        } catch (Exception e) {
            this.pic_url = "null";
        }
    }

    public User(String id, String name, Uri pic_url, String status) {
        this.id = id;
        this.name = name;
        try {
            this.pic_url = pic_url.toString();
        } catch (Exception e) {
            this.pic_url = "null";
        }
        this.status = status;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
