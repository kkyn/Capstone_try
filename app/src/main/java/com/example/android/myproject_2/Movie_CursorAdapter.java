package com.example.android.myproject_2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * MoviesSelectedInfo
 * Created by kkyin on 29/9/2015.
 */
// tky, to add,
 public class Movie_CursorAdapter extends CursorAdapter {// tky, to add,

    public static final String LOG_TAG = Movie_CursorAdapter.class.getSimpleName();

    // stp(1)--
    // constructor
    public Movie_CursorAdapter(Context context, Cursor c, int flags) {  ////??
        super(context, c, flags);
    }

    // stp(2)--
    // * Make a new-view to hold the data pointed at by cursor.
    //   Remember that these views are reused as needed.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Expand/inflate the targeted view and return an 'id' to the view
        View anImageView = LayoutInflater.from(context).inflate(R.layout.imageview, parent, false);

        return anImageView;
    }

    // stp(3)--
    // Bind an existing view to the data pointed to by cursor.
    // This is where we fill-in the views with the contents of the cursor.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Log.d(LOG_TAG, "---xxx IN Movie_CursorAdapter/bindView() ----");

        //////////////////////// try 28th April/////////////////
        /* */

        ImageView anImageView = (ImageView) view;

        if (view == null) {
            anImageView = new ImageView(context);
            anImageView.setScaleType(ImageView.ScaleType.MATRIX);    // good
            anImageView.setScaleType(ImageView.ScaleType.FIT_XY);    // good
            anImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            anImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Picasso.with(context)
            .load(cursor.getString(Movie_Fragment.COLUMN_POSTERLINK))
                //                .centerCrop()
            .placeholder(com.example.android.myproject_2.R.drawable.sample_1)
            .error(com.example.android.myproject_2.R.drawable.sample_0)
            .into(anImageView);
        /* */
        //++++++++++++++++++++++++++++++++++++++++
//        SquaredImageView1 anImageView = (SquaredImageView1) view;
//        //ImageView anImageView = (ImageView) view;
//        if (view == null) {
//            anImageView = new SquaredImageView1(context);
//            anImageView.setScaleType(ImageView.ScaleType.MATRIX);    // good
//            anImageView.setScaleType(ImageView.ScaleType.FIT_XY);    // good
//        }
//        Picasso.with(context)
//            //.load(mMovImages.mPoster)
//            .load(cursor.getString(Movie_Fragment.COLUMN_POSTERLINK))
//                //                .centerCrop()
//            .placeholder(com.example.android.myproject_2.R.drawable.sample_1)
//            .error(com.example.android.myproject_2.R.drawable.sample_0)
//            .into(anImageView);
        //++++++++++++++++++++++
    }
}

/////////////////--- (old version) Pre implementation of new version ----Begin-//////////////
//public class MoviesArrayAdapter extends ArrayAdapter<MoviesSelectedInfo> {
//
//
//    public MoviesArrayAdapter(Activity context, ArrayList<MoviesSelectedInfo> movImages) {
//        super(context, 0, movImages);
//    }
///*
//    Get a View that displays the data at the specified position in the data set.
//    You can either create a View manually or inflate it from an XML layout file.
//    When the View is inflated, the parent View (GridView, ListView...)
//    will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean)
//    to specify a root view and to prevent attachment to the root.
//    */
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //  return super.getView(position, convertView, parent);
//
//        MoviesSelectedInfo mMovImages = getItem(position);
///* */
//        SquaredImageView view = (SquaredImageView) convertView;
////         View view =  convertView;
//
//        if (view == null) {
////            view = (SquaredImageView) LayoutInflater.from(getContext())
//            ///           view =  LayoutInflater.from(getContext())
//            ///                   .inflate(R.layout.imageview, parent, false);
//            view = new SquaredImageView(getContext());
//            view.setScaleType(ImageView.ScaleType.MATRIX);    // good
//            view.setScaleType(ImageView.ScaleType.FIT_XY);    // good
//            //           view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            //    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            //       view.setScaleType(ImageView.ScaleType.CENTER);
//            //    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            //   view.setScaleType(CENTER_CROP);
//        }
////        SquaredImageView imageView = (SquaredImageView) view.findViewById(R.id.gridview_item);
//        ///    SquaredImageView  imageView =  (SquaredImageView) view.findViewById(R.id.gridview_item);
//        Picasso.with(getContext())
//                .load(mMovImages.mPoster)
//                        //                .centerCrop()
//                .placeholder(com.example.android.myproject_2.R.drawable.sample_1)
//                .error(com.example.android.myproject_2.R.drawable.sample_0)
//                .into(view);
//        //  .into(imageView);
//
//        return view;
///*       */
//
//        /***********************************/
///*
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext())
//                    .inflate(R.layout.imageview, parent, false);
//        }
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.gridview_item);
//
//        Picasso.with(getContext())
//                .load(mMovImages.mPoster)
//        //        .centerCrop()
//                .placeholder(R.drawable.sample_1)
//                .error(R.drawable.sample_0)
//                .into(imageView);
//
//        return convertView;
//  */
//    }
//}

/**
 * An image view which always remains square with respect to its width.
 */
//
//final class SquaredImageView1 extends ImageView {
//    public SquaredImageView1(Context context) {
//        super(context);
//    }
//
//    public SquaredImageView1(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
//    }
//}

/////////////////--- Pre implementation of new version ----End---//////////////