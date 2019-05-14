package com.example.mymessenger;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends ViewModel {
    public FirebaseUser getUserInstance() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserPic() {
        return FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void initSignInFlow(Activity activity) {
        MyApp.appInstance.getRepoInstance().startSignInFlow(activity);
    }

    public void checkForSignInResult(int requestCode, int resultCode, @Nullable Intent data, Context context) {
        MyApp.appInstance.getRepoInstance().checkForSignInResult(requestCode, resultCode, data, context);
    }

    public void setCurrentUser(String userId) {
        MyApp.appInstance.getRepoInstance().setUserInstance(userId);
    }

}
