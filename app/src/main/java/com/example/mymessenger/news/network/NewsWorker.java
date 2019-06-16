package com.example.mymessenger.news.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.mymessenger.MyApp;


public class NewsWorker extends Worker {
    private static String TAG = "Worker";

    public NewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "Stopped..."+ this.getId().toString());
        super.onStopped();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork..." + this.getId().toString());
        MyApp.appInstance.getNewsRepository().serviceRoutine();
        return Result.success();
    }
}
