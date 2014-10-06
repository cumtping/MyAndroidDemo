package com.example.myandroiddemo.animation;

import com.example.myandroiddemo.utils.XLog;

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
public class ActionIndicatorCtrl {
	private Context mContext = null;
	private int mHeight;
	private ViewGroup mContainer = null;
	private View mIndicatorView = null;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowLayoutParams;
	private boolean mProcessing = false;
	// private boolean viewAdded = true;
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
	public ActionIndicatorCtrl(Context c, int startX, int startY, int width,
			int height, int layoutId, int indicatorViewId) {
		mContext = c;
		mHeight = height;
		// Check arguments
		if (mContext == null) {
			throw new IllegalArgumentException("context is null");
		}
		mContainer = (ViewGroup) LayoutInflater.from(c).inflate(layoutId, null);
		if (mContainer == null) {
			throw new IllegalArgumentException("indicatorLayout is invalid");
		}

		// indicator view
		mIndicatorView = mContainer.findViewById(indicatorViewId);
		if (mIndicatorView == null) {
			throw new IllegalArgumentException("indicatorViewId is invalid");
		}
		// mIndicatorView.setVisibility(View.GONE);

		// window params
		mWindowManager = (WindowManager) c.getSystemService(c.WINDOW_SERVICE);
		mWindowLayoutParams = new LayoutParams(width, height,
				LayoutParams.TYPE_SYSTEM_OVERLAY,
				LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.RGB_888);
		mWindowLayoutParams.gravity = Gravity.TOP;
		mWindowLayoutParams.x = startX;
		mWindowLayoutParams.y = startY;

		// layout transition
		initLayoutTransition();
		// mContainer.setLayoutTransition(mLayoutTransitioner);
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

	private void initLayoutTransition() {
		mLayoutTransitioner = new LayoutTransition();
		// Appearing Animator
		if (mAppearingDuration == 0) {
			mAppearingDuration = mLayoutTransitioner
					.getDuration(LayoutTransition.APPEARING);
		}
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "y", -mHeight, 0f)
				.setDuration(mAppearingDuration);
		mLayoutTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		// Disappearing Animator
		if (mDisappearingDuration == 0) {
			mDisappearingDuration = mLayoutTransitioner
					.getDuration(LayoutTransition.DISAPPEARING);
		}
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "y", 0, -mHeight)
				.setDuration(mDisappearingDuration);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				mWindowManager.removeView(mContainer);
				mProcessing = false;
			}
		});
		mLayoutTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		XLog.printLogD("Custom aniamtion created!");
		XLog.printLogD("Appearing duration=" + mAppearingDuration + "! From -"
				+ mHeight + "px to 0");
		XLog.printLogD("Disappearing duration=" + mDisappearingDuration
				+ "! From 0 to " + mHeight);
	}

	/**
	 * Add indicator window to WindowManager or updateViewLayout if it's already
	 * added.
	 */
	public void show() {
		if (!mProcessing) {
//			mWindowManager.removeView(mContainer);
		mProcessing = true;
		XLog.printLogD("add ViewGroup to WindowManager");
		mWindowManager.addView(mContainer, mWindowLayoutParams);
		// mIndicatorView.setVisibility(View.VISIBLE);
		// mContainer.addView(mIndicatorView);
		}
	}

	/**
	 * Remove indicator window from WindowManager.
	 */
	public void dismiss() {
		if (mProcessing) {
			XLog.printLogD("Remove ViewGroup from WindowManager");
			// mContainer.removeView(mIndicatorView);
			// mIndicatorView.setVisibility(View.GONE);
			mWindowManager.removeView(mContainer);
			mProcessing = false;
		}
	}
}
