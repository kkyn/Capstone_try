package com.example.android.fnlprjct.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.fnlprjct.Detail_Fragment;
import com.example.android.fnlprjct.R;

/**
 * Created by kkyin on 26/10/2016.
 */

public class DetailRcyclrVw_Adapter extends RecyclerView.Adapter<DetailRcyclrVw_Adapter.Details_ViewHolder> {

    private static final String LOG_TAG = DetailRcyclrVw_Adapter.class.getSimpleName();
    private static final int VIEW_TYPE_A = 0;
    private Cursor mCursor;
    private Context context;

    public DetailRcyclrVw_Adapter(Context context) {
        this.context = context;
    }

    // basic requirement
    @Override
    public Details_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {

            /*Log.d(LOG_TAG, "--xxxx--xxxx--xxxx--xxxx--xxxx-- viewType :- " + viewType);*/
            int layout_id = -1;
            switch (viewType) {
                case VIEW_TYPE_A: {

                    // Log.d(LOG_TAG, "-- MainRcyclrVw_Adapter/onCreateViewHolder() --");
                    //=================================
                    layout_id = R.layout.cardview_review;
                    //=================================
                    break;
                }
            }
            View view = LayoutInflater.from(context).inflate(layout_id, parent, false);

            // Control whether a view can take focus
            view.setFocusable(true);

            Details_ViewHolder details_ViewHolder = new Details_ViewHolder(view);

            view.setTag(details_ViewHolder);

            return details_ViewHolder;
        }
        else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    @Override // basic requirement
    public void onBindViewHolder(Details_ViewHolder holder, int position) {

        if (mCursor.moveToPosition(position)) {

            holder.Reviewer_tv.setText(mCursor.getString(Detail_Fragment.INDX_1_REVIEWER));
            holder.Review_tv.setText(mCursor.getString(Detail_Fragment.INDX_1_REVIEWCONTENT));
        }
    }


    @Override // basic requirement
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        /*Log.d(LOG_TAG, "** swapCursor() --");*/

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
