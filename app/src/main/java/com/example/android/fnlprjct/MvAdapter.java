package com.example.android.fnlprjct;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.example.android.fnlprjct.data.MovieContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by kkyin on 8/5/2016.
 */
public class MvAdapter extends RecyclerView.Adapter<MvAdapter.MvViewHolder>
{
    private static final int VIEW_TYPE_A = 0;

    private Cursor mCursor;
    public static final String LOG_TAG = MvAdapter.class.getSimpleName();

    //------------------------------------------------------
    //-------- ViewHolder stuff (begin) --------------------
    //------------------------------------------------------
    /**
     * Cache of the children views for a movies list item.
     */
    public class MvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.poster_imageview)
        DynamicHeightNetworkImageView poster_imageview;

        public MvViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            poster_imageview.setOnClickListener(this);
        }

        // Implement the OnClickListener callback, i.e. onClick(View);
        @Override
        public void onClick(View view) {
            Log.d(LOG_TAG, "** Movie_ViewHolder.onClick");

            switch (view.getId()){
                case R.id.poster_imageview : /*frgmntm_imageview*/
                    onItemClickHandler_0.onItemClick_0(this); // 'this' refers to this 'ViewHolder'
                    break;
                default: break;
            }
        }
    }
    //------------------------------------------------------
    //-------- ViewHolder stuff (end) ----------------------
    //------------------------------------------------------

    //------------------------------------------------------
    //-------- RecyclerView.Adapter stuff  (begin) ---------
    //------------------------------------------------------
    final private Context context;
    final private OnItemClickHandler_0 onItemClickHandler_0; //

    // Declaration of interface
    public interface OnItemClickHandler_0 {
        void onItemClick_0(MvViewHolder viewHolder);
    }

    // * provide  a suitable constructor( depends on the kind of data-set / how u want to interface ? )
    public MvAdapter(Context context, OnItemClickHandler_0 ref_onItemClickHandler_0) {
        // super(context);
        this.context = context;
        this.onItemClickHandler_0 = ref_onItemClickHandler_0;
    }

    // * Called when RecyclerView needs a new 'RecyclerView.ViewHolder' of the given type to represent an item.
    // * Locate new views( invoked by the LayoutManager )
    @Override // RecyclerView.Adapter basic requirement
    public MvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {

            int layout_id = -1;
            switch (viewType) {
                case VIEW_TYPE_A: {
                    layout_id = R.layout.movie_poster; /*cardview_movie*/
                    break;
                }
            }
            // * Create a new view
            View view = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);

            view.setFocusable(true);    // * Control whether a view can take focus

            MvViewHolder mvViewHolder = new MvViewHolder(view);

            view.setTag(mvViewHolder);

            return mvViewHolder;
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
    @Override // RecyclerView.Adapter basic requirement
    public void onBindViewHolder(MvViewHolder holder, int position) {
        // - get and bind 'that'-element from the data-set at this 'position'
        // - replace the contents of the view with 'that'-element
        // holder.xxx ( data-set(position) ) ;

        // bind the view associated with the 'position'
        //     .into(holder.imageView);

        //***********************
        if (mCursor.moveToPosition(position)) {

            ImageLoader imageLoader = ImageLoaderHelper.getInstance(context).getImageLoader();

            // Get Image-info from ImageLoader-object,
            // with the given image's-Url-id and interface 'pointer'/ (cllback).

            String stringUrl = mCursor.getString(Main_Fragment.COLUMN_POSTERLINK);

            // -- thumb-nail-View --
            // .setImageUrl -- define in ImageView
            holder.poster_imageview.setImageUrl(stringUrl,imageLoader);
            holder.poster_imageview.setAspectRatio(0.66f);

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
     Return the view type of the item at position for the purposes of view recycling.
     The default implementation of this method returns 0,
       making the assumption of a single view type for the adapter.
     Consider using id resources to uniquely identify item view types.
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

           // columnIndex = mCursor.getColumnIndex("MovieID");
            columnIndex = mCursor.getColumnIndex(MovieContract.MovieInfoEntry.COL_MV_ID);

            return mCursor.getLong(columnIndex);
        }
        return columnIndex;
       // return super.getItemId(position);
    }
}
