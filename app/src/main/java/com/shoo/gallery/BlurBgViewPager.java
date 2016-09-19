package com.shoo.gallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Shoo on 16-9-16.
 */
public class BlurBgViewPager extends ViewPager {

    private BlurBgDelegate mBlurBgDelegate;

    public BlurBgViewPager(Context context) {
        super(context);
    }

    public BlurBgViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBlurBackground(Drawable background) {
        setBlurBackground(background, 0);
    }

    public void setBlurBackground(Drawable background, int position) {
        if (mBlurBgDelegate == null) {
            mBlurBgDelegate = new BlurBgDelegate(this);
        }
        mBlurBgDelegate.setBlurBackground(background, position);
    }
}
