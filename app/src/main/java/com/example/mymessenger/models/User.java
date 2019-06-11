package com.example.mymessenger.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mymessenger.ListConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users_table")
public class User implements Serializable {

    @PrimaryKey
    @NonNull
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
            this.pic_url = pic_url.toString().replace("/", ".");
        } catch (Exception e) {
            this.pic_url = "null";
        }
    }

    public User(String id, String name, Uri pic_url, String status, List<String> channelList) {
        this.id = id;
        this.name = name;
        try {
            this.pic_url = pic_url.toString().replace("/", ".");
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
