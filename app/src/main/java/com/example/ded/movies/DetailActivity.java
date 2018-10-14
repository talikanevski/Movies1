package com.example.ded.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String CURRENT_MOVIE = "current_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else {
            Movie currentMovie = intent.getParcelableExtra(CURRENT_MOVIE);

            /** Find the TextView in the activity_detail.xml layout with title**/
            TextView titleTextView = (TextView) findViewById(R.id.title);

            /** Get the version name from the current Movie object and set this text on the name TextView**/
            // Display the title of the current movie in that TextView
            titleTextView.setText(currentMovie.getTitle());

            /** Find the TextView in the activity_detail.xml layout with overview**/
            TextView overviewTextView = (TextView) findViewById(R.id.overview);
            overviewTextView.setText(currentMovie.getOverview());

            /** Find the TextView in the activity_detail.xml layout with releaseDate**/
            TextView releaseDateTextView = (TextView) findViewById(R.id.release_date);
            releaseDateTextView.setText(currentMovie.getReleaseDate().substring(0, 4));

            /** Find the TextView in the activity_detail.xml_detail.xml layout with userRating**/
            TextView userRatingTextView = (TextView) findViewById(R.id.avg_rating);
            userRatingTextView.setText(currentMovie.getUserRating());

            /** Find the View in the activity_detail.xml_detail.xml layout with the poster of the of the current movie**/
            ImageView posterImage = (ImageView) findViewById(R.id.poster);

            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + currentMovie.getPoster())
                    .into(posterImage);

            /** Find the View in the activity_detailvity_detail.xml layout with the poster of the of the current movie**/
            ImageView backdrop = (ImageView) findViewById(R.id.backdrop);

            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + currentMovie.getPoster())
                    .into(backdrop);
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}

