package com.example.ded.movies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String THE_MOVIE_DB = "https://api.themoviedb.org/3/discover/movie?";
    private static final String API_Key = "api_key=27489f2914bd9f638025496deaad801e";
    private static final String THE_MOVIE_DB_REQUEST_URL = THE_MOVIE_DB + API_Key;

    /**Constant value for the movies loader ID. We can choose any integer.*/
    private static final int MOVIES_LOADER_ID = 1;

    /** Adapter for the list of movies */
    private MovieAdapter mAdapter;

    /** TextView that is displayed when the list is empty**/
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Test: Main Activity onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Find a reference to the {@link ListView} in the layout**/
        ListView moviesListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        moviesListView.setEmptyView(mEmptyStateTextView);

        /**Create a new {@link ArrayAdapter} of movies
         // Create a new adapter that takes an empty list of movies as input**/
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        /**Set the adapter on the {@link ListView}
         // so the list can be populated in the user interface**/
        moviesListView.setAdapter(mAdapter);

        /** Set an item click listener on the ListView, which sends an intent to **/
        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /**Find the current news article that was clicked on**/
                Movie currentMovie = mAdapter.getItem(position);

//                /** Convert the String URL into a URI object (to pass into the Intent constructor)
//                 /** The Intent constructor requires a Uri object, so we need to convert the URL
//                 * (in the form of a String) into a URI.The news URL is a more specific form of a URI,
//                 * so we can use the Uri.parse method**/
//
//                Uri articleUri = Uri.parse(currentArticle.getUrl());
//
//                /** Once we have the website URL in a Uri object, we can create a new intent**/
//                /**Create a new intent to view the earthquake URI**/
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
//
//                /** Send the intent to launch a new activity**/
//                startActivity(websiteIntent);
            }
        });
        /** In case that there is no internet connection:
         * I don't want to show that "No movies found" -
         * I want to show that "No internet connection"
         * Get a reference to the ConnectivityManager to check state of network connectivity**/

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /**Get details on the currently active default data network**/
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /** If there is a network connection, fetch data**/
        if (networkInfo != null && networkInfo.isConnected()) {

            /** to retrieve a movie info, we need to get the loader manager
             *  and tell the loader manager to initialize the loader with the specified ID,
             *  the second argument allows us to pass a bundle of additional information, which we'll skip.
             *  The third argument is what object should receive the LoaderCallbacks
             *  (and therefore, the data when the load is complete!) - which will be this activity.
             *  This code goes inside the onCreate() method of the MainActivity,
             *  so that the loader can be initialized as soon as the app opens.**/

            /**Get a reference to the LoaderManager, in order to interact with loaders.**/
            final LoaderManager loaderManager = getLoaderManager();

            /**Initialize the loader. Pass in the int ID constant defined above and pass in null for
             // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
             // because this activity implements the LoaderCallbacks interface).**/

            Log.i(LOG_TAG, "Test: calling initLoader");

            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);

            /** if internet connection got lost and than back, we'll see mEmptyStateTextView
             * with text "no internet connection"
             * we need to reload the app and while it's reloading it's better to see a spinner**/
            mEmptyStateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                    View loading = findViewById(R.id.loading_spinner);
                    loading.setVisibility(View.VISIBLE);
                }
            });
        } else {/** Otherwise, display error
         // First, hide loading indicator so error message will be visible**/
            View loading = findViewById(R.id.loading_spinner);
            loading.setVisibility(View.GONE);

            /** Update empty state with no connection error message**/
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "Test:  onCreateLoader called");

        // Create a new loader for the given URL
        return new MovieLoader(this, THE_MOVIE_DB_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        Log.i(LOG_TAG, "Test:  onLoadFinished called");

        ProgressBar loading = findViewById(R.id.loading_spinner);
        loading.setVisibility(View.GONE);

        /** in case the internet connection got lost in the middle**/
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Set empty state text to display "No movies found."
            mEmptyStateTextView.setText(R.string.no_movies);
        } else// Update empty state with no connection error message
        {
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of movies, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(LOG_TAG, "Test:  onLoaderReset called");

        /** Loader reset, so we can clear out our existing data.**/
        mAdapter.clear();
    }

    public void reload() {
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
    }
}