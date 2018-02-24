package com.example.henly.musicplayer;


import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Henly on 2017/12/22.
 */

public class SplashScreen {
    private Activity activity;
    private Dialog mSplashDialog;
    public final static int SLIDE_LEFT = 1;
    public final static int SLIDE_UP = 2;
    public final static int FADE_OUT = 3;
    public SplashScreen(Activity activity) {
        this.activity = activity;
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

                if (mSplashDialog == null){
                    mSplashDialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
                    mSplashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    Window window = mSplashDialog.getWindow();
                    switch (animation){
                        case SLIDE_LEFT:
                            window.setWindowAnimations(R.style.dialog_anim_slide_left);
                            break;
                        case SLIDE_UP:
                            window.setWindowAnimations(R.style.dialog_anim_slide_up);
                            break;
                        case FADE_OUT:
                            window.setWindowAnimations(R.style.dialog_anim_fade_out);
                            break;
                    }
                    mSplashDialog.setContentView(linearLayout);
                    mSplashDialog.setCancelable(false);
                    mSplashDialog.show();
                }
            }
        };
        activity.runOnUiThread(splashRunnable);
    }

    public void removeSplashScreen() {
        if (mSplashDialog != null && mSplashDialog.isShowing()) {
            Log.i("zhanglh","removeSplashScreen success");
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }
}
