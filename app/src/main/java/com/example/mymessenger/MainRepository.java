package com.example.mymessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymessenger.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;

public class MainRepository {

    private static final int RC_SIGN_IN = 1012;

    private static final String TAG = "REPO_MAIN_DEBUG";

    private List<AuthUI.IdpConfig> providers;

    private Intent signInIntent;

    private FirebaseFirestore dataBase;

    public MainRepository() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        dataBase = FirebaseFirestore.getInstance();
    }

    public void startSignInFlow(Activity activity) {
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void checkForSignInResult(int requestCode, int resultCode, @Nullable Intent data, Context context) {
        if (requestCode != RC_SIGN_IN) {
            return;
        }
        IdpResponse response = IdpResponse.fromResultIntent(data);
        // Successfully signed in
        if (resultCode == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseUserMetadata metadata = user.getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                //Snackbar.make(findViewById(R.id.button),"Greetings new user!!!", Snackbar.LENGTH_LONG).show();
                Log.d(TAG, "add_db");
                addNewUserToDB(new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl()));
            } else {
                //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
            }

        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                Toast.makeText(context, "sign_in_cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(context, "no_internet_connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void addNewUserToDB(final User user) {
        Log.d(TAG, "addNewUserToDB userId = "+ user.getId());
        dataBase.collection("users").document(user.getId()).set(user).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User added to db");
                    }})
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }});
        if(user.getPic_url() != null) {
            Executor executor =  Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ImageManager.downloadUserPhoto(user.getPic_url());
                }
            });
        }
    }




}
