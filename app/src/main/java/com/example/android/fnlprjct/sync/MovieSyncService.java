package com.example.android.fnlprjct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by kkyin on 13/7/2016.
 */
/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class MovieSyncService extends Service {

    // Object to use as a thread-safe lock
    // Storage for an instance of the sync adapter
    private static final Object sSyncAdapterLock = new Object();
    private static MoviesSyncAdapter sMoviesSyncAdapter = null;

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
       // super.onCreate();
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sMoviesSyncAdapter == null) {
                sMoviesSyncAdapter = new MoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /*
     * Return an object that allows the system to invoke
     * the sync adapter.
     */
    //@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sMoviesSyncAdapter.getSyncAdapterBinder();
        //return null;
    }
}
