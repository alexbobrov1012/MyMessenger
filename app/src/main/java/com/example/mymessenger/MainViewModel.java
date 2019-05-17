package com.example.mymessenger;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mymessenger.presentation.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainViewModel extends ViewModel {
    public FirebaseUser getAuthUserInstance() {
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

    public void initSignInFlow(Activity activity, int code) {
        MyApp.appInstance.getRepoInstance().startSignInFlow(activity, code);
    }

    public void checkForSignInResult(int resultCode, @Nullable Intent data, Context context) {
        MyApp.appInstance.getRepoInstance().checkForSignInResult(resultCode, data, context);
    }

    public Task<DocumentSnapshot> fetchUser(String userId) {
        return MyApp.appInstance.getRepoInstance().fetchUser(userId);
    }

    public void setUserInstance(User user) {
        MyApp.appInstance.getRepoInstance().setUserInstance(user);
    }

    public void initCurrentUser(String userId) {
        MyApp.appInstance.getRepoInstance().fetchUser(userId).addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User data = documentSnapshot.toObject(User.class);
                data.setPic_url(data.getPic_url().replace("/","."));
                //data.setName("dasf");
                setUserInstance(data);
            }
        });
    }
}
