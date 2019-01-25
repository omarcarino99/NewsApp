package com.example.android.newsapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.NewsArticle;
import com.example.android.newsapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

        ImageView storyImage = (ImageView) listView.findViewById(R.id.story_picture);
        ImageLoader.getInstance().displayImage(newsArticle.getStoryImage(), storyImage);

        return listView;
    }
}
