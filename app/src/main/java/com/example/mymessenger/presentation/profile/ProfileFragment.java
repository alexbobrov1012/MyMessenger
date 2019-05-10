package com.example.mymessenger.presentation.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.ProfileViewModel;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE";
    private ProfileViewModel viewModel;

    private User data;

    private ImageView imageView;

    private Toolbar toolbar;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("SAFER", "Created");
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        toolbar = getView().findViewById(R.id.profile_name);
        imageView = getView().findViewById(R.id.profile_pic);
        viewModel.getUserRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                data = documentSnapshot.toObject(User.class);
                Log.e("SAFER", data.getPic_url() + " " + data.getName());
                toolbar.setTitle(data.getName());
                new DownloadImageTask(imageView).execute(data.getPic_url());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SAFER", "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SAFER", "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("SAFER", "onStop");
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            //Bitmap mIcon11 = null;
            boolean isNotCached = false;
            isExternalStorageWritable();
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
            } catch (Exception e) {
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
                Log.e(TAG, "download - ok");
            } catch (Exception e) {
                Log.e(TAG, "download error " + e.getMessage());
                e.printStackTrace();
            }
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(newFile);
                if (image != null) {
                    image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                }
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }
    }

}
