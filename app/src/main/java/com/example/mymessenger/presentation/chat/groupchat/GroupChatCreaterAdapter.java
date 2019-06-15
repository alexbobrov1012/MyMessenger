package com.example.mymessenger.presentation.chat.groupchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymessenger.R;
import com.example.mymessenger.SelectItemsListener;
import com.example.mymessenger.models.Message;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.AdapterSelect;
import com.example.mymessenger.presentation.chat.messaging.MessagingViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupChatCreaterAdapter extends AdapterSelect {

    List<User> usersList;

    SelectItemsListener selectItemsListener;

    public GroupChatCreaterAdapter(SelectItemsListener selectItemsListener) {
        this.selectItemsListener = selectItemsListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_view_item, parent, false);
        return new GroupChatCreaterViewHolder(view, selectItemsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GroupChatCreaterViewHolder) holder).bind(usersList.get(position));
        ((GroupChatCreaterViewHolder) holder).selectionView.setVisibility(isSelected(position)
                ? View.VISIBLE : View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        if (usersList == null)
            return 0;
        return usersList.size();
    }

    public void setUsers(List<User> users) {
        this.usersList = users;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        usersList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        // Reverse-sort the list
        Collections.sort(positions, new Comparator<Integer>() {
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return rhs - lhs;
            }
        });

        // Split the list in ranges
        while (!positions.isEmpty()) {
            if (positions.size() == 1) {
                removeItem(positions.get(0));
                positions.remove(0);
            } else {
                int count = 1;
                while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
                    ++count;
                }

                if (count == 1) {
                    removeItem(positions.get(0));
                } else {
                    removeRange(positions.get(count - 1), count);
                }

                for (int i = 0; i < count; ++i) {
                    positions.remove(0);
                }
            }
        }
    }

    private void removeRange(int positionStart, int itemCount) {
        for (int i = 0; i < itemCount; ++i) {
            usersList.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public String getUserId(int index) {
        return usersList.get(index).getId();
    }

    public List<String> getUserIds(List<Integer> selectedItems) {
        ArrayList<String> result = new ArrayList<>();
        for(Integer pos : selectedItems) {
            result.add(getUserId(pos));
        }
        return result;
    }
}
