package com.example.mymessenger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mymessenger.presentation.LoadingDialog;
import com.example.mymessenger.presentation.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MyApp extends Application {
    private static final String TAG = "MYAPP";

    private Repository repoInstance;

    public static MyApp appInstance;

    private File externalImageFolder;

    private LoadingDialog loadingDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        repoInstance = new Repository();

        externalImageFolder = getPrivateAlbumStorageDir("Images");
        loadingDialog = new LoadingDialog();
    }

    public Repository getRepoInstance() {
        return repoInstance;
    }

    private File getPrivateAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(MyApp.appInstance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    public File getExternalImageFolder() {
        return externalImageFolder;
    }

    public void showLoading(FragmentActivity activity) {
        loadingDialog.show(activity.getSupportFragmentManager(), "loading");
    }

    public void hideLoading() {
        loadingDialog.dismiss();

    }

}
