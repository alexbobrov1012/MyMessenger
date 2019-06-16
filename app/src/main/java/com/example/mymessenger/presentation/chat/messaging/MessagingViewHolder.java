package com.example.mymessenger.presentation.chat.messaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.OnMessageClick;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MessagingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
View.OnLongClickListener{

    TextView textTextView;

    TextView dateTextView;

    TextView fileNameTextView;

    TextView authorNameTextView;

    ImageView imageImageView;

    ImageView fileImageView;

    ImageView authorImageView;

    LinearLayout fileLayout;

    LinearLayout itemLayout;

    LinearLayout authorlayout;

    OnMessageClick listener;

    SelectItemsListener selectItemsListener;

    String imageName;

    View selectedOverlay;

    boolean isPrivateChannel;

    private StorageReference storageRefImages= FirebaseStorage.getInstance().getReference().child("Images");

    public MessagingViewHolder(@NonNull View itemView,
                               OnMessageClick listener,
                               SelectItemsListener selectItemsListener,
                               boolean isPrivateChannel) {
        super(itemView);
        this.listener = listener;
        this.authorlayout = itemView.findViewById(R.id.authorMessageLayout);
        this.isPrivateChannel = isPrivateChannel;
        this.selectedOverlay = itemView.findViewById(R.id.selectionOverlayView);
        this.selectItemsListener = selectItemsListener;
        imageName = "";
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.textTextView = itemView.findViewById(R.id.text_message);
        this.authorNameTextView = itemView.findViewById(R.id.authorNameMessage);
        this.dateTextView = itemView.findViewById(R.id.date_message);
        this.fileNameTextView = itemView.findViewById(R.id.fileName_message);
        this.imageImageView = itemView.findViewById(R.id.imageView_message);
        this.fileImageView = itemView.findViewById(R.id.fileImageView);
        this.authorImageView = itemView.findViewById(R.id.authorImageMessage);
        this.fileLayout = itemView.findViewById(R.id.layoutFileMessage);
        fileLayout.setOnClickListener(this);
        this.itemLayout = itemView.findViewById(R.id.messageLayoutText);
    }

    public void bind(Message model) {
        if(!isPrivateChannel) {
            authorlayout.setVisibility(View.VISIBLE);
            authorNameTextView.setText(model.getAuthor());
            new uploadUserIconTask(model.getAuthorId(), authorImageView).execute();

        } else {
            authorlayout.setVisibility(View.GONE);
        }
        if(model.getContentText() == null) {
            textTextView.setVisibility(View.GONE);
        } else {
            textTextView.setText(model.getContentText());
        }

        if(model.getContentImage() == null) {
            imageImageView.setVisibility(View.GONE);
        } else {
                imageName = model.getContentImage();
                imageImageView.setVisibility(View.VISIBLE);
//                imageImageView.setImageBitmap(MyApp.appInstance.getRepoInstance()
//                        .getImage(model.getContentImage()));
                new uploadImageTask().execute();
        }

        if(model.getContentFile() == null) {
            fileLayout.setVisibility(View.GONE);
        } else {
            fileLayout.setVisibility(View.VISIBLE);
            fileNameTextView.setText(model.getContentFile());
        }

        Log.d("DEBUG", model.getId() +" " + model.getAuthor() + " " + model.getContentText());
        if(!model.getAuthorId().equals(MyApp.appInstance.getRepoInstance().getUserInstance().getId())) {
            itemLayout.setBackgroundResource(R.drawable.shape_message_text_item_other);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            itemLayout.setLayoutParams(params);

        } else {
            itemLayout.setBackgroundResource(R.drawable.shape_message_text_item);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            itemLayout.setLayoutParams(params);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        dateTextView.setText(dateFormat.format(model.getDate()));
    }

    @Override
    public boolean onLongClick(View v) {
        if (selectItemsListener != null) {
            return selectItemsListener.onItemLongClicked(getAdapterPosition());
        }
        return false;
    }

    private class uploadImageTask extends AsyncTask<Integer, Void, Integer> {

        private Drawable drawable;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // imageImageView.setImageResource(R.drawable.loading);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            File imageFile = new File(MyApp.appInstance.getExternalImageFolder(),
                    "/" +  imageName);
            try {
                if(imageFile.createNewFile()) {
                    // empty file created then download it
                    RxFirebaseStorage.getFile(storageRefImages
                            .child(imageName), imageFile)
                            .subscribeOn(Schedulers.io())
                            .toCompletable()
                            .doOnComplete(() -> {
                                drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
                            })
                            .blockingAwait();
                } else {
                    drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            imageImageView.setImageDrawable(drawable);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.layoutFileMessage) {
            Log.d("DEBUG", "FILE DOWNLOADING");
            listener.onFileLoadingClick(getAdapterPosition());
        }
        listener.onItemListClick(getAdapterPosition());
        if (selectItemsListener != null) {
            selectItemsListener.onItemClicked(getAdapterPosition());
        }
    }

    private class uploadUserIconTask extends AsyncTask<Integer, Void, Integer> {

        private Drawable drawable;

        private String userId;

        private ImageView imageView;

        public uploadUserIconTask(String userId, ImageView imageView) {
            this.userId = userId;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // imageImageView.setImageResource(R.drawable.loading);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            MyApp.appInstance.getRepoInstance().getUsersFromDatabase()
                    .toFlowable()
                    .flatMap(users -> {
                        return Flowable.fromIterable(users);
                    })
                    .filter(user -> {
                        return user.getId().equals(userId);
                    })
                    .subscribe(s -> {
                        Log.d("DEBUG", s + "d");
                        String picName = s.getPic_url();
                        Log.d("DEBUG", picName + "d");
                        File imageFile = new File(MyApp.appInstance.getExternalImageFolder(),
                                "/" +  picName);
                        try {
                            if(imageFile.createNewFile()) {
                                // empty file created then download it
                                RxFirebaseStorage.getFile(storageRefImages
                                        .child(picName), imageFile)
                                        .subscribeOn(Schedulers.io())
                                        .toCompletable()
                                        .doOnComplete(() -> {
                                            drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
                                        })
                                        .blockingAwait();
                            } else {
                                drawable = Drawable.createFromPath(imageFile.getAbsolutePath());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("DEBUG", "USER ICON SET" + drawable);

            imageView.setImageDrawable(drawable);
        }
    }

}
