package com.example.mymessenger.presentation.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainViewModel extends ViewModel {

    MutableLiveData<Integer> loading = new MutableLiveData<>();

    public MainViewModel() {
        this.loading.setValue(View.VISIBLE);
    }

    public FirebaseUser getAuthUserInstance() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void fetchCurrentUser() {
        String id = getUserId();
        MyApp.appInstance.getRepoInstance().fetchCurrentUser(id)
                .subscribe(() -> {
                    loading.postValue(View.GONE);
                });
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void initSignInFlow(Activity activity) {
        MyApp.appInstance.getMainRepository().startSignInFlow(activity);
    }

    public void checkForSignInResult(int requestCode, int resultCode, @Nullable Intent data, Context context) {
        MyApp.appInstance.getMainRepository().checkForSignInResult(requestCode, resultCode, data, context);
    }

    public void userSignInCheck(Activity activity) {
        if (getAuthUserInstance() != null) {
            // Snackbar.make(findViewById(R.id.viewPager),"" + viewModel.getUserPic() + " " + viewModel.getUserId(), Snackbar.LENGTH_LONG).show();
            //Log.d(TAG, "Successfully signed in" + viewModel.getUserId());
            //fetchCurrentUser();

            Toast.makeText(activity,"You successfully signed in + "+ getAuthUserInstance().getUid(), Toast.LENGTH_SHORT).show();
        } else {
            initSignInFlow(activity);
            //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
        }
    }
}
