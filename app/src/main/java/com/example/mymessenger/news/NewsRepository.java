package com.example.mymessenger.news;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.mymessenger.MyApp;
import com.example.mymessenger.news.network.NewsParser;
import com.example.mymessenger.news.network.NewsParserATOM;
import com.example.mymessenger.news.network.NewsParserRSS;
import com.example.mymessenger.news.network.NewsWorker;
import com.example.mymessenger.news.room.Channel;
import com.example.mymessenger.news.room.ChannelDao;
import com.example.mymessenger.news.room.News;
import com.example.mymessenger.news.room.NewsDao;
import com.example.mymessenger.news.room.NewsRoomDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewsRepository {
    private NewsDao newsDao;

    private ChannelDao channelDao;

    private LiveData<List<News>> AllNews;

    private LiveData<List<Channel>> AllChannels;

    private String currentChannel;

    private NewsParser parser;

    private Executor executor;

    private PeriodicWorkRequest NewsWorkRequestPeriodic;

    private final String TAG_SERVICE = "Worker123";

    public NewsRepository() {
        NewsRoomDatabase db = MyApp.appInstance.getDbInstance();
        newsDao = db.newsDao();
        channelDao = db.channelDao();
        AllNews = newsDao.getAllNews();
        AllChannels = channelDao.getAllChannels();
        currentChannel = "";
        executor =  Executors.newSingleThreadExecutor();
        prePopulateDB();
    }

    private void prePopulateDB() {
        Channel channel = new Channel("Google-news", "https://news.google.com/news/rss");
        insert(channel);
        channel = new Channel("Авто", "https://news.yandex.ru/auto.rss");
        insert(channel);
        channel = new Channel("Армия", "https://news.yandex.ru/army.rss");
        insert(channel);
        channel = new Channel("Баскетбол", "https://news.yandex.ru/basketball.rss");
        insert(channel);
        channel = new Channel("Бизнес", "https://news.yandex.ru/business.rss");
        insert(channel);
        channel = new Channel("Важное за сутки", "https://news.yandex.ru/daily.rss");
        insert(channel);
        channel = new Channel("В мире", "https://news.yandex.ru/world.rss");
        insert(channel);
        channel = new Channel("Гаджеты", "https://news.yandex.ru/gadgets.rss");
        insert(channel);
        channel = new Channel("Здоровье", "https://news.yandex.ru/health.rss");
        insert(channel);
        channel = new Channel("Игры", "https://news.yandex.ru/games.rss");
        insert(channel);
        channel = new Channel("Культура", "https://news.yandex.ru/culture.rss");
        insert(channel);
        channel = new Channel("Космос", "https://news.yandex.ru/cosmos.rss");
        insert(channel);
        channel = new Channel("Музыка", "https://news.yandex.ru/music.rss");
        insert(channel);
        channel = new Channel("Наука", "https://news.yandex.ru/science.rss");
        insert(channel);
        channel = new Channel("Спорт", "https://news.yandex.ru/sport.rss");
        insert(channel);
        channel = new Channel("Шоу-бизнес", "https://news.yandex.ru/showbusiness.rss");
        insert(channel);
        channel = new Channel("Хоккей", "https://news.yandex.ru/hockey.rss");
        insert(channel);
        channel = new Channel("Футбол", "https://news.yandex.ru/football.rss");
        insert(channel);


    }

    public LiveData<List<News>> getAllNews() {
        return AllNews;
    }

    public LiveData<List<Channel>> getAllChannels() {
        return AllChannels;
    }

    public void serviceRoutine() {
        List<News> listNews;
        InputStream in;

        // get and parse news
        Log.d(TAG_SERVICE, "in run..");
        try {
            HttpURLConnection connection;
            URL url = new URL(currentChannel);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            in = connection.getInputStream();
            if (in == null) {
                Log.d(TAG_SERVICE, "instream is null");
                return;
            }
            listNews = parser.parse(in);
            in.close();
            connection.disconnect();
            if(listNews == null) {
                Log.d(TAG_SERVICE, "parsing failed");
                return;
            } else {
                deleteAll();
                insert(listNews);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG_SERVICE, "out run..");
    }

    public void fetchNews(String channelLink) {
        if (TextUtils.equals(channelLink,currentChannel) || !isOnline()) {
            // fetch from db
            AllNews = newsDao.getAllNews();
        } else {
            currentChannel = channelLink;
            // fetch from network
            if (currentChannel.toLowerCase().contains("rss")) {
                // rss parser
                parser = new NewsParserRSS();

            } else if (currentChannel.toLowerCase().contains("atom")) {
                // atom parser
                parser = new NewsParserATOM();

            }
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(
                    NetworkType.CONNECTED).build();
            if (NewsWorkRequestPeriodic == null) {
                NewsWorkRequestPeriodic = new PeriodicWorkRequest.Builder(
                        NewsWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                          .build();
                WorkManager.getInstance().enqueueUniquePeriodicWork("fetch_news_worker",
                        ExistingPeriodicWorkPolicy.REPLACE, NewsWorkRequestPeriodic);
            } else {
                OneTimeWorkRequest newsWorkRequest = new OneTimeWorkRequest.Builder(NewsWorker.class).
                        setConstraints(constraints).build();
                WorkManager.getInstance().enqueue(newsWorkRequest);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApp.appInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
    private void insert(List<News> newsList) {
        for (News tok : newsList) {
            newsDao.insert(tok);
        }
    }

    public void insert(final Channel channel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channelDao.insert(channel);
            }
        });
    }

    public void delete(final String link) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channelDao.delete(link);
            }
        });

    }

    private void deleteAll() {
        newsDao.deleteAll();
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }
}
