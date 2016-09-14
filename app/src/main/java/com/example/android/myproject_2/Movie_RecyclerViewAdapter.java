package com.example.android.myproject_2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by kkyin on 8/5/2016.
 */
public class Movie_RecyclerViewAdapter extends RecyclerView.Adapter<Movie_RecyclerViewAdapter.Movie_RvVwHldr>
                       // implements Recyc
{

    private static final int VIEW_TYPE_A = 0;

    private Cursor mCursor;
    public static final String LOG_TAG = Movie_RecyclerViewAdapter.class.getSimpleName();

    //---------------------------------------------
    //-------- ViewHolder stuff (begin) -----------
    //---------------------------------------------
    /**
     * Cache of the children views for a movies list item.
     */
    public class Movie_RvVwHldr extends RecyclerView.ViewHolder implements View.OnClickListener{

        //======================
        public final ImageView imageView;
        //======================

        public Movie_RvVwHldr(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            //=============================
            imageView = (ImageView) itemView.findViewById(R.id.frgmntmv_imageview);
//            imageView.setScaleType(ImageView.ScaleType.MATRIX);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //=============================
        }


        // Implement the OnClickListener callback, i.e. onClick(View);
        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "** Movie_RrViewHolder.onClick");
            mOnClickHandler.myOnClick(this); // 'this' refers to this 'ViewHolder'
        }
    }

    //---------------------------------------------
    //-------- ViewHolder stuff (end) -------------
    //---------------------------------------------

    //------------------------------------------------------
    //-------- RecyclerView.Adapter stuff  (begin) ---------
    //------------------------------------------------------
    final private Context mContext;
    final private MvItemOnClickHandler mOnClickHandler; //

    // Declaration of interface
    public static interface MvItemOnClickHandler {
        void myOnClick(Movie_RvVwHldr vh);
    }

    // * provide  a suitable constructor( depends on the kind of data-set / how u want to interface ? )

    public Movie_RecyclerViewAdapter(Context context, MvItemOnClickHandler mvRvAdptr_onClickHandler) {

        // super(context);
        mContext = context;
        mOnClickHandler = mvRvAdptr_onClickHandler;
    }


    // * Called when RecyclerView needs a new 'RecyclerView.ViewHolder' of the given type to represent an item.
    // * Locate new views( invoked by the LayoutManager )
    @Override
    public Movie_RvVwHldr onCreateViewHolder(ViewGroup parent, int viewType) {

        // Log.d(LOG_TAG, "-- Movie_RecyclerViewAdapter/onCreateViewHolder() --");

        //---------------------------
        if (parent instanceof RecyclerView) {

            int layout_id = -1;
            switch (viewType) {
                case VIEW_TYPE_A: {

                 // Log.d(LOG_TAG, "-- Movie_RecyclerViewAdapter/onCreateViewHolder() --");

                    //=================================
                    layout_id = R.layout.cardview_movie;
                    //=================================
                    break;
                }
            }
            // * Create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);

            // * Control whether a view can take focus
            view.setFocusable(true);

            Movie_RvVwHldr movieVwHldr = new Movie_RvVwHldr(view);

            view.setTag(movieVwHldr);

            return movieVwHldr;
        }
        else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    // * To update the RecyclerView.ViewHolder contents with the item at the given position
    // and also sets up some private fields to be used by RecyclerView.
    // * Called by RecyclerView to display the data at the specified position.
    // This method should update the contents of the itemView to reflect the item at the given position.
    // * You should only use the 'position' parameter while acquiring the related data item inside this method
    // * Replace the contents of a view ( invoked by the layout manager )
    //
    // * param holder <-- output/returned ViewHolder-data from method onCreateViewHolder{..} ??!!
    @Override
    public void onBindViewHolder(Movie_RvVwHldr holder, int position) {
        // - get and bind 'that'-element from the data-set at this 'position'
        // - replace the contents of the view with 'that'-element
        // holder.xxx ( data-set(position) ) ;

  ///      Log.d(LOG_TAG, "-- Movie_RecyclerViewAdapter/onBindViewHolder() --");
        // bind the view associated with the 'position'
        //     .into(holder.imageView);
        if (mCursor.moveToPosition(position)==true) {

            Picasso.with(mContext)
                    .load(mCursor.getString(Main_Fragment.COLUMN_POSTERLINK))
                    .placeholder(R.drawable.sample_1)
                    .error(R.drawable.sample_0)
                    .into(holder.imageView);
        }
    }

    // Returns the total number of items in the data set hold by the adapter
    // Invoked by the layout-manager.
    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    // Return the view type of the item at position for the purposes of view recycling.
    // The default implementation of this method returns 0,
    //   making the assumption of a single view type for the adapter.
    // Consider using id resources to uniquely identify item view types.
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void swapCursor(Cursor newCursor) {

        Log.d(LOG_TAG, "** swapCursor() --");

        mCursor = newCursor;

        this.notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        int ColIndex=0;
        if (mCursor != null && mCursor.moveToPosition(position)) {
            ColIndex = mCursor.getColumnIndex("MovieID");
            return mCursor.getLong(ColIndex);
        }
        return ColIndex;
       // return super.getItemId(position);
    }
}
