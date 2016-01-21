package com.example.android.myproject_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by kkyin on 11/11/2015.
 */
public class SquaredImageView extends ImageView {

    public SquaredImageView(Context context) {
        super(context);
    }
    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
