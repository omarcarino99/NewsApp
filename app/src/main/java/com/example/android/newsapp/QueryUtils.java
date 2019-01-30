package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.MainActivity.LOG_TAG;


public final class QueryUtils {

    private static final String LOG_MESSAGE = "MISSING_FIELD";
    private String image;

    private QueryUtils() {
    }

    public static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, String.valueOf(R.string.error_response_code + httpURLConnection.getResponseCode()));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.error_message_json), e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<NewsArticle> parseJsonData(String newsArticleJson) {
        if (TextUtils.isEmpty(newsArticleJson)) {
            return null;
        }

        List<NewsArticle> newsArticleList = new ArrayList<>();

        try {

            JSONObject baseObject = new JSONObject(newsArticleJson);
            JSONObject baseJSONResponse = null;
            baseJSONResponse = baseObject.getJSONObject("response");

            JSONArray arrayOfArticles = baseJSONResponse.getJSONArray("results");

            for (int i = 0; i < arrayOfArticles.length(); i++) {
                JSONObject currentArticle = arrayOfArticles.getJSONObject(i);

                String headline = currentArticle.getString("webTitle");
                String sectionName = currentArticle.getString("sectionName");
                String url = currentArticle.getString("webUrl");
                String date = currentArticle.getString("webPublicationDate");

                JSONArray tags = currentArticle.optJSONArray("tags");
                JSONObject thumbnail;
                String  image = "";

                if (currentArticle.has("fields")){
                    thumbnail = currentArticle.getJSONObject("fields");
                  image =  thumbnail.getString("thumbnail");
                }else {
                    Log.e(LOG_MESSAGE,"No thumbnail");
                }

                String author = null;
                if (tags.length() == 0) {
                    NewsArticle newsArticleWithoutAuthor = new NewsArticle(headline, sectionName, url, date);
                    newsArticleList.add(newsArticleWithoutAuthor);
                } else {
                    JSONObject jsonAuthor = tags.optJSONObject(0);
                    author = jsonAuthor.optString("webTitle");

                    if (author == null) {
                        NewsArticle newsArticleWithoutAuthor = new NewsArticle(headline, sectionName, url, date);
                        newsArticleList.add(newsArticleWithoutAuthor);
                    } else {
                        NewsArticle newsArticle = new NewsArticle(headline, sectionName, url, date, author, image);
                        newsArticleList.add(newsArticle);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsArticleList;
    }

    public static List<NewsArticle> getJsonData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.failed_http_message), e);
        }
        List<NewsArticle> newsArticleList = parseJsonData(jsonResponse);
        return newsArticleList;
    }

}
