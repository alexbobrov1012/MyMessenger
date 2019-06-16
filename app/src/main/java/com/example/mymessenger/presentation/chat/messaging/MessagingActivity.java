package com.example.mymessenger.presentation.chat.messaging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.models.Channel;
import com.example.mymessenger.presentation.ActionModeCallback;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.OnMessageClick;
import com.example.mymessenger.presentation.chat.groupchat.addusers.AddUserViewModel;
import com.example.mymessenger.presentation.chat.groupchat.addusers.AddUsersActivity;
import com.example.mymessenger.presentation.chat.groupchat.showusers.ShowUsersActivity;
import com.example.mymessenger.presentation.utils.AttachActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity implements EventListener<QuerySnapshot>,
        OnMessageClick, View.OnClickListener, SelectItemsListener {

    private static final int RC_ATTACH = 1009;

    private MessagingViewModel viewModel;

    private MessagingAdapter adapter;

    private TextInputEditText textMessageEditText;

    private ProgressBar loadingBar;

    ActionMode actionMode = null;

    private boolean isPrivate;

    private ActionModeCallback actionModeCallback;

    private Channel channel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d("DEBUG", "onCreateM");
        actionModeCallback = new ActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_remove:
                        List<String> messageIds = adapter.getIds(adapter.getSelectedItems());
                        adapter.removeItems(adapter.getSelectedItems());
                        viewModel.deleteMessages(messageIds);
                        Log.d("DEBUG", "menu_remove" + adapter.getSelectedItems());
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
                actionMode = null;
            }
        };
        channel = (Channel) getIntent().getSerializableExtra("channel");
        isPrivate = channel.isPrivate();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_messages);
        adapter = new MessagingAdapter(this, this, channel.isPrivate());
        loadingBar = findViewById(R.id.progressBarLoadingFile);
        loadingBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton navidateDown = findViewById(R.id.navigateDownFab);
        navidateDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {
                    navidateDown.show();

                } else if (dy > 0) {
                    navidateDown.hide();
                }
            }
        });
        MyApp.appInstance.getRepoInstance().isUserInChat("TL9CQKKNySQGBgknwbgNfFPRL4o2",
                "ySIgzs8DBAUz69dcmIJKlXQT5TI3TL9CQKKNySQGBgknwbgNfFPRL")
                .subscribe(s -> {
                    Log.d("DEBUG", s + " KEKA");
                });



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
        if(getIntent().getStringExtra("messageToShare") != null) {
            textMessageEditText.setText(getIntent().getStringExtra("messageToShare"));
            getIntent().removeExtra("message");
            Log.d("DEBUG", "SHARE NEWS OK");
        }
        FloatingActionButton fabAttach = findViewById(R.id.fab_attach);
        fabAttach.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!isPrivate) {
            getMenuInflater().inflate(R.menu.chat_menu, menu);
        }

        Log.d("DEBUG", "onCreateOptionsMenuM");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_users_to_chat) {
            Log.d("DEBUG", "ADD USERS TO CHAT");
            Intent intent = new Intent(this, AddUsersActivity.class);
            intent.putExtra("chatId", channel.getId());
            startActivity(intent);
        }
        if (id == R.id.action_leave_chat) {
            Log.d("DEBUG", "LEAVE CHAT");
            viewModel.leaveChat();
            finish();
        }
        if (id == R.id.action_show_users) {
            Log.d("DEBUG", "SHOW CHANNEL USERS");
            Intent intent = new Intent(this, ShowUsersActivity.class);
            intent.putExtra("chatId", channel.getId());
            intent.putExtra("chatName", channel.getName());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onFileLoadingClick(int adapterPosition) {
        String fileName = adapter.getMessageFileName(adapterPosition);
        viewModel.fileDownload(fileName,this, loadingBar);
    }

    @Override
    public void onItemClicked(int position) {
        Log.d("DEBUG", "CLICKED");
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);

        return true;
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
}
