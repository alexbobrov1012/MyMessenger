package com.example.mymessenger;

import android.os.Bundle;
import android.util.Log;

import com.example.mymessenger.models.Channel;
import com.example.mymessenger.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessagingManager {

    private static final String TAG = "MESSAGINGMANAGER";
    private static FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    // usersInfo = {UserIds[], UserNames[]}

    public static Single<Channel> initPrivateChannel(Bundle userInfo) {
        final User user1 = (User) userInfo.getSerializable("user1");
        final User user2 = (User) userInfo.getSerializable("user2");
        final String channelId = generateId(user1.getId(), user2.getId());
        return RxFirestore.getDocument(FirebaseFirestore.getInstance()
                .collection("channels").document(channelId))
                .toSingle()
                .flatMap(documentSnapshot -> {
                    return Single.just(documentSnapshot.toObject(Channel.class));
                })
                .onErrorResumeNext(observer -> {
                    Channel newChannel = new Channel(channelId, user1, user2);
                    return RxFirestore.setDocument(FirebaseFirestore.getInstance()
                    .collection("channels").document(channelId), newChannel)
                            .andThen(Single.just(newChannel))
                            .doOnSuccess(channel -> {
                                Log.d("DEBUG", "UPDATEUSERS");
                                addChannelToUsers(Arrays.asList(user1.getId(), user2.getId()), channelId);
                            });
                });
    }

    public static Single<Channel> initGroupChannel(String channelName,
                                                   String channelIconName,
                                                   List<String> userIds) {
        String channelId = UUID.randomUUID().toString();
        Channel newChannel = new Channel(channelId, channelName, channelIconName);
        return RxFirestore.setDocument(FirebaseFirestore.getInstance()
        .collection("channels").document(channelId), newChannel)
                .andThen(Single.just(newChannel)
                .doOnSuccess(channel -> {
                    Log.d("DEBUG", "GROUP CHANNEL CREATED id = " + channel.getId());
                    addChannelToUsers(userIds, channelId);
                }));
    }
    private static void addChannelToUsers(List<String> usersIds, String channelId) {
        Map<String, String> channelDoc = new HashMap<String, String>() {{
            put("id", channelId);
        }};

        for(String userId : usersIds) {
            RxFirestore.addDocument(FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("channelsList"), channelDoc)
                    .subscribe();
        }
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
