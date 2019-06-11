package com.example.mymessenger.presentation.chat;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.User;
import com.example.mymessenger.models.collections.ChannelId;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ChatsViewModel extends ViewModel {
    private Query query;

    public MutableLiveData<List<Channel>> channelsList = new MutableLiveData<>();

    public ChatsViewModel() {
        query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(getCurrentUser().getId())
                .collection("channelsList");

    }

    public MutableLiveData<List<Channel>> getChannelsList() {
        return channelsList;
    }

    public Query getQuery() {
        return query;
    }

    private User getCurrentUser() {
        return MyApp.appInstance.getRepoInstance().getUserInstance();
    }

    public void getAllUserChannels() {
        List<Channel> channels = new ArrayList<>();
        MyApp.appInstance.getRepoInstance().getAllUserChannel(query)
                .subscribe(documentSnapshots -> {
                    channels.add(documentSnapshots);
                    channelsList.postValue(channels);
                });
    }

}

