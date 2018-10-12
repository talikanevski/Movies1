package com.example.ded.movies;

public class Movie {
    private String mTitle; /** title of the movie  */
    private String mOverview ;/**movie overview */
    private String mReleaseDate; /**release date of the movie */
    private String mUserRating; /*** user rating */
    private String mPoster;/** movie poster*/
    private String mBackdrop; /** backdrop**/

    public Movie(String title, String overview, String releaseDate, String userRating, String poster, String backdrop) {
        mTitle = title;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mUserRating = userRating;
        mPoster = poster;
        mBackdrop = backdrop;
    }

    public String getTitle() {return mTitle;}

    public String getOverview() {return mOverview;}

    public String getReleaseDate() {return mReleaseDate;}

    public String getUserRating() {return mUserRating;}

    public String getPoster() {return mPoster;}

    public String getBackdrop() {return mBackdrop;}

}
