package com.example.mymessenger.news.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Query("delete from news_table")
    void deleteAll();

    @Query("select * from news_table")
    LiveData<List<News>> getAllNews();
}
