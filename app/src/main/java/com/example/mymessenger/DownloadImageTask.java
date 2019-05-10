package com.example.mymessenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "IMAGE";
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        //Bitmap mIcon11 = null;
        boolean isNotCached = false;
        File file = getPrivateAlbumStorageDir("Images");
        File newFile = new File(file, "/" + urldisplay.replace("/", "."));
        try {
            isNotCached = newFile.createNewFile(); // true  - file not exists yet, then download it
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isNotCached) {
            return downloadAndSaveImage(urldisplay, newFile);
        } else {
            return readImage(newFile);
        }

    }

    private Bitmap readImage(File newFile) {
        byte[] bytes = new byte[(int) newFile.length()];
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(newFile);
            inputStream.read(bytes);
            inputStream.close();
            Log.e(TAG, "read image - ok");
        } catch (Exception e) {
            Log.e(TAG, "read image error " + e.getMessage());
            e.printStackTrace();
        }
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(arrayInputStream);
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "External storage available");
            return true;
        }
        return false;
    }

    public File getPrivateAlbumStorageDir(String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(MyApp.appInstance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    private Bitmap downloadAndSaveImage(String urldisplay, File newFile) {
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
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
        return image;
    }
}