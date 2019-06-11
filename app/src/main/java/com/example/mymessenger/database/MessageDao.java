package com.example.mymessenger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymessenger.models.Message;
import com.example.mymessenger.models.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<Message> messageList);

    @Query("select * from messages_table")
    Single<List<Message>> getAllUsers();

    @Query("delete from messages_table")
    void deleteAll();
}
