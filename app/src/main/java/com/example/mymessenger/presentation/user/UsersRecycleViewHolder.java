package com.example.mymessenger.presentation.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mymessenger.R;
import com.example.mymessenger.presentation.User;

public class UsersRecycleViewHolder extends RecyclerView.ViewHolder {
    private TextView nameView;

    public void bind(User model) {
        nameView.setText(model.getName());
    }

    public UsersRecycleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.nameView = itemView.findViewById(R.id.userNameTextView);
    }
}
