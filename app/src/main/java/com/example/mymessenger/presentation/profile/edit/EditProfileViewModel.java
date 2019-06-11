package com.example.mymessenger.presentation.profile.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;

public class EditProfileViewModel extends ViewModel {
    public EditProfileViewModel() {
        super();
    }

    public BitmapDrawable onTakePhotoActivitiesResult(int requestCode, int resultCode, Intent data, Context context) {
        return MyApp.appInstance.getRepoInstance().takeProfilePhoto(requestCode, resultCode, data, context);
    }

    public void startCameraActivity(Activity activity) {
        MyApp.appInstance.getRepoInstance().startCameraActivity(activity);
    }

    public void startGalleryActivity(Activity activity) {
        MyApp.appInstance.getRepoInstance().startGalleryActivity(activity);
    }

    public Bitmap getUserPhoto() {
        return MyApp.appInstance.getRepoInstance().getImage(MyApp.appInstance.getRepoInstance().getUserInstance().getPic_url());
    }

    public String getUserName() {
        return MyApp.appInstance.getRepoInstance().getUserInstance().getName();
    }

    public String getUserStatus() {
        return MyApp.appInstance.getRepoInstance().getUserInstance().getStatus();
    }

    public void setUserStatus(String status) {
        MyApp.appInstance.getRepoInstance().getUserInstance().setStatus(status);
    }

    public void setUserName(String name) {
        MyApp.appInstance.getRepoInstance().getUserInstance().setName(name);
    }

    public void updateUserInDB() {
        MyApp.appInstance.getRepoInstance().updateUserInDB();
    }
}
