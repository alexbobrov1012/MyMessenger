package com.example.mymessenger.news.news.share;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.Channel;

import java.util.List;

public class ShareViewModel extends ViewModel {

    MutableLiveData<List<Channel>> allChannels = new MutableLiveData<>();

    public void fetchChannels() {
        MyApp.appInstance.getRepoInstance().getChannelsFromDatabase()
                .subscribe(channels -> {
                    allChannels.postValue(channels);
                });
    }

    public MutableLiveData<List<Channel>> getAllChannels() {
        return allChannels;
    }
}
