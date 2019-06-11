package com.example.mymessenger.presentation.user.userprofile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.mymessenger.MessagingManager;
import com.example.mymessenger.R;
import com.example.mymessenger.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class UsersProfile extends AppCompatActivity implements View.OnClickListener{

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

        profilePicImageView.setImageBitmap(viewModel.getProfilePicture());
        toolbar.setTitle(viewModel.getUserName());
        status.setText(viewModel.getUserStatus());
        sendMessageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        viewModel.sendPrivateMessage(this);
    }
}
