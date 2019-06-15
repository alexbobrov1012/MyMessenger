package com.example.mymessenger.presentation.chat;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.models.Channel;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.chat.groupchat.GroupChatCreaterActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment implements EventListener<QuerySnapshot>, OnItemListClickListener {

    private static final String TAG = "CHATSFRAGMENT_DEBUG";

    private ChatsViewModel viewModel;

    private ChatsRecycleViewAdapter adapter;

    private ListenerRegistration registration;

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chats_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ChatsViewModel.class);
        viewModel.getAllUserChannels();
        RecyclerView recyclerView = getView().findViewById(R.id.chatsRecycleView);
        adapter = new ChatsRecycleViewAdapter(this);
        recyclerView.setAdapter(adapter);
        Button newGroupChannel = getActivity().findViewById(R.id.newChannelButton);
        newGroupChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupChatCreaterActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewModel.getChannelsList().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(List<Channel> channels) {
                adapter.setChats(channels);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        registration = viewModel.getQuery().addSnapshotListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        registration.remove();
    }

    @Override
    public void onItemListClick(int adapterPosition) {
        Log.d(TAG, "ItemClick");
        viewModel.startMessaging(this.getContext(), adapter.getChannel(adapterPosition));
    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        viewModel.getAllUserChannels();
    }
}
