package com.example.mymessenger.presentation.user.userprofile;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MessagingManager;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.Utils;
import com.example.mymessenger.models.User;

import java.util.ArrayList;
import java.util.Arrays;

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

    public void sendPrivateMessage(Activity activity) {
        Bundle userInfo = new Bundle();
        userInfo.putSerializable("user1", getCurrentUser());
        userInfo.putSerializable("user2", profilesUser);
        MessagingManager.initPrivateChannel(userInfo)
                .subscribe(channel -> {
                    Log.d("DEBUG", channel.toString());
                    // TODO:
                    // startactivity
                });
    }
}
