package com.shoo.gallery;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by Shoo on 16-9-16.
 */
public class ResourceUtils {

    private static Context sContext;

    private static int sDisplayWidth;
    private static int sDisplayHeight;

    public static void initContext(Context context) {
        sContext = context;
    }

    public static Resources getResources() {
        return sContext.getResources();
    }

    public static Drawable getDrawable(int resId) {
        return sContext.getResources().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return sContext.getResources().getColor(resId);
    }

    public static int getDisplayWidth() {
        if (sDisplayWidth <= 0) {
            sDisplayWidth = sContext.getResources().getDisplayMetrics().widthPixels;
        }
        return sDisplayWidth;
    }

    public static int getDisplayHeight() {
        if (sDisplayHeight <= 0) {
            sDisplayHeight = sContext.getResources().getDisplayMetrics().heightPixels;
        }
        return sDisplayHeight;
    }
}
