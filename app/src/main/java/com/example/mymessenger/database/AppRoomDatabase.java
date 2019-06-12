package com.example.mymessenger.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.models.User;

@Database(entities = {User.class, Channel.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ChannelDao channelDao();
}
