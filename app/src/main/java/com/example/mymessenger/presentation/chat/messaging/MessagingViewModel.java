package com.example.mymessenger.presentation.chat.messaging;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymessenger.BuildConfig;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.presentation.main.MainActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessagingViewModel extends ViewModel {

    private String channelId;

    private Query query;

    private MutableLiveData<List<Message>> allMessagesList = new MutableLiveData<>();

    private StorageReference storageRefFile= FirebaseStorage.getInstance().getReference().child("Files");

    private StorageReference storageRefImages= FirebaseStorage.getInstance().getReference().child("Images");

    private Uri imageMessage;

    private Uri fileMessage;

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
                .subscribeOn(Schedulers.io())
                .flatMapSingle(queryDocumentSnapshots -> {
                    List<Message> messages = queryDocumentSnapshots.toObjects(Message.class);
                    for(Message token : messages) {
                       // downloadMessageResources(token);
                    }
                    return Single.just(messages);//
                })
                .subscribe(messages -> {
                    allMessagesList.postValue(messages);
                });
    }

    private void downloadMessageResources(Message token) {
        if(token.getContentImage() != null) {
            File imageFile = new File(MyApp.appInstance.getExternalImageFolder(),
                    "/" +  token.getContentImage());
            try {
                if(imageFile.createNewFile()) {
                    // empty file created then download it
                    RxFirebaseStorage.getFile(storageRefImages
                            .child(token.getContentImage()), imageFile)
                            .subscribeOn(Schedulers.io())
                            .toCompletable()
                            .blockingAwait(4, TimeUnit.SECONDS);
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

    public Uri getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(Uri imageMessage) {
        this.imageMessage = imageMessage;
    }

    public Uri getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(Uri fileMessage) {
        this.fileMessage = fileMessage;
    }

    public void sendMessage(String text) {

        String imageName = null;
        String fileName = null;
        String messageId = UUID.randomUUID().toString();
        if(imageMessage != null) {
            imageName = UUID.randomUUID().toString();
        }
        if(fileMessage != null) {
            fileName = UUID.randomUUID().toString();
        }
        uploadAttachedFiles(imageName, fileName);
        if(imageMessage != null) {
            //Uri file = Uri.fromFile(new File(imageMessage.toString()));
            //Uri file = Uri.fromFile(new File(Uri.fromFile(imageMessage).toString()));
            String finalImageName = imageName;
            String finalFileName = fileName;
            RxFirebaseStorage.putFile(storageRefImages.child(imageName), imageMessage)
                    .subscribeOn(Schedulers.io())
                    .doOnSuccess(taskSnapshot -> {
                        Log.d("DEBUG", "attached image uploaded");
                        Message message = new Message(
                                messageId,
                                MyApp.appInstance.getRepoInstance().getUserInstance().getId(),
                                MyApp.appInstance.getRepoInstance().getUserInstance().getName(),
                                text,
                                finalImageName,
                                finalFileName,
                                Calendar.getInstance().getTime());
                        FirebaseFirestore.getInstance().collection("channels").document(channelId)
                                .collection("messages").document(messageId).set(message);
                    })
                    .subscribe();
        }
        else {
            Message message = new Message(
                    messageId,
                    MyApp.appInstance.getRepoInstance().getUserInstance().getId(),
                    MyApp.appInstance.getRepoInstance().getUserInstance().getName(),
                    text,
                    imageName,
                    fileName,
                    Calendar.getInstance().getTime());
            FirebaseFirestore.getInstance().collection("channels").document(channelId)
                    .collection("messages").document(messageId).set(message);
        }
        imageMessage = null;
        fileMessage = null;
    }

    private void uploadAttachedFiles(String imageName,String fileName) {
        if(fileMessage != null) {
            //Uri file = Uri.fromFile(new File(fileMessage.toString()));
            //Uri file = Uri.fromFile(new File(Uri.fromFile(fileMessage).toString()));
            RxFirebaseStorage.putFile(storageRefFile.child(fileName), fileMessage)
                    .subscribeOn(Schedulers.io())
                    .toCompletable()
                    .subscribe();
        }
    }

    public void getAttachedFiles(Intent data) {
        fileMessage = (Uri) data.getExtras().getParcelable("docFile");
        imageMessage = (Uri) data.getExtras().getParcelable("imageFile");
    }

    public void fileDownload(String fileName, Context context, ProgressBar loadingBar) {
        new uploadFileTask(context, loadingBar, fileName).execute();
    }

    public void deleteMessages(List<String> selectedIds) {
        for(String messageId : selectedIds) {
            FirebaseFirestore.getInstance().collection("channels")
                    .document(channelId).collection("messages").document(messageId)
                    .delete();
        }

    }

    public void leaveChat() {
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document(MyApp.appInstance.getRepoInstance().getUserInstance().getId())
                .collection("channelsList").whereEqualTo("id", channelId);

        query.get().addOnCompleteListener(command -> {
            List<DocumentSnapshot> docs = command.getResult().getDocuments();
            for(DocumentSnapshot doc : docs) {
                if(doc.get("id").equals(channelId)) {
                    RxFirestore.deleteDocument(doc.getReference())
                            .doOnComplete(() -> {
                                Log.d("DEBUG", "LEAVED SUC");
                            })
                        .subscribe();

                    break;
                }
            }
        });
    }

    private class uploadFileTask extends AsyncTask<Integer, Void, Integer> {

        private Context context;

        private ProgressBar progressBar;

        private String fileName;

        private File file;

        public uploadFileTask(Context context, ProgressBar progress, String fileName) {
            this.context = context;
            this.progressBar = progress;
            this.fileName = fileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            file = new File(MyApp.appInstance.getExternalFileFolder(),
                    "/" +  fileName);
            try {
                if(file.createNewFile()) {
                    // empty file created then download it
                    storageRefFile.child(fileName).getFile(file)
                            .addOnProgressListener(command -> {
                                double progress = (100.0 * command.getBytesTransferred()) /
                                        command.getTotalByteCount();
                                Log.d("DEBUG" ,"upload progress: " + progress);
                                int currentprogress = (int) progress;
                                progressBar.setProgress(currentprogress);
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            Intent myIntent = new Intent(Intent.ACTION_VIEW);
            myIntent.setData(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file));
            myIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
            context.startActivity(j);
        }
    }
}
