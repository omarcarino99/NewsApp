package com.example.android.newsapp;

import android.content.Context;

import java.util.List;

public class NewsArticleLoader extends android.support.v4.content.AsyncTaskLoader<List<NewsArticle>> {

    private String mURL;

    public NewsArticleLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<NewsArticle> loadInBackground() {
        if (mURL == null) {
            System.out.println(R.string.bad_url_string);
            return null;
        }

        //Make the Http request
        List<NewsArticle> newsArticles = QueryUtils.getJsonData(mURL);
        return newsArticles;
    }
}
