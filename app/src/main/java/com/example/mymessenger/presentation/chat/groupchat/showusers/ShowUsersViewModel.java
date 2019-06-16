package com.example.mymessenger.presentation.chat.groupchat.showusers;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.User;

import java.util.ArrayList;
import java.util.List;

public class ShowUsersViewModel extends ViewModel {

    public MutableLiveData<List<User>> getChannelUsers() {
        return freeUsers;
    }

    MutableLiveData<List<User>> freeUsers = new MutableLiveData<>();

    public void fetchUsers(String chatId) {

        MyApp.appInstance.getRepoInstance().getUsersFromDatabaseButMe()
                .subscribe(users -> {
                    List<User> list = new ArrayList<>();
                    for(User token : users) {
                        MyApp.appInstance.getRepoInstance().isUserInChat(token.getId(), chatId)
                                .subscribe(aBoolean -> {
                                    if(!aBoolean) {
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
}
