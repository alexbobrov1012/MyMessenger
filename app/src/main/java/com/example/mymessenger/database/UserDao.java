package com.example.mymessenger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymessenger.models.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<User> userList);

    @Query("select * from users_table")
    Single<List<User>> getAllUsers();

    @Query("delete from users_table")
    void deleteAll();
}
