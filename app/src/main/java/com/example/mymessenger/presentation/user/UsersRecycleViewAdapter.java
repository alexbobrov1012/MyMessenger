package com.example.mymessenger.presentation.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymessenger.R;
import com.example.mymessenger.presentation.User;

import java.util.List;

public class UsersRecycleViewAdapter extends RecyclerView.Adapter<UsersRecycleViewHolder> {

    List<User> users;

    OnItemListClickListener listener;

    public UsersRecycleViewAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsersRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_list_view_item, viewGroup, false);
        return new UsersRecycleViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecycleViewHolder usersRecycleViewHolder, int i) {
        usersRecycleViewHolder.bind(users.get(i));
    }

    public void setUsers(List<User> usersList) {
        this.users = usersList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        return users.size();
    }

    public User getItem(int itemId) {
        return users.get(itemId);
    }

}
