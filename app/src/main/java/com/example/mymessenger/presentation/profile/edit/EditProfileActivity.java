package com.example.mymessenger.presentation.profile.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymessenger.EditProfileViewModel;
import com.example.mymessenger.R;

public class EditProfileActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private static final String TAG = "EDITPROFILE";
    private ImageButton takeCameraButton;

    private ImageButton takeFileButton;

    private EditText nameEditText;

    private EditText statusEditText;

    private EditProfileViewModel viewModel;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        takeCameraButton = findViewById(R.id.CameraImageButton);
        takeCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCameraPhotoIntent();
            }
        });
        takeFileButton = findViewById(R.id.FileImageButton);
        takeFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeGalleryPhotoIntent();
            }
        });
        layout = findViewById(R.id.photoLayout);
        layout.setBackground(new BitmapDrawable(viewModel.getUserPhoto()));
        nameEditText = findViewById(R.id.nameEditText);
        nameEditText.setText(viewModel.getUserName());
        nameEditText.setOnEditorActionListener(this);
        statusEditText = findViewById(R.id.statusEditText);
        statusEditText.setText(viewModel.getUserStatus());
        statusEditText.setOnEditorActionListener(this);
        Log.d(TAG, "onCreate");
    }

    private void takeCameraPhotoIntent() {
        viewModel.startCameraActivity(this);
    }

    private void takeGalleryPhotoIntent() {
        viewModel.startGalleryActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BitmapDrawable background = viewModel.onTakePhotoActivitiesResult(requestCode, resultCode, data, this);
        layout.setBackground(background);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            if(v.getId() == R.id.nameEditText) {
                viewModel.setUserName(nameEditText.getText().toString());
            } else if(v.getId() == R.id.statusEditText) {
                viewModel.setUserStatus(statusEditText.getText().toString());
            }
            Log.d(TAG, "Editing");
        }
        return false;
    }
}
