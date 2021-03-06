package com.example.ded.movies;

import android.text.TextUtils;
import android.util.Log;

import com.example.ded.movies.Movie;

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

import static com.example.ded.movies.MovieLoader.LOG_TAG;


/**
 * Helper methods related to requesting and receiving movies from "The Movie DB".
 */
final class QueryUtils {

    private QueryUtils() {
    }

    private static List<Movie> extractFeatureFromJson(String jsonResponse) {
        String title;
        String overview;
        String releaseDate;
        String userRating;
        String thumbnail;
        String backdrop;

        /*If the JSON string is empty or null, then return early.**/
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        /*Create an empty ArrayList that we can start adding movies to**/
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);
                title = result.getString("title");
                overview = result.getString("overview");
                releaseDate = result.getString("release_date");
                userRating = result.getString("vote_average");
                thumbnail = result.getString("poster_path");
                backdrop = result.getString("backdrop_path");
                Movie movie = new Movie(title, overview, releaseDate, userRating, thumbnail, backdrop);
                movies.add(movie);
            }

        } catch (JSONException e) {
            /* an error is thrown when executing any of the above statements in the "try" block,
             // catch the exception here, so the app doesn't crash. Print a log message
             // with the message from the exception.**/
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        /* Return the list of movies  **/
        return movies;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        /* If the URL is null, then return early. **/
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /*If the request was successful (response code 200 or urlConnection.HTTP_OK),
             // then read the input stream and parse the response. **/
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                /* Closing the input stream could throw an IOException, which is why
                 // the makeHttpRequest(URL url) method signature specifies than an IOException
                 // could be thrown. **/
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /* the fetchNewsData() helper method that ties all the steps together -
      creating a URL, sending the request, processing the response.
      Since this is the only “public” QueryUtils method that the MovieAsyncTask needs to interact with,
      make all other helper methods in QueryUtils “private”.**/

    /**
     * Query the The MovieDB data set and return a list of Movie objects.
     */
    public static List<Movie> fetchNewsData(String requestUrl) {

        Log.i(LOG_TAG, "Test:  fetchNewsData called");

//
//        /** To force the background thread to sleep for 2 seconds,
//         * we are temporarily simulating a very slow network response time.
//         * We are “pretending” that it took a long time to fetch the response.
//         * That allows us to see the loading spinner on the screen for a little longer
//         * than it normally would appear for.**/
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        /* Create URL object **/
        URL url = createUrl(requestUrl);

        /* Perform HTTP request to the URL and receive a JSON response back **/
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        /* Extract relevant fields from the JSON response and create a list of Movies
          Return the list of movies **/

        return extractFeatureFromJson(jsonResponse);
    }
}
