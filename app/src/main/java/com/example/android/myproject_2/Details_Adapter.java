package com.example.android.myproject_2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kkyin on 26/10/2016.
 */

public class Details_Adapter extends RecyclerView.Adapter<Details_Adapter.Details_ViewHolder> {

    private static final String LOG_TAG = Details_Adapter.class.getSimpleName();
    private static final int VIEW_TYPE_A = 0;
    private Cursor mCursor;
    private Context context;

    public Details_Adapter(Context context) {
        this.context = context;
    }

    @Override // basic requirement
    public Details_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {
            Log.d(LOG_TAG, "--xxxx--xxxx--xxxx--xxxx--xxxx-- viewType :- " + viewType);
            int layout_id = -1;
            switch (viewType) {
                case VIEW_TYPE_A: {

                    // Log.d(LOG_TAG, "-- Movie_RecyclerViewAdapter/onCreateViewHolder() --");
                    //=================================
                    layout_id = R.layout.cardview_review;
                    //=================================
                    break;
                }
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);

            // * Control whether a view can take focus
            view.setFocusable(true);

            Details_ViewHolder details_ViewHolder = new Details_ViewHolder(view);

            view.setTag(details_ViewHolder);

            return details_ViewHolder;
        }
        else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }

       // return null;
    }

    @Override // basic requirement
    public void onBindViewHolder(Details_ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            holder.Reviewer_tv.setText(mCursor.getString(MovieDetails_Fragment.INDX_1_REVIEWER));
            holder.Review_tv.setText(mCursor.getString(MovieDetails_Fragment.INDX_1_REVIEWCONTENT));
        }
    }


    @Override // basic requirement
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        Log.d(LOG_TAG, "** swapCursor() --");

        mCursor = newCursor;

        this.notifyDataSetChanged();

    }
    //--------------------------------------
    //---- Begin, ViewHolder definition ----
    //--------------------------------------
    public class Details_ViewHolder extends RecyclerView.ViewHolder {

        public final TextView Reviewer_tv;
        public final TextView Review_tv;
        public Details_ViewHolder(View itemView) {
            super(itemView);
            Reviewer_tv = (TextView) itemView.findViewById(R.id.Reviewer_textView);
            Review_tv = (TextView) itemView.findViewById(R.id.Review_textView);
        }
    }

    //--------------------------------------
    //---- End,   ViewHolder definition ----
    //--------------------------------------

}
