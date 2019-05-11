package com.example.mymessenger;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class MyApp extends Application {
    private static final String TAG = "MYAPP";

    private Repository repoInstance;

    public static MyApp appInstance;

    private StorageReference storageRefImage = FirebaseStorage.getInstance().getReference().child("Images");

    private File externalImageFolder;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        repoInstance = new Repository();
        externalImageFolder = getPrivateAlbumStorageDir("Images");
    }

    public Repository getRepoInstance() {
        return repoInstance;
    }

    public StorageReference getStorageRefImage() {
        return storageRefImage;
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
}
