package com.example.mymessenger;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MessagingManager {

    private static final String TAG = "MESSAGINGMANAGER";
    private static FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    // usersInfo = {UserIds[], UserNames[]}
    public static void initChannelForPair(Bundle userInfo) {
        final ArrayList<String> idsList = userInfo.getStringArrayList(Utils.KEY_USERIDS);
        final ArrayList<String> namesList = userInfo.getStringArrayList(Utils.KEY_USERNAMES);
        final String channelId = generateId(idsList.get(0), idsList.get(1));
        // check if channel already exists
        rootRef.collection("channels").document(channelId).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(!documentSnapshot.exists()) {
                            Channel newChannel = new Channel(namesList, idsList);
                            rootRef.collection(Utils.KEY_CHANNELS).document(channelId).set(newChannel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Channel "+ namesList.get(0) + " + " +
                                            namesList.get(1) + "created.");
                                }
                            });
                        }

                    }
                }
        );



    }

    public static String generateId(String userIdA, String userIdB) {
        String result;
        if(userIdA.compareTo(userIdB) > 0) {
            result = userIdA + userIdB;
        } else {
            result = userIdB + userIdA;
        }
        return result;
    }
}
