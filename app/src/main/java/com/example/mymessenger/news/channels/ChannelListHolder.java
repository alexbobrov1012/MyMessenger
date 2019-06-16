package com.example.mymessenger.news.channels;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.news.room.Channel;
import com.example.mymessenger.presentation.OnItemListClickListener;

public class ChannelListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameView;

    private String link;

    private OnItemListClickListener listener;

    public ChannelListHolder(@NonNull final View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.nameView = itemView.findViewById(R.id.channelName);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(Channel model) {
        nameView.setText(model.getName());
        link = model.getLink();
    }

    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition());
    }
}
