package com.example.henly.musicplayer;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Henly on 2017/12/22.
 */

public class SplashScreen {
    private Activity activity;
    public SplashScreen(Activity activity) {
        activity = activity;
    }

    public void showSplash (final int imageRes, final int animation) {
        Runnable splashRunnable = new Runnable() {
            @Override
            public void run() {
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setMinimumHeight(dm.heightPixels);
                linearLayout.setMinimumWidth(dm.widthPixels);
                linearLayout.setBackgroundResource(imageRes);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,0.0F));

            }
        };
    }
}
