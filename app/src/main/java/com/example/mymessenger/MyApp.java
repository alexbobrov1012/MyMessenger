package com.example.mymessenger;

import android.app.Application;

public class MyApp extends Application {
    private Repository repoInstance;

    public static MyApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        repoInstance = new Repository();
    }

    public Repository getRepoInstance() {
        return repoInstance;
    }
}
