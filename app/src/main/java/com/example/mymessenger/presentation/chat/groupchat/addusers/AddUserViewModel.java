package com.example.mymessenger.presentation.chat.groupchat.addusers;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.ActionModeCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AddUserViewModel extends ViewModel {

    public MutableLiveData<List<User>> getFreeUsers() {
        return freeUsers;
    }

    MutableLiveData<List<User>> freeUsers = new MutableLiveData<>();

    public void fetchFreeUsers(String chatId) {

        MyApp.appInstance.getRepoInstance().getUsersFromDatabaseButMe()
                .subscribe(users -> {
                    List<User> list = new ArrayList<>();
                    for(User token : users) {
                        MyApp.appInstance.getRepoInstance().isUserInChat(token.getId(), chatId)
                        .subscribe(aBoolean -> {
                            if(aBoolean) {
                                list.add(token);
                                Log.d("DEBUG", "GOOD");
                            } else {
                                Log.d("DEBUG", "BAD");
                            }
                            freeUsers.postValue(list);
                        });

                    }


                });
    }


    public void addUsersToChannel(List<String> userIds, String chatId) {
        MyApp.appInstance.getRepoInstance().addUsersToChannel(userIds,chatId);
        fetchFreeUsers(chatId);
    }
}
