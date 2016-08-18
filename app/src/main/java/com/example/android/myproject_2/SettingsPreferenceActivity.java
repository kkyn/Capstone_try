package com.example.android.myproject_2;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.android.myproject_2.data.MovieContract;
import com.example.android.myproject_2.sync.MoviesSyncAdapter;
import static com.example.android.myproject_2.Movie_Fragment.*;
//import static com.example.android.myproject_2.Movie_Fragment.*;
//==============================================
//==============================================
/**
 * A {@link PreferenceActivity} that presents a set of application settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsPreferenceActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener,
             SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String LOG_TAG = SettingsPreferenceActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

///        Toast.makeText(getApplicationContext(),"++ In SettingsPreference Activity ----", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"ssss onCreate ----");

        // - Add 'general' preferences, defined in the XML file
        // - Load the preferences from an XML resource
        // TODO: Add preferences from XML
        addPreferencesFromResource(R.xml.pref_general);

        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        // TODO: Add preferences
        // bindPreferenceSummaryToValue(findPreference(getString(com.example.android.myproject_2.R.string.pref_sortmovies_by_key)));

        // Finds a Preference based on its key.
        // Preference mPreference = this.findPreference("Movies"); // -- or --
        Preference mPreference = this.findPreference(getString(R.string.pref_sortmovies_key));
        bindPreferenceSummaryToValue(mPreference);
    }

    //----------------------------------------------------------------------------------------
    //---------++ SETTING FOR implementation of Preference.OnPreferenceChangeListener --------
    //----------------------------------------------------------------------------------------
    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {

        // Set the listener to watch for value changes.
        // Sets the callback to be invoked when this 'preference' is changed by the user
        // (but before the internal state has been updated).
        // 'this' refers to 'Preference.OnPreferenceChangeListener'
        preference.setOnPreferenceChangeListener(this);

        //++++++++++++++++++++++
        Object object = PreferenceManager
                // Gets a SharedPreferences instance that points to the default file
                // that is used by the preference framework in the given context.
                // 'preference.getContext()' ++ get the context of this preference
                .getDefaultSharedPreferences(preference.getContext())

                // Retrieve a String value from the preferences.
                .getString(preference.getKey(), "");

        Log.d(LOG_TAG, "ssss ---- object : " + object);

        // Set the preference summaries
        setPreferenceSummary (preference, object);
        //++++++++++++++++++++++
        /*
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Object object = sharedPreferences.getString(preference.getKey(), "");

        setPreferenceSummary(preference, object);
        */
        //++++++++++++++++++++++
    }

    private void setPreferenceSummary(Preference preference, Object value) {

        String mKey = preference.getKey();
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {

            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;

            int index = listPreference.findIndexOfValue(stringValue);
            if (index >= 0) {
                CharSequence charSequence[] = listPreference.getEntries();
                preference.setSummary(charSequence[index]);
                // -- or --
                //preference.setSummary(listPreference.getEntries()[index]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
    }

    //
    // implementation of interface Preference.OnPreferenceChangeListener
    //
    // This is called 'BEFORE' the preference is changed.
    // Called when a Preference has been changed by the user.
    // This is called before the state of the Preference is about to be updated and before the state is persisted.
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        setPreferenceSummary(preference, value);

        return true;
    }

    /////////////////////////////////////////////////////////////////////
    // Registers a shared preference change listener that gets notified when preferences change
    @Override
    protected void onResume() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    // Unregisters a shared preference change listener
    @Override
    protected void onPause() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    //---------------------------------------------------------------------------------------------
    //----++ SETTING FOR implementation of SharedPreferences.OnSharedPreferenceChangeListener -----
    //---------------------------------------------------------------------------------------------
    // Implementation of interface SharedPreferences.OnSharedPreferenceChangeListener.
    // This gets called after the preference is changed, which is important because we
    // start our synchronization here

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if ( key.equals(getString(R.string.pref_sortmovies_key)) ) {
            // we've changed the location
            // first clear locationStatus
        //    Utility.resetLocationStatus(this);

            String mString = Utility.getPreferredSortSequence(this);

            Log.d(LOG_TAG, "ssss onSharedPreferenceChanged ..... key: " + key + " ..... SortSeq: " + mString);
///            Toast.makeText(getApplicationContext(),"++ SettingsPreference Activity / onSharedPreferenceChanged ----", Toast.LENGTH_SHORT).show();

            // THIS 2 LINES WORKS !! NOT SURE ???
//            getContentResolver().notifyChange(MovieContract.PopularEntry.CONTENT_URI, null);
//            getContentResolver().notifyChange(MovieContract.RatingEntry.CONTENT_URI, null);
        //+++++++++++++++++++++++++++++++++++++++++++

            // tky add, 4th August, 2016, 11.44pm --- DON"T SEEM TO WORK !!
			if (mString.equals(getString(R.string.pref_sortmovies_default_value))){
			        getContentResolver().notifyChange(MovieContract.PopularEntry.CONTENT_URI, null);
                Log.d(LOG_TAG, "ssss onSharedPreferenceChanged ..... notifyChange: " + mString + " .... " + MovieContract.PopularEntry.CONTENT_URI.toString());
            }
			else if (mString.equals(getString(R.string.pref_sortmovies_by_ratings))){
			        getContentResolver().notifyChange(MovieContract.RatingEntry.CONTENT_URI, null);
                Log.d(LOG_TAG, "ssss onSharedPreferenceChanged ..... notifyChange: " + mString + " .... " + MovieContract.RatingEntry.CONTENT_URI.toString());
			}
        //++++++++++++++++++++++++++++++++++++++++

            Log.d(LOG_TAG, "ssss onSharedPreferenceChanged ..... MoviesSyncAdapter.syncImmediately(this) --");

           // Movie_Fragment.myRestartLoaderCode();
            MoviesSyncAdapter.syncImmediately(this);

        }
        /*
        else if ( key.equals(getString(R.string.pref_units_key)) ) {
            // units have changed. update lists of weather entries accordingly
            getContentResolver().notifyChange(MovieContract.PopularEntry.CONTENT_URI, null);

        } else if ( key.equals(getString(R.string.pref_location_status_key)) ) {
            // our location status has changed.  Update the summary accordingly
            Preference locationPreference = findPreference(getString(R.string.pref_location_key));

            bindPreferenceSummaryToValue(locationPreference);

        } else if ( key.equals(getString(R.string.pref_art_pack_key)) ) {
            // art pack have changed. update lists of weather entries accordingly
            getContentResolver().notifyChange(MovieContract.PopularEntry.CONTENT_URI, null);
        */

    }
}

