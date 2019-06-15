package com.example.mymessenger.presentation.chat.groupchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.ImageManager;
import com.example.mymessenger.MessagingManager;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.chat.messaging.MessagingActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GroupChatCreaterViewModel extends ViewModel {

    private MutableLiveData<List<User>> allUsersList = new MutableLiveData<>();

    public void fetchUsers() {
        //TODO: get users from db
        MyApp.appInstance.getRepoInstance().getUsersFromDatabase()
                .subscribe(users -> {
                    allUsersList.postValue(users);
                });
        //MyApp.appInstance.getRepoInstance().deleteUserFromDatabase();
    }

    public MutableLiveData<List<User>> getAllUsersList() {
        return allUsersList;
    }


    public void submitNewChannel(String channelName, Uri channelIconUri, List<String> userIds, Context context) {
        if(!verifyInput(channelName, channelIconUri, userIds, context)) {
            return;
        }
        userIds.add(MyApp.appInstance.getRepoInstance().getUserInstance().getId());
        try {
            String channelIconName = UUID.randomUUID().toString();
            File newImageFile = new File(MyApp.appInstance.getExternalImageFolder(), channelIconName);
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), channelIconUri);
            ImageManager.saveImageExternal(imageBitmap, newImageFile);
            ImageManager.uploadImage(newImageFile);
            MessagingManager.initGroupChannel(channelName, channelIconName, userIds)
                    .subscribe(channel -> {
                        Log.d("DEBUG", channel.toString());
                        Intent intent = new Intent(context.getApplicationContext(), MessagingActivity.class);
                        intent.putExtra("channel", channel);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private boolean verifyInput(String channelName, Uri channelIconUri, List<String> userIds, Context context) {
        if(channelName == null || channelName.equals("")) {
            Toast.makeText(context, "Enter channel name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(channelIconUri == null){
            Toast.makeText(context, "Select channel icon!", Toast.LENGTH_SHORT).show();
            return false;
        } else if(userIds == null || userIds.size() == 0) {
            Toast.makeText(context, "Select users!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
