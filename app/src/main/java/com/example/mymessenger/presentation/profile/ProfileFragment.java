package com.example.mymessenger.presentation.profile;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.profile.edit.EditProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        imageView.setImageBitmap(viewModel.getUserImage());
        status.setText(viewModel.getThisUser().getStatus());
        toolbar.setTitle(viewModel.getThisUser().getName());
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