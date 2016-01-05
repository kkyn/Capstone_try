package com.example.android.myproject_1;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * MoviesSelectedInfo
 * Created by kkyin on 29/9/2015.
 */
public class MoviesArrayAdapter extends ArrayAdapter<MoviesSelectedInfo> {

    public MoviesArrayAdapter(Activity context, ArrayList<MoviesSelectedInfo> movImages) {
        super(context, 0, movImages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //  return super.getView(position, convertView, parent);

        MoviesSelectedInfo mMovImages = getItem(position);
/* */
        SquaredImageView view = (SquaredImageView) convertView;
//         View view =  convertView;

        if (view == null) {
//            view = (SquaredImageView) LayoutInflater.from(getContext())
            ///           view =  LayoutInflater.from(getContext())
            ///                   .inflate(R.layout.gridview, parent, false);
            view = new SquaredImageView(getContext());
            view.setScaleType(ImageView.ScaleType.MATRIX);    // good
            view.setScaleType(ImageView.ScaleType.FIT_XY);    // good
            //           view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //    view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //       view.setScaleType(ImageView.ScaleType.CENTER);
            //    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //   view.setScaleType(CENTER_CROP);
        }
//        SquaredImageView mImageView = (SquaredImageView) view.findViewById(R.id.gridview_item);
        ///    SquaredImageView  mImageView =  (SquaredImageView) view.findViewById(R.id.gridview_item);
        Picasso.with(getContext())
                .load(mMovImages.mPoster)
                        //                .centerCrop()
                .placeholder(R.drawable.sample_1)
                .error(R.drawable.sample_0)
                .into(view);
        //  .into(mImageView);

        return view;
/*       */

        /***********************************/
/*
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.gridview, parent, false);
        }

        ImageView mImageView = (ImageView) convertView.findViewById(R.id.gridview_item);

        Picasso.with(getContext())
                .load(mMovImages.mPoster)
        //        .centerCrop()
                .placeholder(R.drawable.sample_1)
                .error(R.drawable.sample_0)
                .into(mImageView);

        return convertView;
  */
    }
}

/**
 * An image view which always remains square with respect to its width.
 */
//
//final class SquaredImageView extends ImageView {
//    public SquaredImageView(Context context) {
//        super(context);
//    }
//
//    public SquaredImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
//    }
//}