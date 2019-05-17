package com.example.mymessenger.presentation.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.mymessenger.presentation.profile.edit.EditProfileActivity;
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

    private static final int RC_EDITED = 2014;

    private ProfileViewModel viewModel;

    private ImageView imageView;

    private Toolbar toolbar;

    private TextView status;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_EDITED) {
            MyApp.appInstance.showLoading(getActivity());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .detach(ProfileFragment.this)
                    .attach(ProfileFragment.this)
                    .commit();
            MyApp.appInstance.hideLoading();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.initData();
        Log.e(TAG, "onCreate");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = getView().findViewById(R.id.profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editActivity = new Intent(getActivity(), EditProfileActivity.class);
                startActivityForResult(editActivity, RC_EDITED);

            }
        });
        toolbar = getView().findViewById(R.id.profile_name);
        imageView = getView().findViewById(R.id.profile_pic);
        status = getView().findViewById(R.id.status_textView);
        Log.e(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated");

    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.initData();
        imageView.setImageBitmap(viewModel.getUserIcon());
        status.setText(viewModel.getUserStatus());
        toolbar.setTitle(viewModel.getUserName());
        Log.e(TAG, viewModel.getUserName());
        Log.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e(TAG, "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }


}