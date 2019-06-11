package com.example.mymessenger.presentation.profile;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends ViewModel {

    private User thisUser;

    public ProfileViewModel() {
        thisUser = MyApp.appInstance.getRepoInstance().getUserInstance();
    }

    public User getThisUser() {
        return thisUser;
    }

    public Bitmap getUserImage(){
        return MyApp.appInstance.getRepoInstance().getImage(thisUser.getPic_url());
    }


}
