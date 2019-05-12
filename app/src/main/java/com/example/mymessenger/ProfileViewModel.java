package com.example.mymessenger;

import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.mymessenger.presentation.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfileViewModel extends ViewModel {
    private FirebaseAuth currentUser;

    private String userName;

    private String userStatus;

    private Bitmap userIcon;

    public boolean dataReady = false;

    public ProfileViewModel() {
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User data = documentSnapshot.toObject(User.class);
                userName = data.getName();
                userStatus = data.getStatus();
                userIcon = MyApp.appInstance.getRepoInstance().getImage(data.getPic_url());
                dataReady = true;
            }
        });
    }
    // read user info from DB
    public DocumentReference getUserRef() {
        currentUser = FirebaseAuth.getInstance();
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
