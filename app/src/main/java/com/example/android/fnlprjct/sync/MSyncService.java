package com.example.android.fnlprjct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class MSyncService extends Service {

    // Object to use as a thread-safe lock
    // Storage for an instance of the sync adapter
    private static final Object LockSyncAdapter = new Object();
    private static MSyncAdapter mSyncAdapter = null;

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() { // super.onCreate();

        // Create the sync adapter as a singleton.
        // Set the sync adapter as syncable
        // Disallow parallel syncs
        synchronized (LockSyncAdapter) {
            if (mSyncAdapter == null) {
                mSyncAdapter = new MSyncAdapter(getApplicationContext(), true);
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
        return mSyncAdapter.getSyncAdapterBinder();
        //return null;
    }
}
