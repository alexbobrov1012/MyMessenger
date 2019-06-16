package com.example.mymessenger.presentation.chat.groupchat.addusers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.presentation.ActionModeCallback;
import com.example.mymessenger.presentation.chat.groupchat.GroupChatCreaterAdapter;
import com.example.mymessenger.presentation.chat.groupchat.GroupChatCreaterViewModel;

import java.util.List;

public class AddUsersActivity extends AppCompatActivity implements SelectItemsListener, View.OnClickListener{

    private AddUserViewModel viewModel;

    private GroupChatCreaterAdapter adapter;

    ActionMode actionMode = null;

    private ActionModeCallback actionModeCallback;

    String chatId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addusers);


        RecyclerView recyclerView = findViewById(R.id.addUsersRecyclerView);
        adapter = new GroupChatCreaterAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatId = getIntent().getStringExtra("chatId");
        viewModel = ViewModelProviders.of(this).get(AddUserViewModel.class);
        viewModel.fetchFreeUsers(chatId);
        viewModel.getFreeUsers().observe(this, users -> {
            adapter.setUsers(users);
        });


        actionModeCallback = new ActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.submit_selection:
                        List<String> userIds = adapter.getUserIds(adapter.getSelectedItems());
                        viewModel.addUsersToChannel(userIds, chatId);
                        Log.d("DEBUG", "ADD USER TO CHANNEL" + adapter.getSelectedItems());
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

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate (R.menu.add_users_menu, menu);
                return true;
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.choose_users));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.submit_selection) {
            //TODO:
            Log.d("DEBUG", "SUBMIT");
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {

    }
}
