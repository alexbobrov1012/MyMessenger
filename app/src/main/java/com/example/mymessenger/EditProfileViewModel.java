package com.example.mymessenger;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

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
        return MyApp.appInstance.getRepoInstance().getUserImage();
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
}
