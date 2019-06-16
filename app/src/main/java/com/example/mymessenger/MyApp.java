package com.example.mymessenger;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mymessenger.database.AppRoomDatabase;
import com.example.mymessenger.news.NewsRepository;
import com.example.mymessenger.news.room.NewsRoomDatabase;
import com.example.mymessenger.presentation.LoadingDialog;

import java.io.File;

public class MyApp extends Application {
    private static final String TAG = "MYAPP";

    private NewsRoomDatabase dbInstance;

    private NewsRepository newsRepository;

    private Repository repoInstance;

    private MainRepository mainRepository;

    public static MyApp appInstance;

    private File externalImageFolder;

    private File externalFileFolder;

    private LoadingDialog loadingDialog;

    private AppRoomDatabase roomDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        dbInstance = Room.databaseBuilder(this,
                NewsRoomDatabase.class, "news_database")
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dbInstance.newsDao().deleteAll();
                            }
                        });
                        thread.start();
                    }
                })
                .build();
        roomDatabase = Room.databaseBuilder(this,
                AppRoomDatabase.class, "MessengerDB2")
                .build();
        repoInstance = new Repository();
        mainRepository = new MainRepository();
        newsRepository = new NewsRepository();
        externalImageFolder = getPrivateAlbumStorageDir("Images");
        externalFileFolder = getPrivateFileStorageDir("Files");
        loadingDialog = new LoadingDialog();
    }

    public AppRoomDatabase getRoomDatabase() {
        return roomDatabase;
    }

    public Repository getRepoInstance() {
        return repoInstance;
    }

    public NewsRoomDatabase getDbInstance() {
        return dbInstance;
    }

    public NewsRepository getNewsRepository() {
        return newsRepository;
    }

    public MainRepository getMainRepository() {
        return mainRepository;
    }

    private File getPrivateAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(MyApp.appInstance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    private File getPrivateFileStorageDir(String dirName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(MyApp.appInstance.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), dirName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }


    public File getExternalImageFolder() {
        return externalImageFolder;
    }

    public File getExternalFileFolder() {
        return externalFileFolder;
    }

    public void showLoading(FragmentActivity activity) {
        loadingDialog.show(activity.getSupportFragmentManager(), "loading");
    }

    public void hideLoading() {
        loadingDialog.dismiss();

    }

}
