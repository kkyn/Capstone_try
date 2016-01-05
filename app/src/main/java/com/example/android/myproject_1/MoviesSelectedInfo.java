package com.example.android.myproject_1;

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
    long mId;
    String mOriginalTitle;
    String mOverview;
    double mVoteAverage;
    String mReleaseDate;
//    long mVoteCount;
    String mPoster;
    String mThumbnail;

    MoviesSelectedInfo(long id,
                       String originalTitle,
                       String overview,
                       double voteAverage,
                       String releaseDate,
                       //    long voteCount,
                       String poster,
                       String thumbnail
    ) {
        mId = id;
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
    //    mVoteCount = voteCount;
        mPoster = poster;
        mThumbnail = thumbnail;
    }

    //---------------------------------------------
    protected MoviesSelectedInfo(Parcel in) {
        mId = in.readLong();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
    //    mVoteCount = in.readLong();
        mPoster = in.readString();
        mThumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
    //    dest.writeLong(mVoteCount);
        dest.writeString(mPoster);
        dest.writeString(mThumbnail);

    }
}



