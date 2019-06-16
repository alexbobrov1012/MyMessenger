package com.example.mymessenger.news.news;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymessenger.R;
import com.example.mymessenger.news.news.share.ShareActivity;
import com.example.mymessenger.news.room.News;
import com.example.mymessenger.presentation.chat.messaging.MessagingActivity;
import com.example.mymessenger.presentation.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class WebActivity extends AppCompatActivity {
    private String newsLink;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Fade());
        setContentView(R.layout.activity_web);
        News news = (News) getIntent().getSerializableExtra("news");
        String title = news.getTitle();
        setTitle(title);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        WebViewClient webViewClient = new WebViewClient() {

            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        newsLink = news.getLink();
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(webViewClient);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_web);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(newsLink);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        webView.loadUrl(newsLink);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share_news) {
            Intent intent = new Intent(this, ShareActivity.class);
            String message = String.format("Check this out:\n %s", newsLink);
            intent.putExtra("share", message);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
