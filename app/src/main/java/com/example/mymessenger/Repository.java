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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.EmptyResultSetException;

import com.example.mymessenger.database.AppRoomDatabase;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.User;
import com.example.mymessenger.models.collections.ChannelId;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class Repository {

    private static final int RC_CAMERA_PHOTO = 2013;

    private static final int RC_GALLERY_PHOTO = 2014;

    private static final String TAG = "REPO";

    private FirebaseFirestore dataBase;

    public void setUserInstance(User userInstance) {
        this.userInstance = userInstance;
    }

    private User userInstance;

    private AppRoomDatabase roomDatabase;

    public Repository() {
        dataBase = FirebaseFirestore.getInstance();
        roomDatabase = MyApp.appInstance.getRoomDatabase();
    }

    public Observable<Channel> getAllUserChannel(Query query) {
        return RxFirestore.observeQueryRef(query)
                .toObservable()
                .flatMap(querySnapshots -> {
                    return Observable.fromIterable(querySnapshots.getDocuments());
                })
                .flatMap(querySnapshot -> {
                    ChannelId channelId = querySnapshot.toObject(ChannelId.class);
                    return RxFirestore.getDocument(FirebaseFirestore.getInstance()
                            .collection("channels").document(channelId.getId()))
                            .toObservable()
                            .flatMapSingle(documentSnapshot -> {
                                Channel tmpChannel = documentSnapshot.toObject(Channel.class);
                                if(tmpChannel.isPrivate()) {
                                    tmpChannel = parseChannel(tmpChannel);
                                }
                                return Single.just(tmpChannel);
                            });
                });
    }

    private Channel parseChannel(Channel tmpChannel) {
        int index = 0;
        String name = tmpChannel.getPrivateMap().get("name").get(0);
        if(name.equals(MyApp.appInstance.getRepoInstance().getUserInstance().getName())) {
            index = 1;
        }
        Channel newChannel = tmpChannel;
        newChannel.setName(newChannel.getPrivateMap().get("name").get(index));
        newChannel.setIcon(newChannel.getPrivateMap().get("icon").get(index));
        return newChannel;
    }

    public Single<List<User>> fetchUsers() {
        return RxFirestore.getCollection(FirebaseFirestore.getInstance().collection("users"))
                .toSingle()
                .flatMap(snapshot -> {
                    List<User> users = snapshot.toObjects(User.class);
                    return Single.just(users);
                })
                .map(users -> {
//                    int idx = 0;
//                    for(int i = 0; i < users.size(); i++) {
//                        if(users.get(i).getId().equals(userInstance.getId())) {
//                            idx = i;
//                            break;
//                        }
//                    }
//                    users.remove(idx);
                    return users;
                })
                .flatMap(users -> putUsersToDatabase(users));
    }

    public Completable fetchCurrentUser(String id) {
        return RxFirestore.getDocument(FirebaseFirestore.getInstance()
                .collection("users").document(id))
                .toSingle()
                .flatMapCompletable(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    Log.d("DEBUG", user.toString());
                    userInstance = user;
                    return Completable.complete();
                });
    }

    public Single<List<User>> getUsersFromDatabase() {
        return roomDatabase.userDao().getAllUsers()
                .subscribeOn(Schedulers.io());

    }

    public void deleteUserFromDatabase() {
        Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
            @Override
            public void run() {
                roomDatabase.userDao().deleteAll();
            }
        });
    }

    public Maybe<String> getUserIcon(String id) {
        return roomDatabase.userDao().getUserIcon(id)
                .subscribeOn(Schedulers.io());
    }

    private Single<List<User>> putUsersToDatabase(List<User> users) {
        return roomDatabase.userDao().insert(users)
                .subscribeOn(Schedulers.io())
                .andThen(Single.just(users));
    }

    public Bitmap getImage(String name) {
        if(name.equals("null")) {
            return BitmapFactory.decodeResource(MyApp.appInstance.getApplicationContext().getResources(), R.drawable.user_icon);
        }
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

    public User getUserInstance() {
        return userInstance;
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


