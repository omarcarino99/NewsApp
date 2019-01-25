package com.example.android.newsapp.Fragments;


import android.content.Context;
import android.content.Loader;
import android.support.annotation.Nullable;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.android.newsapp.Adapters.NewsArticleAdapter;
import com.example.android.newsapp.MainActivity;
import com.example.android.newsapp.NewsArticle;
import com.example.android.newsapp.NewsArticleLoader;
import com.example.android.newsapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


public class BusinessFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final String URL_GUARDIAN = "https://content.guardianapis.com/search?api-key=bf9f4d70-7d59-4e67-a316-760ba44e52c4";
    private static final int NEWSARTICLE_LOADER_ID = 1;
    public String topic;
    public  NewsArticleAdapter adapter;

    public BusinessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.story_list,container,false);
        ListView listView = rootview.findViewById(R.id.story_list);
        adapter = new NewsArticleAdapter(getContext(),new ArrayList<NewsArticle>());
        listView.setAdapter(adapter);

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
    public android.support.v4.content.Loader<List<NewsArticle>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String date = sharedPreferences.getString(
                getString(R.string.settings_order_by_date_key),
                getString(R.string.settings_order_by_date_default)
        );

        topic = "business";
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
    public void onLoadFinished(android.support.v4.content.Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<NewsArticle>> loader) {
        adapter.clear();
    }
}
