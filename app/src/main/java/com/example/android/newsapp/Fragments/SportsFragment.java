package com.example.android.newsapp.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.newsapp.NewsArticle;
import com.example.android.newsapp.Adapters.NewsArticleAdapter;
import com.example.android.newsapp.NewsArticleLoader;
import com.example.android.newsapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportsFragment extends Fragment implements  LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    public String topic;
    private static final String URL_GUARDIAN = "https://content.guardianapis.com/search?api-key=bf9f4d70-7d59-4e67-a316-760ba44e52c4";
    private NewsArticleAdapter newsArticleAdapter;
    private static final int NEWSARTICLE_LOADER_ID = 1;


    public SportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.story_list,container,false);
        newsArticleAdapter = new  NewsArticleAdapter(getContext(),new ArrayList<NewsArticle>());
        ListView listView = rootview.findViewById(R.id.story_list);
        listView.setAdapter(newsArticleAdapter);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSARTICLE_LOADER_ID, null,this);
        }
        return rootview;
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String date = sharedPreferences.getString(
                getString(R.string.settings_order_by_date_key),
                getString(R.string.settings_order_by_date_default)
        );

        topic = "sport";
        Uri uri = Uri.parse(URL_GUARDIAN);
        Uri.Builder uriBuilder = uri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("show-fields","thumbnail");
        uriBuilder.appendQueryParameter("order-by", date);
        uriBuilder.appendQueryParameter("section", topic);
        uriBuilder.build();
        return new NewsArticleLoader(getContext(),uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        newsArticleAdapter.clear();
        if (data != null && !data.isEmpty()) {
            newsArticleAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        newsArticleAdapter.clear();
    }
}
