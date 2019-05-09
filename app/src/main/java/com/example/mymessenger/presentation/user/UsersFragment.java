package com.example.mymessenger.presentation.user;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mymessenger.R;
import com.example.mymessenger.UsersViewModel;
import com.example.mymessenger.presentation.User;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class UsersFragment extends Fragment implements EventListener<QuerySnapshot> {

    private static final String TAG = "UsersFragment";
    private UsersViewModel viewModel;

    private UsersRecycleViewAdapter adapter;

    private ListenerRegistration registration;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        RecyclerView recyclerView = getView().findViewById(R.id.users_recycleView);
        adapter = new UsersRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
    public void onEvent(@javax.annotation.Nullable QuerySnapshot querySnapshots,
                        @javax.annotation.Nullable FirebaseFirestoreException e) {
        if(e != null) {
            Toast.makeText(getActivity(), "Error:" + e.getMessage() + e.getCode(),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.setUsers(querySnapshots.toObjects(User.class));
    }
}
