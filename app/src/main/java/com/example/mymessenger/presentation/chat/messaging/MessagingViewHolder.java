package com.example.mymessenger.presentation.chat.messaging;

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

import java.text.SimpleDateFormat;

public class MessagingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView textTextView;

    TextView dateTextView;

    TextView fileNameTextView;

    ImageView imageImageView;

    LinearLayout fileLayout;

    LinearLayout itemLayout;

    OnItemListClickListener listener;

    public MessagingViewHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.listener = listener;
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
            imageImageView.setImageBitmap(MyApp.appInstance.getRepoInstance()
                    .getImage(model.getContentImage()));
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
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        dateTextView.setText(dateFormat.format(model.getDate()));
    }


    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition());
    }
}
