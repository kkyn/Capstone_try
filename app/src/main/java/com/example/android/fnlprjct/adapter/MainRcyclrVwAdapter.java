package com.example.android.fnlprjct.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.example.android.fnlprjct.DynamicHeightNetworkImageView;
import com.example.android.fnlprjct.ImageLoaderHelper;
import com.example.android.fnlprjct.MainFragment;
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRcyclrVwAdapter extends RecyclerView.Adapter<MainRcyclrVwAdapter.MainRcyclrVwViewHolder>
{
    public static final String LOG_TAG = MainRcyclrVwAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_A = 0;

    private Cursor mCursor;

    //------------------------------------------------------
    //-------- (Begin) ViewHolder stuff --------------------
    //------------------------------------------------------
    /**
     * Cache of the children views for a movies list item.
     */
    public class MainRcyclrVwViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.poster_networkimageview) public DynamicHeightNetworkImageView poster_networkimageview;

        public MainRcyclrVwViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            poster_networkimageview.setOnClickListener(this);
        }

        // Implement the OnClickListener callback, i.e. onClick(View);
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.poster_networkimageview: /*frgmntm_imageview*/
                    clickListener.onClick0(this); // 'this' refers to this 'ViewHolder'
                    break;

                default: break;
            }
        }
    }
    //------------------------------------------------------
    //-------- (End) ViewHolder stuff ----------------------
    //------------------------------------------------------

    //------------------------------------------------------
    //-------- (Begin) RecyclerView.Adapter stuff  ---------
    //------------------------------------------------------
    final private Context mContext;
    final private ItemClickListener clickListener; // "ItemClickHandler0" , "ittmClckHndlr"

    // Declaration of interface listener, i.e. ItemClickListener,
    // for this RecyclerView Adapter, i.e. MainRcyclrVwAdapter
    public interface ItemClickListener {

        void onClick0(MainRcyclrVwViewHolder viewHolder);
    }

    // Provide a suitable constructor( depends on the kind of data-set / how u want to interface ? )
    public MainRcyclrVwAdapter(Context context, ItemClickListener clickListener) { // "itemClickHandler"
        this.mContext = context;
        this.clickListener = clickListener;
    }

    // Called when RecyclerView needs a new 'RecyclerView.ViewHolder' of the given type to represent an item.
    // Locate new views( invoked by the LayoutManager )

    @Override // This is RecyclerView.Adapter's basic method requirement
    public MainRcyclrVwViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {

            int layout_id = -1;

            switch (viewType) {

                case VIEW_TYPE_A: {

                    //=================================
                    layout_id = R.layout.movie_poster; /*cardview_movie*/
                    //=================================
                    break;
                }
            }

            // Create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);

            // Control whether a view can take focus
            view.setFocusable(true);

            MainRcyclrVwViewHolder viewHolder = new MainRcyclrVwViewHolder(view);

            view.setTag(viewHolder);

            return viewHolder;
        }
        else {
            throw new RuntimeException("Not bound to RecyclerViewSelection");
        }
    }

    // * To update the RecyclerView.ViewHolder contents with the item at the given position
    //      and also sets up some private fields to be used by RecyclerView.
    // * Called by RecyclerView to display the data at the specified position.
    //   This method should update the contents of the itemView to reflect the item at the given position.
    // * You should only use the 'position' parameter while acquiring the related data item inside this method
    // * Replace the contents of a view ( invoked by the layout manager )
    //
    // * param holder <-- output/returned ViewHolder-data from method onCreateViewHolder{..} ??!!

    @Override // This is RecyclerView.Adapter's basic method requirement
    public void onBindViewHolder(MainRcyclrVwViewHolder viewHolder, int position) {
        // - get and bind 'that'-element from the data-set at this 'position'
        // - replace the contents of the view with 'that'-element
        // holder.xxx ( data-set(position) ) ;

        // bind the view associated with the 'position'
        //     .into(holder.imageView);


        if (mCursor.moveToPosition(position)) {

            // ++++++++++++++++++++++++++++++++++++++++++
            ImageLoader imageLoader = ImageLoaderHelper.getInstance(mContext).getImageLoader();

            // Get Image-info from ImageLoader-object,
            // with the given image's-Url-id and interface 'pointer'/ (callback).

            String stringUrl = mCursor.getString(MainFragment.COLUMN_POSTERLINK);

            String string2 = mCursor.getString(MainFragment.COLUMN_MOVIE_TITLE);
            viewHolder.poster_networkimageview.setContentDescription(string2);

            // .setImageUrl -- define in ImageView
            // Sets the content of this ImageView to the specified Uri.
            // Use volley's NetworkImageView, 'setImageUrl(url,imageloader)'-method
            // https://github.com/google/volley/blob/master/src/main/java/com/android/volley/toolbox/NetworkImageView.java
            viewHolder.poster_networkimageview.setImageUrl(stringUrl,imageLoader);

            viewHolder.poster_networkimageview.setAspectRatio(0.66f); // 1.5f,
            viewHolder.poster_networkimageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            viewHolder.poster_networkimageview.setScaleType(ImageView.ScaleType.FIT_XY);
            // ++++++++++++++++++++++++++++++++++++++++++
        }
    }

    // Returns the total number of items in the data set hold by the adapter
    // Invoked by the layout-manager.
    @Override // RecyclerView.Adapter basic requirement
    public int getItemCount() {

        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    /*
     Return the view-type of the item at position for the purposes of view recycling.
     The default implementation of this method returns 0,
       making the assumption of a single view type for the adapter.
     Consider using id resources to uniquely identify item view-types.
    */
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    public void swapCursor(Cursor newCursor) {

        mCursor = newCursor;

        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {

        int columnIndex=0;

        if (mCursor != null && mCursor.moveToPosition(position)) {

            columnIndex = mCursor.getColumnIndex(MovieInfoEntry.COL_MOVIE_ID);

            return mCursor.getLong(columnIndex);
        }
        return columnIndex;
       // return super.getItemId(position);
    }

    public String getItemName(int position) {
        int columnIndex=0;
        String string;
        if (mCursor != null && mCursor.moveToPosition(position)) {

            columnIndex = mCursor.getColumnIndex(MovieInfoEntry.COL_ORIGINAL_TITLE);

            return mCursor.getString(columnIndex);
        }

        string = mContext.getResources().getString(R.string.error_no_title_found);
        return string;
    }




}
