package com.example.mymessenger.news.channels;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.models.User;
import com.example.mymessenger.news.news.NewsActivity;
import com.example.mymessenger.news.room.Channel;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.user.UsersRecycleViewAdapter;
import com.example.mymessenger.presentation.user.UsersViewModel;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class ChannelFragment extends Fragment implements OnItemListClickListener {

    ChannelViewModel viewModel;

    ChannelListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_channel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerview_channel);
        adapter = new ChannelListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getAllChannels().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(List<Channel> channels) {
                adapter.setChannels(channels);
            }
        });

    }
    @Override
    public void onItemListClick(int position) {
        Intent intent = new Intent(getContext(), NewsActivity.class);
        Channel channel = adapter.getChannel(position);
        intent.putExtra("channel", channel);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }
}
