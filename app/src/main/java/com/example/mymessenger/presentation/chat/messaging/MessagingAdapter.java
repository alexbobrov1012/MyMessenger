package com.example.mymessenger.presentation.chat.messaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.chat.ChatsRecycleViewHolder;

import java.util.List;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingViewHolder>{

    List<Message> messages;

    OnItemListClickListener listener;

    public MessagingAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessagingViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagingViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public String getMessageId(int index) {
        return messages.get(index).getId();
    }
}
