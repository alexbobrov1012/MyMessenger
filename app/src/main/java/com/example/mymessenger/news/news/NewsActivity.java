package com.example.mymessenger.news.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.R;
import com.example.mymessenger.news.room.Channel;
import com.example.mymessenger.news.room.News;
import com.example.mymessenger.presentation.OnItemListClickListener;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class NewsActivity extends AppCompatActivity implements OnItemListClickListener {

    private NewsViewModel viewModel;

    private String link;

    NewsListAdapter adapter;

    private static String TAG = "NEWSACTIVITY123";

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.activity_main_news);
        Channel channel = (Channel) getIntent().getSerializableExtra("channel");
        link = channel.getLink();
        String name = channel.getName();
        setTitle(name);

//        Toolbar toolbar = findViewById(R.id.toolbarNews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.fetchNews(link);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                Log.d(TAG, "OnChanged "  + link +" | " + viewModel.getChannelRepo());
                if (TextUtils.equals(link, viewModel.getChannelRepo())
                        || viewModel.getChannelRepo().equals("")) {
                    adapter.setNews(news);
                } else {
                    adapter.removeItems();
                }
            }
        });

        viewModel.fetchNews(link);
    }

    @Override
    public void onItemListClick(int position) {
        Intent intent = new Intent(this, WebActivity.class);
        News news = adapter.getNewsItem(position);
        intent.putExtra("news", news);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class NewsViewModel extends ViewModel {
        private LiveData<List<News>> allNews;

        public NewsViewModel() {
            allNews = MyApp.appInstance.getNewsRepository().getAllNews();
        }

        LiveData<List<News>> getAllNews() {
            return allNews;
        }

        void fetchNews(String link) {
            MyApp.appInstance.getNewsRepository().fetchNews(link);
        }

        String getChannelRepo() {
           return MyApp.appInstance.getNewsRepository().getCurrentChannel();
        }
    }
}
