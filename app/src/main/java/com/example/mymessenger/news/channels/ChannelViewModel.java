package com.example.mymessenger.news.channels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.news.room.Channel;

import java.util.List;

public class ChannelViewModel extends ViewModel {
    private LiveData<List<Channel>> allChannels;

    public ChannelViewModel() {
        allChannels = MyApp.appInstance.getNewsRepository().getAllChannels();
    }

    LiveData<List<Channel>> getAllChannels() {
        return allChannels;
    }

    void addChannel(Channel channel) {
        MyApp.appInstance.getNewsRepository().insert(channel);
    }

    void delete(String link) {
        MyApp.appInstance.getNewsRepository().delete(link);
    }

}
