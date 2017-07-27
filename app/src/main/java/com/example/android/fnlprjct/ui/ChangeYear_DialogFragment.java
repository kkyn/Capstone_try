package com.example.android.fnlprjct.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.fnlprjct.Main_Fragment;
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.data.MovieContract;
import com.example.android.fnlprjct.sync.MSyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by kkyin on 2/7/2017.
 */
//  https://developer.android.com/reference/android/app/DialogFragment.html
//  https://developer.android.com/guide/topics/ui/dialogs.html
//  https://developer.android.com/reference/android/app/AlertDialog.Builder.html
//  https://developer.android.com/reference/android/widget/TextView.html

public class ChangeYear_DialogFragment extends DialogFragment
                implements SharedPreferences.OnSharedPreferenceChangeListener
{

    private static final String LOG_TAG = ChangeYear_DialogFragment.class.getSimpleName();

    // Empty constructor required for DialogFragment
    public ChangeYear_DialogFragment(){
    }

    public static ChangeYear_DialogFragment
    newInstance(){
        ChangeYear_DialogFragment cyDialog = new ChangeYear_DialogFragment();
        return cyDialog;
    }

    //----------------------------------------------------------------
    // Start-A-Fragment-In-A-Fragment .... Start Dialog in Fragment
    // Step 2 : ( Return data to 'source'/'target'-fragment )
    private void sendBackResult() {
        Bundle bundle = new Bundle();
        bundle.putString(Main_Fragment.DIALOG_KEY, enterYear_et.getText().toString());

        Intent intent = new Intent().putExtras(bundle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

    }

    @BindView(R.id.year_dialog) EditText enterYear_et;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        /*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);*/
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        SharedPreferences
            sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences
            sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        // +++++++++++ View Inflater Stuff ++++++++++++++
        //LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        //View
            view = inflater.inflate(R.layout.dialog_changeyear, null);

        ButterKnife.bind(this, view);

        // Set a special listener to be called when an action is performed on the text view.
        // Interface definition for a callback to be invoked when an action is performed on the editor.
        enterYear_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            // Called when an action is being performed.
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //v.getText().toString();
                Toast.makeText(getContext(),"--- " + v.getText().toString(), Toast.LENGTH_SHORT ).show();
                Toast.makeText(getContext(),"Clicked search, setOnEditorActionListener", Toast.LENGTH_SHORT).show();

                //return false;
                return true;
            }
        });

        return view;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        // +++++++++++ View Inflater Stuff ++++++++++++++
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        View view = layoutInflater.inflate(R.layout.dialogfragment_changeyear, null);

        ButterKnife.bind(this, view);

        // Set a special listener to be called when an action is performed on the text view.
        // Interface definition for a callback to be invoked when an action is performed on the editor.
        enterYear_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            // Called when an action is being performed.
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //v.getText().toString();
                /*Toast.makeText(getContext(),"--- " + v.getText().toString(), Toast.LENGTH_SHORT ).show();
                Toast.makeText(getContext(),"Clicked search, setOnEditorActionListener", Toast.LENGTH_SHORT).show();*/

                searchMoviesYear();

                //return false;
                return true;
            }
        });


        // +++++++++++ AlertDialog Builder Stuff ++++++++++++++
        dialogBuilder.setView(view);
        dialogBuilder.setTitle(R.string.dialog_title);
        dialogBuilder.setMessage(getString(R.string.dialog_comment));

        dialogBuilder.setPositiveButton(
            getString(R.string.dialog_submit),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    searchMoviesYear();
                    sendBackResult();
                }
            });

        dialogBuilder.setNegativeButton(
            getString(R.string.dialog_cancel), null
        );

        Dialog dialog = dialogBuilder.create();

        // ++++++++++++++++++++++++++++++++++++++++++++++++++

        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    // -------------------------------------------------
    private void searchMoviesYear() {

        String yearKey = getResources().getString(R.string.pref_key_year);
        String year = enterYear_et.getText().toString();

        /*Toast.makeText(getContext(),
            "dialog> setPositiveButton> onClick> searchMoviesYear() > "
                + yearKey + " - " + year, Toast.LENGTH_LONG).show();*/

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(yearKey, year);
        editor.apply();

        dismissAllowingStateLoss(); // ??
        //MSyncAdapter.syncImmediately(getContext());

    }



    //---------------------------------------------------------------------------------------------
    //------ SETTING FOR implementation of SharedPreferences.OnSharedPreferenceChangeListener -----
    //---------------------------------------------------------------------------------------------
    // For SharedPreferences.OnSharedPreferenceChangeListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        /*Toast.makeText(getContext(),"onSharedPreferenceChanged 0", Toast.LENGTH_SHORT).show();*/

        if (key.equals(getResources().getString(R.string.pref_key_year))) {

            /*Toast.makeText(getContext(),"onSharedPreferenceChanged 1", Toast.LENGTH_SHORT).show();*/

            //getLoaderManager().restartLoader(MOVIE_FRAGMENT_ID, null, this);

            getContext().getContentResolver().notifyChange(MovieContract.MovieInfoEntry.CONTENT_URI, null);

            MSyncAdapter.syncImmediately(getContext());
        }
    }
}
