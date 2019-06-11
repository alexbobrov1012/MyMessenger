package com.example.mymessenger.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.models.User;
import com.example.mymessenger.presentation.chat.ChatsFragment;
import com.example.mymessenger.presentation.profile.ProfileFragment;
import com.example.mymessenger.presentation.user.UsersFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import durdinapps.rxfirebase2.RxFirestore;

public class MainActivity extends AppCompatActivity implements OnCompleteListener{

    private static final String TAG = "MAIN_DEBUG";

    private ViewPagerAdapter viewPagerAdapter;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private MainViewModel viewModel;

    private ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.userSignInCheck(this);
        progressBar = findViewById(R.id.loadingProgressBarMain);
        progressBar.setVisibility(View.VISIBLE);
        //MyApp.appInstance.showLoading(this);
        viewModel.loading.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "onChanged");
                if(integer.equals(View.GONE)) {
                    Toolbar toolbar = findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    tabLayout = findViewById(R.id.tabs);
                    viewPager = findViewById(R.id.viewPager);
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                    viewPagerAdapter.addFragment(new UsersFragment(), "Users");
                    viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
                progressBar.setVisibility(integer);
            }
        });

        viewModel.fetchCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.checkForSignInResult(requestCode, resultCode, data, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(this, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        this.recreate();
    }
}
