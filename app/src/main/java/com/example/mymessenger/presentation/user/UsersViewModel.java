package com.example.mymessenger.presentation.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.User;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

import javax.annotation.Nullable;

public class UsersViewModel extends ViewModel {

    private MutableLiveData<List<User>> allUsers = new MutableLiveData<>();

    public UsersViewModel() {
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void fetchUsers() {
        MyApp.appInstance.getRepoInstance().fetchUsers()
                .subscribe(users -> {
                    allUsers.postValue(users);
                });
    }

    public void search(String newText) {
        MyApp.appInstance.getRepoInstance().getSearchResult(newText + "%")
                .subscribe(result -> {
                    allUsers.postValue(result);
                });
    }
}
