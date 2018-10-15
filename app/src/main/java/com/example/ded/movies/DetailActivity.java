package com.example.ded.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

            /* Find the TextView in the activity_detail.xml layout with title**/
            TextView titleTextView = findViewById(R.id.title);

            /* Get the version name from the current Movie object and set this text on the name TextView**/
            // Display the title of the current movie in that TextView
            titleTextView.setText(currentMovie.getTitle());

            /* Find the TextView in the activity_detail.xml layout with overview**/
            TextView overviewTextView = findViewById(R.id.overview);
            overviewTextView.setText(currentMovie.getOverview());

            /* Find the TextView in the activity_detail.xml layout with releaseDate**/
            TextView releaseDateTextView = findViewById(R.id.release_date);
            releaseDateTextView.setText(currentMovie.getReleaseDate().substring(0, 4));

            /* Find the TextView in the activity_detail.xml_detail.xml layout with userRating**/
            TextView userRatingTextView = findViewById(R.id.avg_rating);
            userRatingTextView.setText(currentMovie.getUserRating());

            /* Find the View in the activity_detail.xml_detail.xml layout with the poster of the of the current movie**/
            ImageView posterImage = findViewById(R.id.poster);

            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w185" + currentMovie.getPoster())
                    .into(posterImage);

            /* Find the View in the activity_detail.xml layout with the poster of the of the current movie**/
            ImageView backdrop = findViewById(R.id.backdrop);

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

