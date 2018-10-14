package com.example.ded.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**  http://www.vogella.com/tutorials/AndroidParcelable/article.html **/
public class Movie implements Parcelable {
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

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mUserRating = in.readString();
        mPoster = in.readString();
        mBackdrop = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {return mTitle;}

    public String getOverview() {return mOverview;}

    public String getReleaseDate() {return mReleaseDate;}

    public String getUserRating() {return mUserRating;}

    public String getPoster() {return mPoster;}

    public String getBackdrop() {return mBackdrop;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mUserRating);
        parcel.writeString(mPoster);
        parcel.writeString(mBackdrop);
    }
}
