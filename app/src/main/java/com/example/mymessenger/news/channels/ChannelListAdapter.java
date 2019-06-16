package com.example.mymessenger.news.channels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.news.room.Channel;
import com.example.mymessenger.presentation.OnItemListClickListener;

import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListHolder> {
    private List<Channel> channels;

    private OnItemListClickListener listener;

    public ChannelListAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChannelListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_item, parent, false);
        return new ChannelListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelListHolder holder, int position) {
        holder.bind(channels.get(position));
    }

    @Override
    public int getItemCount() {
        if (channels == null)
            return 0;
        return channels.size();
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public int getChannelId(int position) {
        return channels.get(position).getId();
    }

    public String getChannelLink(int position) {
        return channels.get(position).getLink();
    }

    public String getChannelName(int position) {
        return channels.get(position).getName();
    }

    public Channel getChannel(int position) {
        return channels.get(position);
    }
}
