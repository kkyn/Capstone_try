package com.example.android.myproject_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * Created by kkyin on 1/7/2016.
 */
public class Utility {
    public static String LOG_TAG = Utility.class.getSimpleName();

    public static String getPreferredSortSequence(Context context) {
    //public static String getPreferredSortSequence(Context context) {

        // get the file, SharedPreferences
        // Gets a SharedPreferences instance that points to the default file
        // that is used by the preference framework in the given context.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Retrieve a String value from the preferences.
        // getString(String key, String defValue)

        String mstring = context.getString(R.string.pref_sortmovies_key);
        String mstringDefault = context.getString(R.string.pref_sortmovies_default_value);
    //    String mstring = String.valueOf((R.string.pref_sortmovies_key));

        //++++++++++++++++++++++++++++++++++++++++

        String string = sharedPreferences.getString(mstring, mstringDefault);
        //String string = sharedPreferences.getString("Movies", "popularity.desc");

        Log.d(LOG_TAG, "1111 mstring: " + mstring + " -- mstringDefault: " + mstringDefault);
        Log.d(LOG_TAG, "1111 string : " + string);

        return string;
        //++++++++++++++++++++++++++++++++++++++++
//        Log.d(LOG_TAG, "1111 mstring :" + mstring + " -- mstringDefault :" + mstringDefault);
//        return sharedPreferences.getString(mstring, mstringDefault);
        //++++++++++++++++++++++++++++++++++++++++
    }

//    public static void xxxx(){
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//    }


}
