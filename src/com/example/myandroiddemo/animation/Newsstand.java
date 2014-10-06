package com.example.myandroiddemo.animation;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myandroiddemo.R;
import com.example.myandroiddemo.utils.AndUtils;

public class Newsstand extends Activity {
	private static final String TAG = "Newsstand";
	private View mNavContainer;
	private DrawerLayout mDrawerLayout;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_newsstand_layout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		mNavContainer = findViewById(R.id.container_nav);

		mActionBar = getActionBar();

		if (null != mActionBar) {
			mActionBar.setBackgroundDrawable(null);
			mActionBar.setDisplayHomeAsUpEnabled(true);
		}

		setDrawerOpen(false);
		NewsstandFragment.show(getFragmentManager(), AndUtils.getInstance(this)
				.getActionBarView(this));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void setDrawerOpen(boolean open) {
		if (open) {
			mDrawerLayout.openDrawer(mNavContainer);
		} else {
			mDrawerLayout.closeDrawer(mNavContainer);
		}
	}

	private boolean isDrawerOpen() {
		return mDrawerLayout.isDrawerOpen(mNavContainer);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		NewsstandFragment.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

}
