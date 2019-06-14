package com.example.mymessenger.presentation.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mymessenger.ImageManager;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class AttachActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_CAMERA_PHOTO = 1001;

    private static final int RC_IMAGE = 1002;

    private static final int RC_FILE = 1003;

    private Uri imageFile;

    private Uri docFile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach);

        Toolbar toolbar = findViewById(R.id.toolbarAttach);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Button attachFileButton = findViewById(R.id.attachFileButton);
        attachFileButton.setOnClickListener(this);
        Button attachImageButton = findViewById(R.id.attachImageButton);
        attachImageButton.setOnClickListener(this);
        Button detachFileButton = findViewById(R.id.detachFileButton);
        detachFileButton.setOnClickListener(this);
        Button detachImageButton = findViewById(R.id.detachImageButton);
        detachImageButton.setOnClickListener(this);
        Button attachPhotoButton = findViewById(R.id.attachPhotoButton);
        attachPhotoButton.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("imageFile", imageFile);
        intent.putExtra("docFile", docFile);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_IMAGE && resultCode == RESULT_OK) {
            imageFile = data.getData();
            Toast.makeText(this, getString(R.string.on_image_attached), Toast.LENGTH_SHORT).show();
           // Log.d("DEBUG","file = " +  imageFile.getAbsolutePath());
        }

        if(requestCode == RC_CAMERA_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String photoName = UUID.randomUUID().toString();
            File imageFile1 = new File(MyApp.appInstance.getExternalImageFolder(),
                    "/" + photoName);
            try {
                imageFile1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, getString(R.string.on_image_attached), Toast.LENGTH_SHORT).show();
            ImageManager.saveImageExternal(imageBitmap, imageFile1);
//            Log.d("DEBUG",imageFile.getName() );
//            Log.d("DEBUG",imageFile.getAbsolutePath() );
            imageFile = Uri.fromFile(imageFile1);
        }

        if(requestCode == RC_FILE && resultCode == RESULT_OK) {
            docFile = data.getData();
            Toast.makeText(this, getString(R.string.on_file_attached), Toast.LENGTH_SHORT).show();

            Log.d("DEBUG","file = " +  docFile.getPath());

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.attachImageButton:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, RC_IMAGE);
                }
                break;
            case R.id.attachFileButton:
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, RC_FILE);
                break;
            case R.id.attachPhotoButton:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RC_CAMERA_PHOTO);
                break;
            case R.id.detachImageButton:
                imageFile = null;
                Toast.makeText(this, getString(R.string.on_image_detached), Toast.LENGTH_SHORT).show();
                break;
            case R.id.detachFileButton:
                docFile = null;
                Toast.makeText(this, getString(R.string.on_file_detached), Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
