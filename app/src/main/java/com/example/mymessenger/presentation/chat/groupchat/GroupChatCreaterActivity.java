package com.example.mymessenger.presentation.chat.groupchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.ImageManager;
import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.presentation.ActionModeCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GroupChatCreaterActivity extends AppCompatActivity implements SelectItemsListener, View.OnClickListener {

    private static final int RC_ICON = 4001;

    private GroupChatCreaterAdapter adapter;

    private TextView selectedIconNameTextView;

    private EditText chatNameEditText;

    private Button selectIconButton;

    private Button submitButton;

    private GroupChatCreaterViewModel viewModel;

    private Uri channelIconUri;

    ActionMode actionMode = null;

    private ActionModeCallback actionModeCallback;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_ICON && resultCode == RESULT_OK) {
            if (data != null) {
                channelIconUri = data.getData();
                selectedIconNameTextView.setText(R.string.select_chat_icon_added);
//                try {
//                    channelIconName = UUID.randomUUID().toString();
//                    File newImageFile = new File(MyApp.appInstance.getExternalImageFolder(), channelIconName);
//                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
//                    ImageManager.saveImageExternal(imageBitmap, newImageFile);
//                    ImageManager.uploadImage(newImageFile);
//                    Toast.makeText(this, R.string.edit_profile_image_set_suc, Toast.LENGTH_SHORT).show();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    channelIconName = null;
//                    Toast.makeText(this, R.string.edit_profile_image_set_fail, Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);

        this.chatNameEditText = findViewById(R.id.nameInputGroup);
        this.selectedIconNameTextView = findViewById(R.id.iconNameGroup);
        this.selectIconButton = findViewById(R.id.selectIconButton);
        selectIconButton.setOnClickListener(this);
        this.submitButton = findViewById(R.id.submitGroupChatButton);
        submitButton.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.group_recyclerView);
        adapter = new GroupChatCreaterAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(GroupChatCreaterViewModel.class);
        viewModel.fetchUsers();
        viewModel.getAllUsersList().observe(this, users -> {
            adapter.setUsers(users);
        });

        actionModeCallback = new ActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // nothing to do
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.clearSelection();
                actionMode = null;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate (R.menu.group_menu, menu);
                return true;
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.group_chat_creating_title));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemClicked(int position) {
        Log.d("DEBUG", "CLICKED");
//        if (actionMode != null) {
//            toggleSelection(position);
//        }
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {

        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submitGroupChatButton) {
            List<String> userIds = adapter.getUserIds(adapter.getSelectedItems());
            viewModel.submitNewChannel(
                    chatNameEditText.getText().toString(),
                    channelIconUri,
                    userIds,
                    this);
            Log.d("DEBUG", "SUBMIT");


            return;
        }

        if(v.getId() == R.id.selectIconButton) {
            Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, RC_ICON);
            }
        }

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
