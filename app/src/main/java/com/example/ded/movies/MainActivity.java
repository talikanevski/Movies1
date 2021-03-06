package com.example.ded.movies;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String THE_MOVIE_DB = "https://api.themoviedb.org/3/discover/movie?";
    private static final String API_Key_Label = "api_key=";
    private static final String API_Key = BuildConfig.ApiKey;
    private static final String THE_MOVIE_DB_REQUEST_URL = THE_MOVIE_DB + API_Key_Label + API_Key;

    /**
     * Constant value for the movies loader ID. We can choose any integer.
     */
    private static final int MOVIES_LOADER_ID = 1;

    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     **/
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Test: Main Activity onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Find a reference to the {@link ListView} in the layout**/
        GridView moviesGridView = findViewById(R.id.grid);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        moviesGridView.setEmptyView(mEmptyStateTextView);

        /*Create a new {@link ArrayAdapter} of movies
         // Create a new adapter that takes an empty list of movies as input**/
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        /*Set the adapter on the {@link ListView}
         // so the list can be populated in the user interface**/
        moviesGridView.setAdapter(mAdapter);

        /* Set an activity_detail click listener on the ListView, which sends an intent to **/
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*Find the current news article that was clicked on**/
                Movie currentMovie = mAdapter.getItem(position);
                launchDetailActivity(currentMovie);
            }
        });
        /* In case that there is no internet connection:
          I don't want to show that "No movies found" -
          I want to show that "No internet connection"
          Get a reference to the ConnectivityManager to check state of network connectivity**/

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /*Get details on the currently active default data network**/
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        /* If there is a network connection, fetch data**/
        if (networkInfo != null && networkInfo.isConnected()) {

            /* to retrieve a movie info, we need to get the loader manager
               and tell the loader manager to initialize the loader with the specified ID,
               the second argument allows us to pass a bundle of additional information, which we'll skip.
               The third argument is what object should receive the LoaderCallbacks
               (and therefore, the data when the load is complete!) - which will be this activity.
               This code goes inside the onCreate() method of the MainActivity,
               so that the loader can be initialized as soon as the app opens.**/

            /*Get a reference to the LoaderManager, in order to interact with loaders.**/
            final LoaderManager loaderManager = getLoaderManager();

            /*Initialize the loader. Pass in the int ID constant defined above and pass in null for
             // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
             // because this activity implements the LoaderCallbacks interface).**/

            Log.i(LOG_TAG, "Test: calling initLoader");

            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);

            /* if internet connection got lost and than back, we'll see mEmptyStateTextView
              with text "no internet connection"
              we need to reload the app and while it's reloading it's better to see a spinner**/
            mEmptyStateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                    View loading = findViewById(R.id.loading_spinner);
                    loading.setVisibility(View.VISIBLE);
                }
            });
        } else {/* Otherwise, display error
         // First, hide loading indicator so error message will be visible**/
            View loading = findViewById(R.id.loading_spinner);
            loading.setVisibility(View.GONE);

            /* Update empty state with no connection error message**/
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "Test:  onCreateLoader called");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        /* getString retrieves a String value from the preferences.
         // The second parameter is the default value for this preference.**/

        String sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default)
        );

        /* parse breaks apart the URI string that's passed into its parameter**/
        Uri baseUri = Uri.parse(THE_MOVIE_DB_REQUEST_URL);

        /* buildUpon prepares the baseUri that we just parsed so we can add query parameters to it**/
        Uri.Builder uriBuilder = baseUri.buildUpon();

        /*Append query parameter and its value.**/
        uriBuilder.appendQueryParameter("sort_by", sortBy); /*the most popular movies: sort_by=popularity.desc
          the highest rated movies : sort_by=vote_average.desc**/

        // Create a new loader for the given URL
        return new MovieLoader(this,  uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        Log.i(LOG_TAG, "Test:  onLoadFinished called");

        ProgressBar loading = findViewById(R.id.loading_spinner);
        loading.setVisibility(View.GONE);

        /* in case the internet connection got lost in the middle**/
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

        /* Loader reset, so we can clear out our existing data.**/
        mAdapter.clear();
    }

    private void reload() {
        getLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
    }

    private void launchDetailActivity(Movie currentMovie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.CURRENT_MOVIE, currentMovie);
        startActivity(intent);
    }

    @Override
    /* This method initialize the contents of the Activity's options menu.
     /** onCreateOptionsMenu() inflates the Options Menu specified in the XML
      when the MainActivity opens up, it actually make it visible.**/
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the Options Menu we specified in XML**/
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is where we can setup the specific action
     * that occurs when any of the items in the Options Menu are selected.
     * <p>
     * This method passes the MenuItem that is selected:
     * <p>
     * public boolean onOptionsItemSelected(MenuItem item)
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* An Options Menu may have one or more items.

          To determine which item was selected and what action to take,
          call getItemId, which returns the unique ID for the menu item
          (defined by the android:id attribute in the menu resource).**/

        int id = item.getItemId();
        /* In our case, our menu only has one item
           (which we setup in main.xml)
          android:id="@+id/action_settings".**/
        if (id == R.id.action_settings) {
            /*  You then match the ID against known menu items to perform the appropriate action.
              In our case, we open the SettingsActivity via an intent.**/
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
