package com.example.mymessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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

    private static final int RC_SIGN_IN = 3228;

    List<AuthUI.IdpConfig> providers;

    Button button;

    private ViewPagerAdapter viewPagerAdapter;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        Snackbar.make(findViewById(R.id.viewPager),"" + userAuth.getCurrentUser().getUid(), Snackbar.LENGTH_LONG).show();
        //addNewUserToDB(new User(userAuth.getUid(), userAuth.getCurrentUser().getDisplayName(), userAuth.getCurrentUser().getPhotoUrl()));
        if (userAuth.getCurrentUser() != null) {
            FirebaseUserMetadata metadata = userAuth.getCurrentUser().getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                //Snackbar.make(findViewById(R.id.button),"Greetings new user!!!", Snackbar.LENGTH_LONG).show();
                Log.d("USER_", "suc");
                //addNewUserToDB(new User(userAuth.getUid(), userAuth.getCurrentUser().getDisplayName(), userAuth.getCurrentUser().getPhotoUrl()));
            } else {
                //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
            }
        } else {
            showSignInDialog();
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

    private void addNewUserToDB(User user) {
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("USER_", "suc");
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("USER_", "Error updating document", e);
                    }
                });
    }

    private void showSignInDialog() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseUserMetadata metadata = user.getMetadata();
                if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                    //Snackbar.make(findViewById(R.id.button),"Greetings new user!!!", Snackbar.LENGTH_LONG).show();
                    Log.d("USER_", "suc");
                    addNewUserToDB(new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl()));
                } else {
                    //Snackbar.make(findViewById(R.id.button),"Welcome back, " + userAuth.getCurrentUser().getDisplayName() + "!!!", Snackbar.LENGTH_LONG).show();
                }
                //Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "sign_in_cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "no_internet_connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
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
