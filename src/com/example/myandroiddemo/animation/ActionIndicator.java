package com.example.myandroiddemo.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * A class to control a overlay window for an specific action.
 * 
 * @author WenpingWang
 * @date 2014/02/14
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class ActionIndicator {
	private Context mContext = null;
	private int mHeight;
	private ViewGroup mContainer = null;
	private View mIndicatorView = null;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowLayoutParams;
	private boolean windowAdded = false;
	private boolean viewAdded = true;
	private LayoutTransition mLayoutTransitioner = null;
	private long mAppearingDuration = 0;
	private long mDisappearingDuration = 0;

	/**
	 * Constructor
	 * 
	 * @param c
	 * @param startX
	 * @param startY
	 * @param width
	 * @param height
	 * @param layoutId
	 * @param containerId
	 * @param indicatorViewId
	 */
	public ActionIndicator(Context c, int startX, int startY, int width,
			int height, int layoutId, int indicatorViewId) {
		mContext = c;
		mHeight = height;
		// Check arguments
		if (mContext == null) {
			throw new IllegalArgumentException(
					"ActionBarRefreshIndicator constructer context is null");
		}
		mContainer = (ViewGroup) LayoutInflater.from(c).inflate(layoutId, null);
		if (mContainer == null) {
			throw new IllegalArgumentException(
					"ActionBarRefreshIndicator constructer indicatorLayout is invalid");
		}
		mIndicatorView = mContainer.findViewById(indicatorViewId);
		if (mIndicatorView == null) {
			throw new IllegalArgumentException(
					"ActionBarRefreshIndicator constructer indicatorViewId is invalid");
		}
		mIndicatorView.setVisibility(View.GONE);

		mWindowManager = (WindowManager) c.getSystemService(c.WINDOW_SERVICE);
		mWindowLayoutParams = new LayoutParams(width, height,
				LayoutParams.TYPE_SYSTEM_OVERLAY,
				LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGB_888);
		mWindowLayoutParams.gravity = Gravity.TOP;
		mWindowLayoutParams.x = startX;
		mWindowLayoutParams.y = startY;
		mLayoutTransitioner = new LayoutTransition();
		mContainer.setLayoutTransition(mLayoutTransitioner);
		createCustomAnimations(mLayoutTransitioner);
	}

	/**
	 * Get window layout params
	 * 
	 * @return
	 */
	public LayoutParams getLayoutParams() {
		return mWindowLayoutParams;
	}

	/**
	 * Set window layout params
	 * 
	 * @param params
	 */
	public void setLayoutParms(LayoutParams params) {
		mWindowLayoutParams = params;
	}

	/**
	 * Set appearing animator.
	 * 
	 * @param animator
	 */
	public void setAnimation(int transitionType, ObjectAnimator animator) {
		mLayoutTransitioner.setAnimator(transitionType, animator);
	}

	private void createCustomAnimations(LayoutTransition transition) {
		// Appearing Animator
		if (mAppearingDuration == 0) {
			mAppearingDuration = transition
					.getDuration(LayoutTransition.APPEARING);
		}
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "y", -mHeight, 0f)
				.setDuration(mAppearingDuration);
		transition.setAnimator(LayoutTransition.APPEARING, animIn);
		// Disappearing Animator
		if (mDisappearingDuration == 0) {
			mDisappearingDuration = transition
					.getDuration(LayoutTransition.DISAPPEARING);
		}
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "y", 0, -mHeight)
				.setDuration(mDisappearingDuration);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				dismiss();
			}
		});
		transition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
	}

	/**
	 * Add indicator window to WindowManager or updateViewLayout if it's already
	 * added.
	 */
	public void show() {
		if (!windowAdded) {
			mWindowManager.addView(mContainer, mWindowLayoutParams);
			windowAdded = true;
		}
		addContentView();
		mContainer.setVisibility(View.VISIBLE);
		mIndicatorView.setVisibility(View.VISIBLE);
	}

	/**
	 * Remove indicator window from WindowManager.
	 */
	public void dismiss() {
		if (windowAdded) {
			mWindowManager.removeView(mContainer);
			windowAdded = false;
		}
	}

	/**
	 * Add indicator view to it's container.
	 */
	public void addContentView() {
		if (!viewAdded) {
			mContainer.addView(mIndicatorView);
			viewAdded = true;
		}
	}

	/**
	 * Remove indicator view from it's container.
	 */
	public void removeContentView() {
		if (viewAdded) {
			mContainer.removeView(mIndicatorView);
			viewAdded = false;
		}
	}
}
