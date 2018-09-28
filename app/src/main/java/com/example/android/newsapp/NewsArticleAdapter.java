package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {

    public NewsArticleAdapter(Context context, List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        NewsArticle newsArticle = getItem(position);

        TextView headline = (TextView) listView.findViewById(R.id.headline);
        headline.setText(newsArticle.getHeadline());

        TextView description = (TextView) listView.findViewById(R.id.category);
        description.setText(newsArticle.getSection());

        TextView date = (TextView) listView.findViewById(R.id.date);
        date.setText(newsArticle.getDate());

        TextView author = (TextView) listView.findViewById(R.id.author);
        author.setText(newsArticle.getAuthor());

        return listView;
    }
}
