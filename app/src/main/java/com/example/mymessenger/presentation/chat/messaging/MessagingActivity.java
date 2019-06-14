package com.example.mymessenger.presentation.chat.messaging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.utils.AttachActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity implements EventListener<QuerySnapshot>, OnItemListClickListener, View.OnClickListener {

    private static final int RC_ATTACH = 1009;

    private MessagingViewModel viewModel;

    private MessagingAdapter adapter;

    private TextInputEditText textMessageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Channel channel = (Channel) getIntent().getSerializableExtra("channel");
        adapter = new MessagingAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CircleImageView imageView = findViewById(R.id.chatTitleImageView);
        imageView.setImageBitmap(MyApp.appInstance.getRepoInstance().getImage(channel.getIcon()));
        TextView textViewTitle = findViewById(R.id.chatTitleTextView);
        textViewTitle.setText(channel.getName());
        viewModel = ViewModelProviders.of(this, new MessagingViewModelFactory(channel.getId()))
                .get(MessagingViewModel.class);
        viewModel.fetchMessages();
        viewModel.getAllMessagesList().observe(this, messages -> {
            adapter.setMessages(messages);
        });
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView send = findViewById(R.id.imageView_send);
        send.setOnClickListener(this);
        textMessageEditText = findViewById(R.id.editText_message);

        FloatingActionButton fabAttach = findViewById(R.id.fab_attach);
        fabAttach.setOnClickListener(this);

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
            if(textMessageEditText.getText() != null && !textMessageEditText.getText().toString().equals("")) {
                viewModel.sendMessage(textMessageEditText.getText().toString());
                textMessageEditText.setText("");
            }
        }

        if(v.getId() == R.id.fab_attach) {
            Intent intent = new Intent(this, AttachActivity.class);
            startActivityForResult(intent, RC_ATTACH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_ATTACH && resultCode == RESULT_OK) {
            viewModel.getAttachedFiles(data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
