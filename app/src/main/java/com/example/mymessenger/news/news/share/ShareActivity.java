package com.example.mymessenger.news.news.share;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.chat.ChatsRecycleViewAdapter;
import com.example.mymessenger.presentation.chat.ChatsViewModel;
import com.example.mymessenger.presentation.chat.messaging.MessagingActivity;

public class ShareActivity extends AppCompatActivity implements OnItemListClickListener {

    ShareViewModel viewModel;

    ChatsRecycleViewAdapter adapter;

    String messageToShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        messageToShare = getIntent().getStringExtra("share");
        viewModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        viewModel.fetchChannels();
        RecyclerView recyclerView = findViewById(R.id.shareActivityRecyclerView);
        adapter = new ChatsRecycleViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getAllChannels().observe(this, channels -> {
            adapter.setChats(channels);
        });

    }

    @Override
    public void onItemListClick(int adapterPosition) {
        Intent intent = new Intent(this, MessagingActivity.class);
        intent.putExtra("messageToShare", messageToShare);
        intent.putExtra("channel", adapter.getChannel(adapterPosition));
        finish();
        startActivity(intent);
    }
}
