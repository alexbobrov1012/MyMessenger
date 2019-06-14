package com.example.mymessenger.presentation.chat.messaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.example.mymessenger.models.Message;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.CompletableSource;
import io.reactivex.schedulers.Schedulers;

public class MessagingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView textTextView;

    TextView dateTextView;

    TextView fileNameTextView;

    ImageView imageImageView;

    LinearLayout fileLayout;

    LinearLayout itemLayout;

    OnItemListClickListener listener;

    String imageName;

    private StorageReference storageRefImages= FirebaseStorage.getInstance().getReference().child("Images");

    public MessagingViewHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.listener = listener;
        imageName = "";
        itemView.setOnClickListener(this);
        this.textTextView = itemView.findViewById(R.id.text_message);
        this.dateTextView = itemView.findViewById(R.id.date_message);
        this.fileNameTextView = itemView.findViewById(R.id.fileName_message);
        this.imageImageView = itemView.findViewById(R.id.imageView_message);
        this.fileLayout = itemView.findViewById(R.id.layoutFileMessage);
        this.itemLayout = itemView.findViewById(R.id.messageLayoutText);
    }

    public void bind(Message model) {
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
            fileNameTextView.setText(model.getContentFile());
        }

        if(!model.getId().equals(MyApp.appInstance.getRepoInstance().getUserInstance().getId())) {
            itemLayout.setBackgroundResource(R.drawable.shape_message_text_item_other);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            itemLayout.setLayoutParams(params);
            Log.d("DEBUG", "itemLayout");
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
        listener.onItemListClick(getAdapterPosition());
    }
}
