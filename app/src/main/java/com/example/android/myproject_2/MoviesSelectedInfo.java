package com.example.android.myproject_2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kkyin on 12/10/2015.
 */

public class MoviesSelectedInfo implements Parcelable {

    public static final Parcelable.Creator<MoviesSelectedInfo>
            CREATOR = new Parcelable.Creator<MoviesSelectedInfo>() {

        @Override
        public MoviesSelectedInfo createFromParcel(Parcel in) {
            return new MoviesSelectedInfo(in);
        }

        @Override
        public MoviesSelectedInfo[] newArray(int size) {
            return new MoviesSelectedInfo[size];
        }

    };
    //---------------------------------------

    public String mVideoKey;
    public String mVideoSite;

    public MoviesSelectedInfo(
                       String videoKey,
                       String videoSite
    ) {
        mVideoKey = videoKey;
        mVideoSite= videoSite;
    }
    public MoviesSelectedInfo(Parcel in) {
        this.mVideoKey = in.readString();
        mVideoSite = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mVideoKey);
        dest.writeString(mVideoSite);

    }
    //---------------------------------------
    //---------------------------------------
//    public long mId;
//    public String mOriginalTitle;
//    public String mOverview;
//    public String mVoteAverage;
//    public String mPopularity;
//    public String mReleaseDate;
//    public long mVoteCount;
//    public String mPoster;
//    public String mThumbnail;
//
//    //MoviesSelectedInfo(long id,
//    public MoviesSelectedInfo(long id,
//                       String originalTitle,
//                       String overview,
//                       String voteAverage,
//                       String popularity,
//                       String releaseDate,
//                       long voteCount,
//                       String poster,
//                       String thumbnail
//    ) {
//        mId = id;
//        mOriginalTitle = originalTitle;
//        mOverview = overview;
//        mVoteAverage = voteAverage;
//        mPopularity = popularity;
//        mReleaseDate = releaseDate;
//        mVoteCount = voteCount;
//        mPoster = poster;
//        mThumbnail = thumbnail;
//    }
//    /*
//     + MovieInfoEntry.COL_VOTE_AVERAGE   + " TEXT NOT NULL, " //" REAL NOT NULL, "
//     + MovieInfoEntry.COL_VOTE_COUNT     + " INTEGER NOT NULL, "//" INTEGER NOT NULL, "
//     */
//
//    //---------------------------------------------
//    //protected MoviesSelectedInfo(Parcel in) {
//    public MoviesSelectedInfo(Parcel in) {
//        this.mId = in.readLong();
//        mOriginalTitle = in.readString();
//        mOverview = in.readString();
//        mVoteAverage = in.readString();
//        mPopularity = in.readString();
//        mReleaseDate = in.readString();
//        mVoteCount = in.readLong();
//        mPoster = in.readString();
//        mThumbnail = in.readString();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(mId);
//        dest.writeString(mOriginalTitle);
//        dest.writeString(mOverview);
//        dest.writeString(mVoteAverage);
//        dest.writeString(mPopularity);
//        dest.writeString(mReleaseDate);
//        dest.writeLong(mVoteCount);
//        dest.writeString(mPoster);
//        dest.writeString(mThumbnail);
//
//    }
}



