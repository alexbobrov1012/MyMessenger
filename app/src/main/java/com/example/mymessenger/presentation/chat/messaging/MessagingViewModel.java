package com.example.mymessenger.presentation.chat.messaging;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.Message;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MessagingViewModel extends ViewModel {

    private String channelId;

    private Query query;

    private MutableLiveData<List<Message>> allMessagesList = new MutableLiveData<>();

    private StorageReference storageRefFile= FirebaseStorage.getInstance().getReference().child("Files");

    private StorageReference storageRefImages= FirebaseStorage.getInstance().getReference().child("Images");

    private String imageMessage;

    private String fileMessage;

    public MessagingViewModel(String channelId) {
        this.channelId = channelId;
        query = FirebaseFirestore.getInstance()
                .collection("channels")
                .document(channelId)
                .collection("messages")
                .orderBy("date");
    }


    public void fetchMessages() {
        RxFirestore.observeQueryRef(query)
                .flatMapSingle(queryDocumentSnapshots -> {
                    List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                    for(Message token : messages) {
                        getMessageResources(token);
                    }
                    return Single.just(messages);
                })
                .subscribe(messages -> {
                    allMessagesList.postValue(messages);
                });
    }

    private void getMessageResources(Message token) {
        if(token.getContentImage() != null) {
            File imageFile = new File(MyApp.appInstance.getExternalImageFolder(),
                    "/" +  token.getContentImage());
            try {
                if(imageFile.createNewFile()) {
                    // empty file created then download it
                    RxFirebaseStorage.getFile(storageRefImages, imageFile)
                            .subscribeOn(Schedulers.io())
                            .blockingGet();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public MutableLiveData<List<Message>> getAllMessagesList() {
        return allMessagesList;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(String fileMessage) {
        this.fileMessage = fileMessage;
    }

    public void sendMessage(String text) {

        Message message = new Message(MyApp.appInstance.getRepoInstance().getUserInstance().getId(),
                MyApp.appInstance.getRepoInstance().getUserInstance().getName(),
                text,
                null,
                null,
                Calendar.getInstance().getTime());

        FirebaseFirestore.getInstance().collection("channels").document(channelId)
                .collection("messages").add(message);
    }
}
