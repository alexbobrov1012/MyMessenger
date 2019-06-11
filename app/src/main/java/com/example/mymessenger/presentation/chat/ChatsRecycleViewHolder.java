package com.example.mymessenger.presentation.chat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;

public class ChatsRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView chatImageView;

    TextView chatNameTextView;

    TextView lastMessageTextView;

    OnItemListClickListener listener;

    public ChatsRecycleViewHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        chatImageView = itemView.findViewById(R.id.chatIconImageView);
        chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
        lastMessageTextView = itemView.findViewById(R.id.chatLastMessageTextView);
        this.listener = listener;
    }

    public void bind(Channel model) {
        chatNameTextView.setText(model.getName());
        chatImageView.setImageBitmap(MyApp.appInstance.getRepoInstance().getImage(model.getIcon()));
       // String channelName = model.getName().get(0) + ", " + model.getName().get(1);
       // chatNameTextView.setText(channelName);
        //chatImageView.setImageBitmap();
    }


    @Override
    public void onClick(View v) {

    }
}
