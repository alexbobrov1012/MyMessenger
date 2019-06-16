package com.example.mymessenger.presentation.chat.groupchat.showusers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.presentation.chat.groupchat.GroupChatCreaterAdapter;
import com.example.mymessenger.presentation.chat.groupchat.GroupChatCreaterViewModel;

public class ShowUsersActivity extends AppCompatActivity implements SelectItemsListener {

    private GroupChatCreaterAdapter adapter;

    private ShowUsersViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_users);
        String chatId = getIntent().getStringExtra("chatId");
        String channelName = getIntent().getStringExtra("chatName");
        RecyclerView recyclerView = findViewById(R.id.showUsersRecyclerView);
        adapter = new GroupChatCreaterAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ShowUsersViewModel.class);
        viewModel.fetchUsers(chatId);
        viewModel.getChannelUsers().observe(this, users -> {
            adapter.setUsers(users);
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(
                String.format("%s %s:",getResources()
                        .getString(R.string.show_users_in_chat_title), channelName));

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public boolean onItemLongClicked(int position) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
