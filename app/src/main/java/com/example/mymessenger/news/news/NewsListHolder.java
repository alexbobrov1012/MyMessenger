package com.example.mymessenger.news.news;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.news.room.News;
import com.example.mymessenger.presentation.OnItemListClickListener;

public class NewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView titleView;

    private TextView dateView;

    private TextView contentsView;

    private OnItemListClickListener listener;

    //TODO:
    private String link;

    public NewsListHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.listener = listener;
        this.titleView = itemView.findViewById(R.id.title);
        this.dateView = itemView.findViewById(R.id.date);
        this.contentsView = itemView.findViewById(R.id.contents);
        itemView.setOnClickListener(this);
    }

    public void bind(News model) {
        titleView.setText(model.getTitle());
        dateView.setText(model.getDate());
        contentsView.setText(model.getContent());
        link = model.getLink();
    }

    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition());
    }
}
