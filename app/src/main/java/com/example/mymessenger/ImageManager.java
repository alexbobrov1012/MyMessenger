package com.example.mymessenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageManager {
    private static final String TAG = "IMAGEMANAGER";

    private static StorageReference storageRefImage = FirebaseStorage.getInstance().getReference().child("Images");

    public static void uploadImage(File fileImage) {
        Uri file = Uri.fromFile(fileImage);
        storageRefImage.child(fileImage.getName()).putFile(file);
    }

    public static void downloadImage(String fromFileName, final File toFileImage, final ImageView imageView) {
        Log.e(TAG, "downloadImage storage");
        storageRefImage.child(fromFileName).getFile(toFileImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(toFileImage.getAbsolutePath()));
            }
        });
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
        uploadImage(newFile);
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
