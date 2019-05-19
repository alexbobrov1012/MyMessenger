package com.example.mymessenger.presentation.user;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymessenger.R;
import com.example.mymessenger.UsersProfileViewModel;

public class UsersProfile extends AppCompatActivity {

    Button sendMessageButton;

    ImageView profilePicImageView;

    private Toolbar toolbar;

    private TextView status;

    private UsersProfileViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        sendMessageButton = findViewById(R.id.users_send_message_button);
        toolbar = findViewById(R.id.users_profile_name);
        status = findViewById(R.id.users_profile_status);
        profilePicImageView = findViewById(R.id.users_profile_pic);
        viewModel = ViewModelProviders.of(this).get(UsersProfileViewModel.class);
        viewModel.getUserData(getIntent().getExtras());
        profilePicImageView.setImageBitmap(viewModel.getProfilePicture(viewModel.getUserPicture()));
        toolbar.setTitle(viewModel.getUserName());
        status.setText(viewModel.getUserStatus());
    }
}