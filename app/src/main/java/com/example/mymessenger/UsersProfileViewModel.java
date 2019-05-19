package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.os.Bundle;

public class UsersProfileViewModel extends ViewModel {

    private String userName;

    private String userStatus;

    private String userPicture;

    public Bitmap getProfilePicture(String pictureName) {
        return MyApp.appInstance.getRepoInstance().getImage(pictureName);
    }

    public void getUserData(Bundle bundle) {
        userName = bundle.getString("name", "NoName");
        userStatus = bundle.getString("status", "EmptyStatus");
        userPicture = bundle.getString("picture", "null");

    }

    public String getUserName() {
        return userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public String getUserPicture() {
        return userPicture;
    }


}
