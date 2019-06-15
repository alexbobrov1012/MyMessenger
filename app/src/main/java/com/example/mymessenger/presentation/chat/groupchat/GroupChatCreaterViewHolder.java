package com.example.mymessenger.presentation.chat.groupchat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.OnItemListClickListener;
import com.example.mymessenger.presentation.user.UsersRecycleViewHolder;

public class GroupChatCreaterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
View.OnLongClickListener{

    private TextView nameView;

    private TextView statusView;

    private ImageView iconView;

    private SelectItemsListener selectItemsListener;

    View selectionView;

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

    public GroupChatCreaterViewHolder(@NonNull View itemView, SelectItemsListener listener) {
        super(itemView);
        this.nameView = itemView.findViewById(R.id.userNameTextView);
        this.statusView = itemView.findViewById(R.id.userStatusTextView);
        this.iconView = itemView.findViewById(R.id.userIconImageView);
        this.selectionView = itemView.findViewById(R.id.selectionView);
        this.selectItemsListener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (selectItemsListener != null) {
            selectItemsListener.onItemClicked(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (selectItemsListener != null) {
            return selectItemsListener.onItemLongClicked(getAdapterPosition());
        }
        return false;
    }
}
