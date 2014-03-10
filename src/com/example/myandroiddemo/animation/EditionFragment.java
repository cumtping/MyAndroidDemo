package com.example.myandroiddemo.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.example.myandroiddemo.R;
import com.example.myandroiddemo.utils.AndUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class EditionFragment extends Fragment {
	private static final String TAG = "Newsstand";
	private static EditionFragment mEditionFragment;
	private View mActionBarView;
	private ViewPager mPager;
	private TabHost mTabHost;
	private View mHeaderImage;
	private View mHeaderLogo;
	private ImageView mHomeIcon;
	private TextView mTitle;
	private float mScaleX;
	private float mCurScaleX = 1.0f;
	private float mScaleY;
	private float mCurScaleY = 1.0f;
	private int mTransX;
	private int mTransY;
	private int mTransStartX;
	private int mTransStartY;
	private int mCurTransY;
	private int mCurTransX;

	public static void show(FragmentManager fm, View actionBarView) {
		final FragmentTransaction ft = fm.beginTransaction();
		mEditionFragment = new EditionFragment();
		mEditionFragment.mActionBarView = actionBarView;

		ft.replace(R.id.container_edition, mEditionFragment);
		ft.commitAllowingStateLoss();
	}

	private static float motionStartY = 0.0f;
	private static float motionRecord = 0.0f;
	private static float sensibility = 0.0f;

	public static void onTouchEvent(MotionEvent event) {
		if (null != mEditionFragment) {
			final EditionFragment ef = mEditionFragment;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				motionStartY = event.getY();
				motionRecord = motionStartY;
				Log.d(TAG, "x=" + event.getX() + " y=" + event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				float deltaY = event.getY() - motionStartY;
				float deltaX = deltaY * ef.mTransX / ef.mTransY;
				if (Math.abs(event.getY() - motionRecord) > sensibility) {
					// Log.d(TAG, "move delta=" + deltaY);
					ef.translateView(ef.mHeaderLogo, (int) deltaX, (int) deltaY);
					motionRecord = event.getY();
				}
				break;
			default:
				break;
			}
		} else {
			Log.d(TAG, "move mEditionFragment=null");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context context = inflater.getContext();
		final View view = inflater.inflate(R.layout.edition_fragment_layout,
				container, false);
		// views
		mPager = (ViewPager) view.findViewById(R.id.section_pager);
		mTabHost = (TabHost) view.findViewById(R.id.tab_host);
		mHeaderImage = view.findViewById(R.id.header_image);
		mHeaderLogo = view.findViewById(R.id.header_logo);
		mHomeIcon = (ImageView) (mActionBarView).findViewById(getResources()
				.getIdentifier("home", "id", "android"));
		mHomeIcon.setVisibility(View.INVISIBLE);
		mTitle = (TextView) (mActionBarView).findViewById(getResources()
				.getIdentifier("action_bar_title", "id", "android"));
		mTitle.setVisibility(View.INVISIBLE);
		// intrinsic values
		int logoIntrinsicHeight = mHeaderLogo.getBackground()
				.getIntrinsicHeight();
		int logoIntrinsicWidth = mHeaderLogo.getBackground()
				.getIntrinsicWidth();
		int frameHeight = mHeaderImage.getBackground().getIntrinsicHeight();
		WindowManager wm = (WindowManager) context
				.getSystemService(context.WINDOW_SERVICE);
		int frameWidth = wm.getDefaultDisplay().getWidth();
		// home icon size
		int homeIconSize = getHomeIconSize(context);
		mScaleY = homeIconSize / (float) logoIntrinsicHeight;
		mTransX = (frameWidth - logoIntrinsicWidth) / 2 - mTransStartX;
		mTransY = (frameHeight - logoIntrinsicHeight) / 2 - mTransStartY;
		mCurTransX = mTransX;
		mCurTransY = mTransY;

		// translateView(mHomeIcon, mTranslateX, mTranslateY);
		// mHomeIcon.layout(mTranslateX, mTranslateY, mTranslateX
		// + homeIntrinsicWidth, mTranslateY + homeIntrinsicHeight);
		Log.d(TAG, "mScaleY=" + mScaleY + " mTranslateY=" + mTransY
				+ " mTranslateX=" + mTransX + " homeIconSize=" + homeIconSize);
		
//		mHeaderLogo.setLayoutParams(params)

		mHeaderLogo.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				Log.d(TAG, "onLayoutChange l=" + left + " t=" + top + " r=" + right + " b=" + bottom);
				Log.d(TAG, "onLayoutChange ol=" + oldLeft + " ot=" + oldTop + " or=" + oldRight + " ob=" + oldBottom);
			}
		});
		
		mHeaderLogo.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
			
			@Override
			public void onViewDetachedFromWindow(View v) {
				Log.d(TAG, "onViewDetachedFromWindow");
			}
			
			@Override
			public void onViewAttachedToWindow(View v) {
				Log.d(TAG, "onViewAttachedToWindow");
			}
		});
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private int ANIM_DURATION = 10;

	private void translateView(View view, int deltaX, int deltaY) {
		int tmpX = mCurTransX + deltaX;
		int tmpY = mCurTransY + deltaY;
		float tmpScaleX;
		float tmpScaleY;
		if (tmpY > mTransY || tmpY <= mTransStartY) {
			return;
		}

		tmpScaleX = tmpScaleY = 1 - (1 - mScaleY) * (mTransY - tmpY) / mTransY;
//		Log.d(TAG, "translateView fromX=" + mCurTransX + " toX=" + tmpX);
//		Log.d(TAG, "translateView fromY=" + mCurTransY + " toY=" + tmpY);
//		Log.d(TAG, "translateView fromScaleX=" + mCurScaleX + " toScaleX="
//				+ tmpScaleX);
//		Log.d(TAG, "translateView fromScaleY=" + mCurScaleY + " toScaleY="
//				+ tmpScaleY);

		AnimatorSet set = new AnimatorSet();
		// animX
		ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
				mCurTransX, tmpX);
		animX.setDuration(ANIM_DURATION);
		// animY
		ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
				mCurTransY, tmpY);
		animY.setDuration(ANIM_DURATION);
		// scaleX
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX",
				mCurScaleX, tmpScaleX);
		animY.setDuration(ANIM_DURATION);
		// scaleY
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY",
				mCurScaleY, tmpScaleY);
		animY.setDuration(ANIM_DURATION);
		/** Need to set pivot **/
		view.setPivotX(0.0f);
		view.setPivotY(0.0f);
		// animation set
		set.play(scaleY).with(scaleX).with(animY).with(animX);
		set.start();
		mCurTransX = tmpX;
		mCurTransY = tmpY;
		mCurScaleX = tmpScaleX;
		mCurScaleY = tmpScaleY;
	}

	private int getHomeIconSize(Context context) {
		int actionbarHeight = AndUtils.getInstance(context).getActionBarHeight(
				context);
		MarginLayoutParams mlp = (MarginLayoutParams) mHomeIcon
				.getLayoutParams();
		int homeIconMargin = mlp.bottomMargin + mlp.topMargin;
		mTransStartY = mlp.topMargin;
		mTransStartX = getTranslateStartX(context);
		Log.d(TAG, "StartY=" + mTransStartY + " StartX=" + mTransStartX);
		return actionbarHeight - homeIconMargin;
	}

	private ImageView getUpView(Context context) {
		return (ImageView) (mActionBarView).findViewById(getResources()
				.getIdentifier("up", "id", "android"));
	}

	private int getTranslateStartX(Context context) {
		ImageView upView = getUpView(context);
		MarginLayoutParams bmlp = (MarginLayoutParams) upView.getLayoutParams();
		MarginLayoutParams hmlp = (MarginLayoutParams) mHomeIcon
				.getLayoutParams();
		int margin = bmlp.leftMargin + bmlp.rightMargin + hmlp.leftMargin;
		int intrinsicWidth = upView.getDrawable().getIntrinsicWidth();
		return intrinsicWidth + margin;
	}
}
