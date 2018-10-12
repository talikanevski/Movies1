package com.example.ded.movies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of news articles by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class MovieLoader  extends AsyncTaskLoader<List<Movie>> {

    /** Tag for log messages */
    public static final String LOG_TAG = MovieLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Test:  onStartLoading called");

        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        Log.i(LOG_TAG, "Test:  loadInBackground called");

        if (mUrl == null) {
            return null;
        }

        /**Perform the network request, parse the response, and extract a list of news.**/
        List<Movie> movies = QueryUtils.fetchNewsData(mUrl);
        return movies;
    }
}
