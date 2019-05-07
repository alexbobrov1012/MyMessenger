package com.example.mymessenger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
