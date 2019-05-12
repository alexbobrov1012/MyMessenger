package com.example.mymessenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

// upload user's picture to storage
/*public class UploadImageTask extends AsyncTask<File,Void, Void> {
    private static final String TAG = "UPLOAD_IMAGE";
    // TODO: split into tasks with local File and with URL params
    @Override
    protected Void doInBackground(File... files) {
        File imageFile = files[0];
        uploadImageToStorage(imageFile);
    }

    // get local picture and upload to storage
    private void uploadImageToStorage(File imageFile) {
        Uri file = Uri.fromFile(imageFile);
        MyApp.appInstance.getStorageRefImage().child(imageFile.getName()).putFile(file);
    }

    // get user's picture from url then upload to storage and save local
    private void downloadAndSaveImage(String url, File newFile) {
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            MyApp.appInstance.getStorageRefImage().child(url).putStream(in);
            image = BitmapFactory.decodeStream(in);
            Log.e(TAG, "download image - ok");
        } catch (Exception e) {
            Log.e(TAG, "download image error " + e.getMessage());
            e.printStackTrace();
        }
        if(isExternalStorageWritable()) {
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(newFile);
                if (image != null) {
                    image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                }
                outputStream.flush();
                outputStream.close();
                Log.e(TAG, "save image - ok");
            } catch (Exception e) {
                Log.e(TAG, "save image error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "External storage available");
            return true;
        }
        return false;
    }
}
*/