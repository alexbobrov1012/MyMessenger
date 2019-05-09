package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfileViewModel extends ViewModel {
    private FirebaseAuth currentUser;

    public ProfileViewModel() {

    }

    public DocumentReference getUserRef() {
        currentUser = FirebaseAuth.getInstance();
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUser.getUid());
    }

}
