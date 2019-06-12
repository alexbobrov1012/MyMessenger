package com.example.mymessenger.presentation.chat.messaging;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MessagingViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private String channelId;

    public MessagingViewModelFactory(String channelId) {
        this.channelId = channelId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessagingViewModel(channelId);
    }
}
