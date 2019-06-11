package com.example.mymessenger.presentation.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.user.userprofile.UsersProfile;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UsersFragment extends Fragment implements OnItemListClickListener {

    private static final String TAG = "USERFRAGMENT_DEBUG";

    private UsersViewModel viewModel;

    private UsersRecycleViewAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        viewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        RecyclerView recyclerView = getView().findViewById(R.id.users_recycleView);
        adapter = new UsersRecycleViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewModel.fetchUsers();
        viewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    public void onItemListClick(int adapterPosition) {
        Log.d(TAG, "onItemListClick");
        Intent intent = new Intent(getActivity(), UsersProfile.class);
        User extra = adapter.getItem(adapterPosition);
        Bundle userExtra = new Bundle();
        Log.d(TAG, extra.getName());
        userExtra.putSerializable("user", extra);
        intent.putExtras(userExtra);
        startActivity(intent);
    }
}
