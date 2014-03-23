package com.example.myandroiddemo.drawable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.myandroiddemo.R;

public class ActionBarColorTest2 extends Activity {
	private static final String TAG = "ActionBarColorTest2";
	private final Handler mHandler = new Handler();
    private ColorAnimationDrawable mActionBarBackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_bar_color_test_layout);
		// action bar background
		setupActionBarBackground();

		findViewById(R.id.trans_action_bar).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(mActionBarBackground != null){
							mActionBarBackground.stop();
							mActionBarBackground.setAlpha(0xff, 0);
							mActionBarBackground.start();
						}
					}
				});
		findViewById(R.id.detrans_action_bar).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(mActionBarBackground != null){
							mActionBarBackground.stop();
							mActionBarBackground.setAlpha(0, 0xff);
							mActionBarBackground.start();
						}
					}
				});
	}

	/**
	 * This must be called in onCreate, onStart or onResume.
	 */
	private void setupActionBarBackground(){
		mActionBarBackground = new ColorAnimationDrawable(Color.BLUE, 0xff);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackground.setCallback(mDrawableCallback);
        } else {
            getActionBar().setBackgroundDrawable(mActionBarBackground);
        }
        mActionBarBackground.start();
	}

	private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            mHandler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            mHandler.removeCallbacks(what);
        }
    };
}
