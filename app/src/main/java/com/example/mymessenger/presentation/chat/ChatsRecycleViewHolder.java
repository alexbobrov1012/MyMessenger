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

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView chatImageView;

    TextView chatNameTextView;

    TextView lastMessageTextView;

    OnItemListClickListener listener;

    public ChatsRecycleViewHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        chatImageView = itemView.findViewById(R.id.chatIconImageView);
        chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
        lastMessageTextView = itemView.findViewById(R.id.chatLastMessageTextView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(Channel model) {
        chatNameTextView.setText(model.getName());
        chatImageView.setImageBitmap(MyApp.appInstance.getRepoInstance().getImage(model.getIcon()));

    }


    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition());
    }
}
