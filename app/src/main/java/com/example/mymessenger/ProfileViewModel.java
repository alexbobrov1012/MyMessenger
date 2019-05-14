package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.mymessenger.presentation.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfileViewModel extends ViewModel {

    private String userName;

    private String userStatus;

    private Bitmap userIcon;

    public boolean dataReady = false;

    public ProfileViewModel() {

    }
    // read user info from DB
    public void initData() {
        User currentUser  = MyApp.appInstance.getRepoInstance().getUserInstance();
        userName = currentUser.getName();
        userStatus = currentUser.getStatus();
        userIcon = MyApp.appInstance.getRepoInstance().getImage(currentUser.getPic_url());
        Log.d("PROFILE", "model");
    }
    public DocumentReference getUserRef() {
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid());
    }

    public void setImageView(ImageView imageView, String url) {
        MyApp.appInstance.getRepoInstance().downloadImageTask(imageView, url);
    }

    public void getImage(String name) {
        MyApp.appInstance.getRepoInstance().getImage(name);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public Bitmap getUserIcon() {
        return userIcon;
    }


}
