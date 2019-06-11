package com.example.mymessenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManager {
    private static final String TAG = "IMAGEMANAGER";

    private static StorageReference storageRefImage = FirebaseStorage.getInstance().getReference().child("Images");

    public static void uploadImage(File fileImage) {
        Uri file = Uri.fromFile(fileImage);
        storageRefImage.child(fileImage.getName()).putFile(file).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e(TAG, "image uploaded successfully");
                    }
                }
        )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "image upload fail");
                 }
        });
    }

    public static Bitmap downloadImage(String fromFileName, final File toFileImage) {
        Log.e(TAG, "downloadImage storage");
        storageRefImage.child(fromFileName).getFile(toFileImage);
        return BitmapFactory.decodeFile(toFileImage.getAbsolutePath());
    }

    public static void downloadUserPhoto(String url) {
        Bitmap image = null;
        File newFile = new File(MyApp.appInstance.getExternalImageFolder(),
                "/" + url.replace("/", "."));
        //Log.e(TAG, url);
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(in);
            Log.e(TAG, "download image - ok");
        } catch (Exception e) {
            Log.e(TAG, "download image error " + e.getMessage());
            e.printStackTrace();
        }
        saveImageExternal(image, newFile);
        uploadImage(newFile);
    }

    public static void saveImageExternal(Bitmap image, File newFile) {
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
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "External storage available");
            return true;
        }
        return false;
    }
}
