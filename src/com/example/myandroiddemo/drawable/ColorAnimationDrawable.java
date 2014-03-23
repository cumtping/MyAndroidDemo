package com.example.myandroiddemo.drawable;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.AnimationUtils;

/**
 * 
 * @author Cyril Mottiers
 * @modify WenpingWang
 * @link http://cyrilmottier.com/2012/11/27/actionbar-on-the-move/
 * @feature Drawable with color animation & alpha animation[New].
 * @date 2014/03/23
 * 
 * @issues
 * 1.Have bounds when translucent.
 */
public class ColorAnimationDrawable extends Drawable implements Animatable {
	private static final long FRAME_DURATION = 1000 / 60;
	private static final int ACCCENT_COLOR = 0x33FFFFFF;
	private static final int DIM_COLOR = 0x33000000;
	private static final Random mRandom = new Random();
	private final Paint mPaint = new Paint();
	private long mDuration = 1000;
	private long mStartTime;
	private boolean mIsRunning;
	private int mStartColor;
	private int mEndColor;
	private int mCurrentColor;
	private int mStartAlpha;
	private int mEndAlpha;
	private int mCurrentAlpha;
	private boolean mRepeat = false;

	/**
	 * Constructor
	 */
	public ColorAnimationDrawable() {
	}

	/**
	 * Constructor
	 */
	public ColorAnimationDrawable(int color, int alpha) {
		mCurrentColor = mStartColor = mEndColor = color;
		mCurrentAlpha = mStartAlpha = mEndAlpha = alpha;
	}

	@Override
	public void draw(Canvas canvas) {
		final Rect bounds = getBounds();

		if(mCurrentAlpha == 0){
			return;
		}

		mPaint.setColor(mCurrentColor);
		mPaint.setAlpha(mCurrentAlpha);
		canvas.drawRect(bounds, mPaint);

		mPaint.setColor(ACCCENT_COLOR);
		canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.top + 1,
				mPaint);

		mPaint.setColor(DIM_COLOR);
		canvas.drawRect(bounds.left, bounds.bottom - 2, bounds.right,
				bounds.bottom, mPaint);
	}

	@Override
	public void setAlpha(int alpha) {
		mCurrentAlpha = alpha;
	}

	public void setAlpha(int startAlpha, int endAlpha) {
		mStartAlpha = startAlpha;
		mEndAlpha = endAlpha;
	}

	public void setColor(int startColor, int endColor) {
		mStartColor = startColor;
		mEndAlpha = endColor;
	}

	public boolean ismRepeat() {
		return mRepeat;
	}

	public void setRepeat(boolean repeat) {
		this.mRepeat = mRepeat;
	}

	public void setDuration(long duration) {
		mDuration = duration;
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		oops("setColorFilter(ColorFilter)");
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSPARENT;
	}

	@Override
	public void start() {
		if (!isRunning()) {
			mIsRunning = true;
			mStartTime = AnimationUtils.currentAnimationTimeMillis();
			scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
			invalidateSelf();
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			unscheduleSelf(mUpdater);
			mIsRunning = false;
		}
	}

	@Override
	public boolean isRunning() {
		return mIsRunning;
	}

	private void oops(String message) {
		throw new UnsupportedOperationException(
				"ColorAnimationDrawable doesn't support " + message);
	}

	private static int randomColor() {
		return mRandom.nextInt() & 0x00FFFFFF;
	}

	private static int evaluate(float fraction, int startValue, int endValue) {
		return (int) (startValue + fraction * (endValue - startValue));
	}

	private final Runnable mUpdater = new Runnable() {
		@Override
		public void run() {
			long now = AnimationUtils.currentAnimationTimeMillis();
			long duration = now - mStartTime;
			if (duration >= mDuration) {
				if (mRepeat) {
					mStartColor = mEndColor;
					mEndColor = randomColor();
					mStartTime = now;
					mCurrentColor = mStartColor;
				} else {
					return;
				}
			} else {
				float fraction = duration / (float) mDuration;
				// @formatter:off
				mCurrentAlpha = evaluate(fraction, mStartAlpha, mEndAlpha);
				mCurrentColor = Color.rgb(
						evaluate(fraction, Color.red(mStartColor),
								Color.red(mEndColor)), // red
						evaluate(fraction, Color.green(mStartColor),
								Color.green(mEndColor)), // green
						evaluate(fraction, Color.blue(mStartColor),
								Color.blue(mEndColor))); // blue
				// @formatter:on
			}
			scheduleSelf(mUpdater, SystemClock.uptimeMillis() + FRAME_DURATION);
			invalidateSelf();
		}
	};
}
