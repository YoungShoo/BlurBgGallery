package com.shoo.gallery;

import android.app.Application;
import android.content.Context;

/**
 * Created by Shoo on 16-9-16.
 */
public class GalleryApplication extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        ResourceUtils.initContext(getApplicationContext());
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
