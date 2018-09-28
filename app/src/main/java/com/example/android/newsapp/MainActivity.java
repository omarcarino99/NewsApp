package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final String URL_GUARDIAN = "https://content.guardianapis.com/search?api-key=bf9f4d70-7d59-4e67-a316-760ba44e52c4";
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWSARTICLE_LOADER_ID = 1;
    public NewsArticleAdapter mNewsArticleAdapter;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emptyTextView = findViewById(R.id.empty_view);
        listView = findViewById(R.id.news_list);

        // Checks if device is connected to the internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSARTICLE_LOADER_ID, null, this);
        } else {
            emptyTextView.setText(R.string.no_network);
            listView.setEmptyView(emptyTextView);
        }

        mNewsArticleAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());
        listView.setAdapter(mNewsArticleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticle newsArticle = mNewsArticleAdapter.getItem(position);
                Uri url = Uri.parse(newsArticle.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String date = sharedPreferences.getString(
                getString(R.string.settings_order_by_date_key),
                getString(R.string.settings_order_by_date_default)
        );
        String topic = sharedPreferences.getString(
                getString(R.string.settings_order_by_topic_key),
                getString(R.string.settings_order_by_topic_default)
        );

        Uri uri = Uri.parse(URL_GUARDIAN);
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("order-by", date);
        uriBuilder.appendQueryParameter("section", topic);

        return new NewsArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        mNewsArticleAdapter.clear();

        // If the data is valid pass it in to our adapter
        if (data != null && !data.isEmpty()) {
            mNewsArticleAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        mNewsArticleAdapter.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
