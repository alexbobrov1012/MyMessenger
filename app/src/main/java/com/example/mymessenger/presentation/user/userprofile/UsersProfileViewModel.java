package com.example.mymessenger.presentation.user.userprofile;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MessagingManager;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.Utils;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.chat.messaging.MessagingActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class UsersProfileViewModel extends ViewModel {

    private User profilesUser;

    public Bitmap getProfilePicture() {
        return MyApp.appInstance.getRepoInstance().getImage(getUserPicture());
    }

    public void getUserData(Bundle bundle) {
        profilesUser = (User) bundle.getSerializable("user");
    }

    public String getUserName() {
        return profilesUser.getName();
    }

    public String getUserStatus() {
        return profilesUser.getStatus();
    }

    public String getUserPicture() {
        return profilesUser.getPic_url();
    }

    public String getUserId() {
        return profilesUser.getId();
    }

    public User getCurrentUser() {
        return MyApp.appInstance.getRepoInstance().getUserInstance();
    }

    public String getCurrentUserId() {
        return MyApp.appInstance.getRepoInstance().getUserInstance().getId();
    }

    public void sendPrivateMessage(Context context) {
        Bundle userInfo = new Bundle();
        userInfo.putSerializable("user1", getCurrentUser());
        userInfo.putSerializable("user2", profilesUser);
        MessagingManager.initPrivateChannel(userInfo)
                .subscribe(channel -> {
                    if(channel.isPrivate()) {
                        channel = parseChannel(channel);
                    }
                    Log.d("DEBUG", channel.toString());
                    Intent intent = new Intent(context.getApplicationContext(), MessagingActivity.class);
                    intent.putExtra("channel", channel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                });
    }

    private Channel parseChannel(Channel tmpChannel) {
        int index = 0;
        String name = tmpChannel.getPrivateMap().get("name").get(0);
        if(name.equals(MyApp.appInstance.getRepoInstance().getUserInstance().getName())) {
            index = 1;
        }
        Channel newChannel = tmpChannel;
        newChannel.setName(newChannel.getPrivateMap().get("name").get(index));
        newChannel.setIcon(newChannel.getPrivateMap().get("icon").get(index));
        return newChannel;
    }
}
