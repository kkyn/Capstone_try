package com.example.android.fnlprjct;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

public class DynamicHeightNetworkImageView extends NetworkImageView {

    private float mAspectRatio = 1.5f;
    private static String LTAG = "IN_DYNAMIC";

    public DynamicHeightNetworkImageView(Context context) {
        super(context);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {

        mAspectRatio = aspectRatio;

        //--------------------------------------
        // Call this when something has changed which has invalidated the layout of this view.
        // https://developer.android.com/reference/android/view/View.html
        // * https://developer.android.com/reference/android/view/View.html#requestLayout()
        //--------------------------------------
        requestLayout();
    }

    //--------------------------------------
    // * https://developer.android.com/reference/android/view/View.html#measure(int, int)
    // * https://developer.android.com/reference/android/view/View.html#onMeasure(int, int)
    // * Called to determine the size requirements for this view and all of its children.
    // * Only onMeasure(int, int) can and must be overridden by subclasses.
    // * When overriding this method, you must call setMeasuredDimension(int, int)
    // to store the measured width and height of this view.
    //--------------------------------------
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //--------------------------------------
        // https://developer.android.com/reference/android/view/View.html
        //--------------------------------------

        int measuredWidth = getMeasuredWidth();

        //--------------------------------------
        // https://developer.android.com/reference/android/view/View.html
        // This method must be called by onMeasure(int, int) to store the measured width and measured height.
        // MUST CALL METHOD INSIDE 'onMeasure(....)'
        //--------------------------------------

        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }
}
