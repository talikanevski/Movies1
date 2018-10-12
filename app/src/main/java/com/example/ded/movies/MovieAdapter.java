package com.example.ded.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;


    public MovieAdapter(Activity context, ArrayList<Movie> movies) {
        /**
         the second argument is used when the ArrayAdapter is populating a single TextView.
         Because this is a custom adapter for more then 1 TextView , the adapter is not going to use
         this second argument, so it can be any value. Here, I used 0.
         */
        super(context, 0, movies);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /** Check if the existing view is being reused, otherwise inflate the view**/
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }

        /**Get the Movie object located at this position in the list**/
        final Movie currentMovie = getItem(position);

        /** Find the TextView in the item.xml layout with title**/
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);

        /** Get the version name from the current Movie object and set this text on the name TextView**/
        // Display the title of the current movie in that TextView
        titleTextView.setText(currentMovie.getTitle());

        /** Find the TextView in the item.xml layout with overview**/
        TextView overviewTextView = (TextView) listItemView.findViewById(R.id.overview);
        overviewTextView.setText(currentMovie.getOverview());

        /** Find the TextView in the item.xml layout with releaseDate**/
        TextView releaseDateTextView = (TextView) listItemView.findViewById(R.id.release_date);
        releaseDateTextView.setText(currentMovie.getReleaseDate().substring(0,4));

        /** Find the TextView in the item.xml layout with userRating**/
        TextView userRatingTextView = (TextView) listItemView.findViewById(R.id.avg_rating);
        userRatingTextView.setText(currentMovie.getUserRating());

        /** Find the View in the item.xml layout with the poster of the of the current movie**/
        ImageView posterImage = (ImageView) listItemView.findViewById(R.id.poster);

        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w185"+currentMovie.getPoster())
                .into(posterImage);

        /** Find the View in the item.xml layout with the poster of the of the current movie**/
        ImageView backdrop = (ImageView) listItemView.findViewById(R.id.backdrop);

        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w185"+currentMovie.getPoster())
                .into(backdrop);

        /**Return the list item view that is now showing the appropriate data**/
        return listItemView;
    }
}
