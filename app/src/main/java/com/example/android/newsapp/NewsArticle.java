package com.example.android.newsapp;

import java.util.Date;


public class NewsArticle {

    private String mHeadline;
    private String mSection;
    private String url;
    private String mDate;
    private String author;
    private String storyImage;

    public NewsArticle() {
    }

    public NewsArticle(String headline, String section) {
        mHeadline = headline;
        mSection = section;
    }

    public NewsArticle(String headline, String section, String url) {
        mHeadline = headline;
        mSection = section;
        this.url = url;
    }

    public NewsArticle(String headline, String section, String url, String date) {
        mHeadline = headline;
        mSection = section;
        this.url = url;
        mDate = date;
    }

    public NewsArticle(String headline, String section, String url, String date, String author) {
        mHeadline = headline;
        mSection = section;
        this.url = url;
        mDate = date;
        this.author = author;
    }

    public NewsArticle(String mHeadline, String mSection, String url, String mDate, String author, String storyImage) {
        this.mHeadline = mHeadline;
        this.mSection = mSection;
        this.url = url;
        this.mDate = mDate;
        this.author = author;
        this.storyImage = storyImage;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;
    }

    public String getSection() {
        return mSection;
    }

    public void setSection(String section) {
        mSection = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(String storyImage) {
        this.storyImage = storyImage;
    }

    @Override
    public String toString() {
        return "NewsArticle{" +
                "mHeadline='" + mHeadline + '\'' +
                ", mSection='" + mSection + '\'' +
                '}';
    }
}
