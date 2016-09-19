package com.shoo.gallery;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shoo.gallery.blurbg.R;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置背景为空
        getWindow().setBackgroundDrawable(null);

        BlurBgViewPager viewPager = (BlurBgViewPager) findViewById(R.id.blur_bg_view_pager);
        new ToggleHandler(viewPager).sendEmptyMessage(0);
    }

    private static class ToggleHandler extends Handler {

        private final WeakReference<BlurBgViewPager> mViewPagerRef;
        private final Drawable[] mDrawables;

        public ToggleHandler(BlurBgViewPager viewPager) {
            mViewPagerRef = new WeakReference<>(viewPager);
            mDrawables = new Drawable[] {
                    ResourceUtils.getDrawable(R.drawable.mountain),
                    ResourceUtils.getDrawable(R.drawable.bridge),
                    ResourceUtils.getDrawable(R.drawable.house)
            };
        }

        @Override
        public void handleMessage(Message msg) {
            BlurBgViewPager viewPager = mViewPagerRef.get();
            if (viewPager != null) {
                int index = (int) (Math.random() * 10) % mDrawables.length;
                viewPager.setBlurBackground(mDrawables[index]);
            }
            sendEmptyMessageDelayed(0, 2000);
        }
    }
}
