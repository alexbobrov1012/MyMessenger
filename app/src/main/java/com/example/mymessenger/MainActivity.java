package com.example.mymessenger;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.mymessenger.presentation.User;
import com.example.mymessenger.presentation.chat.ChatsFragment;
import com.example.mymessenger.presentation.profile.ProfileFragment;
import com.example.mymessenger.presentation.user.UsersFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "USER_AUTH";

    Button button;

    private ViewPagerAdapter viewPagerAdapter;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());

        if (viewModel.getCurrentUser() != null) {
           // Snackbar.make(findViewById(R.id.viewPager),"" + viewModel.getUserPic() + " " + viewModel.getUserId(), Snackbar.LENGTH_LONG).show();
            Log.d(TAG, "suc"+ viewModel.getUserId());
        } else {
            showSignInDialog();
            //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
        }
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

    private void showSignInDialog() {
        viewModel.initSignInFlow(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.checkForSignInResult(requestCode, resultCode, data, this);
        this.recreate();
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
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            showSignInDialog();
                        }
                    });
        }

        return super.onOptionsItemSelected(item);
    }
}
