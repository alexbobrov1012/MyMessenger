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
import android.widget.TextView;

import com.example.mymessenger.ImageManager;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE";
    private ProfileViewModel viewModel;

    private User data;

    private ImageView imageView;

    private Toolbar toolbar;

    private TextView status;

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
        status = getView().findViewById(R.id.status_textView);
        viewModel.getUserRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                data = documentSnapshot.toObject(User.class);
                Log.e("SAFER", data.getPic_url() + " " + data.getName());
                toolbar.setTitle(data.getName());
                //viewModel.setImageView(imageView, data.getPic_url());
                viewModel.getImage(data.getPic_url(), imageView);
                status.setText(data.getStatus());
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


}