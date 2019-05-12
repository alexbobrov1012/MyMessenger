package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfileViewModel extends ViewModel {
    private FirebaseAuth currentUser;

    public ProfileViewModel() {

    }
    // read user info from DB
    public DocumentReference getUserRef() {
        currentUser = FirebaseAuth.getInstance();
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid());
    }

    public void setImageView(ImageView imageView, String url) {
        MyApp.appInstance.getRepoInstance().downloadImageTask(imageView, url);
    }

    public void getImage(String name, ImageView imageView) {
        MyApp.appInstance.getRepoInstance().getImage(name, imageView);
    }

}
