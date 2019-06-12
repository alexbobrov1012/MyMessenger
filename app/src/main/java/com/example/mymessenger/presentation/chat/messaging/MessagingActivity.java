package com.example.mymessenger.presentation.chat.messaging;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MessagingActivity extends AppCompatActivity implements EventListener<QuerySnapshot>, OnItemListClickListener, View.OnClickListener {

    private MessagingViewModel viewModel;

    private MessagingAdapter adapter;

    private TextInputEditText textMessageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String channelId = getIntent().getStringExtra("channelId");

        adapter = new MessagingAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this, new MessagingViewModelFactory(channelId))
                .get(MessagingViewModel.class);
        viewModel.fetchMessages();
        viewModel.getAllMessagesList().observe(this, messages -> {
            adapter.setMessages(messages);
        });


        ImageView send = findViewById(R.id.imageView_send);
        send.setOnClickListener(this);
        textMessageEditText = findViewById(R.id.editText_message);

    }

    @Override
    public void onItemListClick(int adapterPosition) {

    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        viewModel.fetchMessages();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageView_send) {
            if(textMessageEditText.getText() != null) {
                viewModel.sendMessage(textMessageEditText.getText().toString());
            }
        }
    }
}
