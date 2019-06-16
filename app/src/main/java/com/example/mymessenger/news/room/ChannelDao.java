package com.example.mymessenger.news.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Channel channel);

    @Query("delete from channel_table")
    void deleteAll();

    @Query("select * from channel_table")
    LiveData<List<Channel>> getAllChannels();

    @Query("delete from channel_table where link = :link")
    void delete(String link);

}
