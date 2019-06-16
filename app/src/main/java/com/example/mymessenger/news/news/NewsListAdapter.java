package com.example.mymessenger.news.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.news.room.News;
import com.example.mymessenger.presentation.OnItemListClickListener;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListHolder> {

    private List<News> news;

    private OnItemListClickListener listener;

    public NewsListAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new NewsListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        if (news == null)
            return 0;
        return news.size();
    }

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public void removeItems() {
        if (news != null)
            news = null;
    }

    public String getNewsTitle(int position) {
        return news.get(position).getTitle();
    }

    public News getNewsItem(int position) {
        return news.get(position);
    }
}
