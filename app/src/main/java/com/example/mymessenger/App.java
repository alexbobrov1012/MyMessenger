package com.example.mymessenger;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class App extends Application {
    private FirebaseAuth userAuth;

    public FirebaseAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(FirebaseAuth userAuth) {
        this.userAuth = userAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    //private FirebaseUser mFirebaseUser;
}
