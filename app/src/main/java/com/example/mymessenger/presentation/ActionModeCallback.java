package com.example.mymessenger.presentation;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.example.mymessenger.R;

public abstract class ActionModeCallback implements ActionMode.Callback {

    private final String TAG = ActionModeCallback.class.getSimpleName();

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate (R.menu.selction_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

}
