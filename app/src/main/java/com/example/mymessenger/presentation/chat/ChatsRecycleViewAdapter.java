package com.example.mymessenger.presentation.chat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.models.Channel;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;

import java.util.List;

public class ChatsRecycleViewAdapter extends RecyclerView.Adapter<ChatsRecycleViewHolder> {

    List<Channel> chats;

    OnItemListClickListener listener;

    public ChatsRecycleViewAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ChatsRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_list_view_item, viewGroup, false);
        return new ChatsRecycleViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsRecycleViewHolder chatsRecycleViewHolder, int i) {
        chatsRecycleViewHolder.bind(chats.get(i));
    }

    @Override
    public int getItemCount() {
        if (chats == null)
            return 0;
        return chats.size();
    }

    public void setChats(List<Channel> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }
}
