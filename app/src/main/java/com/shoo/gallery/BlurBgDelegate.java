package com.shoo.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.ViewPager;

/**
 * Created by Shoo on 16-9-16.
 */
public class BlurBgDelegate {

    public static final int LAYER_ID_NEW_DRAWABLE = 0;
    public static final int LAYER_ID_OLD_DRAWABLE = 1;

    private final ViewPager mViewPager;

    private LayerDrawable mLayerDrawable;
    private ColorDrawable mBlackDrawable;
    private ColorDrawable mTransDrawable;

    private Drawable mLastOriginDrawable;
    private Drawable mLastBlurDrawable;

    private AnimatorSet mToggleAnimatorSet;

    public BlurBgDelegate(ViewPager viewPager) {
        mViewPager = viewPager;
    }

    public void setBlurBackground(Drawable originDrawable, int position) {
        // 初始化背景为空的处理，以免未对背景进行任何处理
        if (originDrawable == null) {
            originDrawable = mBlackDrawable;
        }

        // 相同背景，不需要做任何处理
        if (originDrawable == mLastOriginDrawable) {
            return;
        }
        mLastOriginDrawable = originDrawable;

        Drawable blurDrawable;
        if (originDrawable != mBlackDrawable) {
            // 创建模糊背景
            blurDrawable = createBlurDrawable(originDrawable);
        } else {
            // 背景为空，则设置黑色背景
            blurDrawable = mBlackDrawable;
        }

        // 初始化层级背景
        ensureLayerDrawable();
        mLayerDrawable.setDrawableByLayerId(LAYER_ID_NEW_DRAWABLE, blurDrawable);
        mLayerDrawable.setDrawableByLayerId(LAYER_ID_OLD_DRAWABLE, mLastBlurDrawable);
        mLastBlurDrawable = blurDrawable;

        // 设置背景
        if (position == mViewPager.getCurrentItem()) {
            mViewPager.setBackground(mLayerDrawable);
        }

        // 执行新旧背景层渐隐渐现动画
        startToggleAnimation();
    }

    /**
     * 初始化层级背景，自上而下：遮罩层，旧背景层，新背景层
     */
    private void ensureLayerDrawable() {
        if (mLayerDrawable == null) {
            // 黑色遮罩层
            Drawable maskDrawable = ResourceUtils.getDrawable(android.R.color.black);
            maskDrawable.setAlpha((int) (0.7f * 255));
            // 透明背景层
            mTransDrawable = new ColorDrawable(Color.TRANSPARENT);
            mLastBlurDrawable = mTransDrawable;
            // 黑色背景层
            mBlackDrawable = new ColorDrawable(Color.BLACK);
            // 创建层级背景
            Drawable[] layers = {mBlackDrawable, mTransDrawable, maskDrawable};
            mLayerDrawable = new LayerDrawable(layers);
            // 根据下标设置ID
            for (int i = 0; i < layers.length; i++) {
                mLayerDrawable.setId(i, i);
            }
        }
    }

    /**
     * 生成模糊背景
     *
     * @param originDrawable
     * @return
     */
    private Drawable createBlurDrawable(Drawable originDrawable) {
        return BlurUtils.createBlurDrawable(originDrawable);
    }

    /**
     * 执行新旧背景层渐隐渐现动画
     */
    private void startToggleAnimation() {
        if (mToggleAnimatorSet != null && mToggleAnimatorSet.isRunning()) {
            mToggleAnimatorSet.cancel();
        }

        // 初始化动画集
        ensureAnimatorSet();
        // 新背景层Alpha渐现动画
        Drawable newDrawable = mLayerDrawable.getDrawable(LAYER_ID_NEW_DRAWABLE);
        ObjectAnimator newDrAlphaAnim = ObjectAnimator.ofInt(newDrawable, "alpha", 0, 255);
        // 旧背景层Alpha渐隐动画
        Drawable oldDrawable = mLayerDrawable.getDrawable(LAYER_ID_OLD_DRAWABLE);
        ObjectAnimator oldDrAlphaAnim = ObjectAnimator.ofInt(oldDrawable, "alpha", oldDrawable.getAlpha(), 0);
        // 执行动画
        mToggleAnimatorSet.playTogether(oldDrAlphaAnim, newDrAlphaAnim);
        mToggleAnimatorSet.start();
    }

    /**
     * 初始化动画集
     */
    private void ensureAnimatorSet() {
        if (mToggleAnimatorSet == null) {
            mToggleAnimatorSet = new AnimatorSet();
            mToggleAnimatorSet.setDuration(500);
            mToggleAnimatorSet.addListener(new AnimListener());
        }
    }

    private class AnimListener extends AnimatorListenerAdapter {

        private boolean mIsCanceled;

        @Override
        public void onAnimationStart(Animator animation) {
            mIsCanceled = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mIsCanceled = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!mIsCanceled) {
                // 渐变动画完成后，重置旧背景层为透明背景层，以减少过度绘制
                mLayerDrawable.setDrawableByLayerId(LAYER_ID_OLD_DRAWABLE, mTransDrawable);
            }
        }
    }

}
