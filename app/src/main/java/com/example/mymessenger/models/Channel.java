package com.example.mymessenger.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mymessenger.MapConverter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "channels_table")
public class Channel {

    @PrimaryKey
    @NonNull
    private String id;

    private boolean isPrivate;

    @TypeConverters({MapConverter.class})
    private Map<String, List<String>> privateMap;

    private String name;

    private String icon;

    private String date;

    public Channel() {
    }
    // new group channel
    public Channel(String id, String name, String icon) {
        this.isPrivate = false;
        this.id = id;
        privateMap = null;
        this.name = name;
        this.icon = icon;
        this.date = SimpleDateFormat.getDateInstance().format(new Date());
    }
    // new private channel
    public Channel(String id, User firstUser, User secondUser) {
        this.isPrivate = true;
        this.id = id;
        privateMap = new HashMap<>();
        privateMap.put("icon", Arrays.asList(firstUser.getPic_url(), secondUser.getPic_url()));
        privateMap.put("name", Arrays.asList(firstUser.getName(), secondUser.getName()));
        this.name = null;
        this.icon = null;
        this.date = SimpleDateFormat.getDateInstance().format(new Date());
    }
    // for room
    public Channel(String id, boolean isPrivate, Map<String, List<String>> privateMap, String name, String icon, String date) {
        this.id = id;
        this.isPrivate = isPrivate;
        this.privateMap = privateMap;
        this.name = name;
        this.icon = icon;
        this.date = date;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Map<String, List<String>> getPrivateMap() {
        return privateMap;
    }

    public void setPrivateMap(Map<String, List<String>> privateMap) {
        this.privateMap = privateMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", isPrivate=" + isPrivate +
                ", privateMap=" + privateMap +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
