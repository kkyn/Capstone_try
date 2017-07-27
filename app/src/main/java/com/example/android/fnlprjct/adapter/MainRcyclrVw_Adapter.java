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
import com.example.android.fnlprjct.Main_Fragment;
import com.example.android.fnlprjct.R;
import com.example.android.fnlprjct.data.MovieContract.MovieInfoEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by kkyin on 8/5/2016.
 */
public class MainRcyclrVw_Adapter extends RecyclerView.Adapter<MainRcyclrVw_Adapter.MainRcyclrVw_ViewHolder>
{
    public static final String LOG_TAG = MainRcyclrVw_Adapter.class.getSimpleName();

    private static final int VIEW_TYPE_A = 0;

    private Cursor mCursor;

    //------------------------------------------------------
    //-------- (Begin) ViewHolder stuff --------------------
    //------------------------------------------------------
    /**
     * Cache of the children views for a movies list item.
     */
    public class MainRcyclrVw_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.poster_imageview) public DynamicHeightNetworkImageView poster_networkimageview;

        public MainRcyclrVw_ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            poster_networkimageview.setOnClickListener(this);
        }

        // Implement the OnClickListener callback, i.e. onClick(View);
        @Override
        public void onClick(View view) {
            /*Log.d(LOG_TAG, "** Movie_ViewHolder.onClick");*/

            switch (view.getId()){

                case R.id.poster_imageview : /*frgmntm_imageview*/
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
    final private Context context;
    final private ItemClickListener clickListener; // "ItemClickHandler0" , "ittmClckHndlr"

    // Declaration of interface listener, i.e. ItemClickListener,
    // for this RecyclerView Adapter, i.e. MainRcyclrVw_Adapter
    public interface ItemClickListener {

        void onClick0(MainRcyclrVw_ViewHolder viewHolder);
    }

    // Provide a suitable constructor( depends on the kind of data-set / how u want to interface ? )
    public MainRcyclrVw_Adapter(Context context, ItemClickListener clickListener) { // "itemClickHandler"
        this.context = context;
        this.clickListener = clickListener;
    }

    // Called when RecyclerView needs a new 'RecyclerView.ViewHolder' of the given type to represent an item.
    // Locate new views( invoked by the LayoutManager )
    @Override // RecyclerView.Adapter basic requirement
    public MainRcyclrVw_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {

            int layout_id = -1;
            switch (viewType) {

                case VIEW_TYPE_A: {
                    layout_id = R.layout.movie_poster; /*cardview_movie*/
                    break;
                }
            }

            // Create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);

            // Control whether a view can take focus
            view.setFocusable(true);

            MainRcyclrVw_ViewHolder viewHolder = new MainRcyclrVw_ViewHolder(view);

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
    @Override // RecyclerView.Adapter basic requirement
    public void onBindViewHolder(MainRcyclrVw_ViewHolder viewHolder, int position) {
        // - get and bind 'that'-element from the data-set at this 'position'
        // - replace the contents of the view with 'that'-element
        // holder.xxx ( data-set(position) ) ;

        // bind the view associated with the 'position'
        //     .into(holder.imageView);

        //***********************
        if (mCursor.moveToPosition(position)) {

            // ++++++++++++++++++++++++++++++++++++++++++
            ImageLoader imageLoader = ImageLoaderHelper.getInstance(context).getImageLoader();

            // Get Image-info from ImageLoader-object,
            // with the given image's-Url-id and interface 'pointer'/ (cllback).

            String stringUrl = mCursor.getString(Main_Fragment.COLUMN_POSTERLINK);

            String string2 = mCursor.getString(Main_Fragment.COLUMN_MOVIE_TITLE);
            viewHolder.poster_networkimageview.setContentDescription(string2);

            // .setImageUrl -- define in ImageView
            viewHolder.poster_networkimageview.setImageUrl(stringUrl,imageLoader);

            viewHolder.poster_networkimageview.setAspectRatio(0.66f); // 1.5f, // --combo 1 --
            viewHolder.poster_networkimageview.setScaleType(ImageView.ScaleType.FIT_CENTER); // --combo 1 --
            viewHolder.poster_networkimageview.setScaleType(ImageView.ScaleType.FIT_XY); // --combo 1 --
//                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_END);
//                moviethumbnail.setScaleType(ImageView.ScaleType.FIT_START);
            // ++++++++++++++++++++++++++++++++++++++++++
            //-------------------------------------
            // Call Volley's imageloader get-method to get image from the Web
            /*ImageLoader.ImageContainer myImageContainer = imageLoader.get
                (
                    stringUrl,
                    new ImageLoader.ImageListener() {

                        // Callback method
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

                            Bitmap bitmap = imageContainer.getBitmap();

                            //if (bitmap != null) {
                            //    Palette p = Palette.generate(bitmap, 12);
                            //    mMutedColor = p.getDarkMutedColor(0xFF770000); // 0xFF333333
                            //    holder.bar_layout.setBackgroundColor(mMutedColor);
                            //}
                        }
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    }
                );*/
            //***********************
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
}
