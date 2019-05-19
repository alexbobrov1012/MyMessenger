package com.example.mymessenger.presentation.user;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.presentation.User;

public class UsersRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameView;

    private TextView statusView;

    private ImageView iconView;

    private OnItemListClickListener listener;

    public void bind(User model) {
        if(model.getName().length() > 16) {
            nameView.setTextSize(15);
        } else {
            nameView.setTextSize(25);
        }
        nameView.setText(model.getName());
        statusView.setText(model.getStatus());
        iconView.setImageBitmap(MyApp.appInstance.getRepoInstance().getImage(model.getPic_url()));
    }

    public UsersRecycleViewHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.nameView = itemView.findViewById(R.id.userNameTextView);
        this.statusView = itemView.findViewById(R.id.userStatusTextView);
        this.iconView = itemView.findViewById(R.id.userIconImageView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition());
    }
}
