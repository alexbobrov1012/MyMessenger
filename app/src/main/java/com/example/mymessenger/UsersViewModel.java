package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

import javax.annotation.Nullable;

public class UsersViewModel extends ViewModel {

    private Query query;

    public UsersViewModel() {
        query = FirebaseFirestore.getInstance()
                .collection("users")
                  .orderBy("name");
    }

    public Query getQuery() {
        return query;
    }


}
