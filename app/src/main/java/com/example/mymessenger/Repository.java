package com.example.mymessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mymessenger.presentation.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.app.Activity.RESULT_OK;

public class Repository {

    private static final int RC_CAMERA_PHOTO = 2013;

    private static final int RC_GALLERY_PHOTO = 2014;

    private static final String TAG = "REPO";

    private List<AuthUI.IdpConfig> providers;

    private Intent signInIntent;

    private FirebaseFirestore dataBase;

    public void setUserInstance(User userInstance) {
        this.userInstance = userInstance;
    }

    private User userInstance;

    public Repository() {
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        dataBase = FirebaseFirestore.getInstance();
    }

    public void downloadImageTask(ImageView imageView, String url) {
        new DownloadImageTask(imageView).execute(url);
    }

    public Bitmap getImage(String name) {
        File imageFile = new File(MyApp.appInstance.getExternalImageFolder(),
                "/" +  name.replace("/","."));
        Bitmap image = null;
        try {
            if(imageFile.createNewFile()) {
                // empty file created then download it
                image = downloadImage(name.replace("/","."), imageFile);
            } else {
                // otherwise image already exists external
                Log.d(TAG, "image read external");
                image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void uploadImage(final File fileImage) {
        Executor executor =  Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ImageManager.uploadImage(fileImage);
            }
        });
    }

    private Bitmap downloadImage(final String fromFileName, final File toFileImage) {
        ImageManager.downloadImage(fromFileName, toFileImage);
        return BitmapFactory.decodeFile(toFileImage.getAbsolutePath());
    }

    public Intent getSignInIntent() {
        return signInIntent;
    }

    public void startSignInFlow(Activity activity, int code) {
        activity.startActivityForResult(signInIntent, code);
    }

    public void startCameraActivity(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(takePictureIntent, RC_CAMERA_PHOTO);

    }

    public void startGalleryActivity(Activity activity) {
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, RC_GALLERY_PHOTO);
        }
    }

    public void checkForSignInResult(int resultCode, @Nullable Intent data, Context context) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUserMetadata metadata = user.getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    //Snackbar.make(findViewById(R.id.button),"Greetings new user!!!", Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "add_db");
                    addNewUserToDB(new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl()));
                } else {
                    //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
                }

            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(context, "sign_in_cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(context, "no_internet_connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
    }

    public void updateUserInDB() {
        Log.d(TAG, "updateUser userId = "+ userInstance.getId());
        dataBase.collection("users").document(userInstance.getId()).set(userInstance).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User updated to db");
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }});
    }
    private void addNewUserToDB(final User user) {
        Log.d(TAG, "addNewUserToDB userId = "+ user.getId());
        dataBase.collection("users").document(user.getId()).set(user).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User added to db");
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }});
        if(user.getPic_url() != null) {
            Executor executor =  Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ImageManager.downloadUserPhoto(user.getPic_url());
                }
            });
        }
    }
    public Task<DocumentSnapshot> fetchUser(String userId) {
        /*dataBase.collection("users").document(userId).get()  .addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d(TAG, "CurrentUser set");
                User data = documentSnapshot.toObject(User.class);
                data.setPic_url(data.getPic_url().replace("/","."));
                Log.d(TAG, data.getName());
                //data.setName("dasf");
                userInstance = data;
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error setting user", e);
                        userInstance = new User();
                    }
                });*/
        return dataBase.collection("users").document(userId).get();
    }

    public User getUserInstance() {
        return userInstance;
    }

    public Bitmap getUserImage() {
        File file = new File(MyApp.appInstance.getExternalImageFolder(), userInstance.getPic_url());
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public BitmapDrawable takeProfilePhoto(int requestCode, int resultCode, Intent data, Context context) {
        Bitmap imageBitmap = null;
        boolean isNewPhoto = false;
        if(userInstance.getPic_url().equals("null")) {
            userInstance.setPic_url(UUID.randomUUID().toString());
        }
        File newImageFile = new File(MyApp.appInstance.getExternalImageFolder(), userInstance.getPic_url());
        if (requestCode == RC_CAMERA_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                ImageManager.saveImageExternal(imageBitmap, newImageFile);
                isNewPhoto = true;
                Toast.makeText(context, R.string.edit_profile_image_set_suc, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.edit_profile_image_set_fail, Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "onActivityResultCAMERA");
        } else if (requestCode == RC_GALLERY_PHOTO && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    ImageManager.saveImageExternal(imageBitmap, newImageFile);
                    isNewPhoto = true;
                    Toast.makeText(context, R.string.edit_profile_image_set_suc, Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.edit_profile_image_set_fail, Toast.LENGTH_SHORT).show();
                }
            }

            Log.d(TAG, "onActivityResultGALLERY");
        }
        if(isNewPhoto) {
            ImageManager.uploadImage(newImageFile);
        }
        return new BitmapDrawable(imageBitmap);
    }
}


