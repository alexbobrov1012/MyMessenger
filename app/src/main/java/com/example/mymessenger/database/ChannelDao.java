package com.example.mymessenger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Channel channel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Channel> channelList);

    @Query("select * from channels_table")
    Single<List<Channel>> getAllChannels();

    @Query("SELECT * FROM channels_table WHERE name LIKE :input")
    Single<List<Channel>> searchChannel(String input);

    @Query("delete from channels_table")
    void deleteAll();
}
