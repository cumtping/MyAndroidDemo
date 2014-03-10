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
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
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
public class EditionFragment2 extends Fragment {
	private static final String TAG = "Newsstand";
	private static EditionFragment2 mEditionFragment;
	private View mActionBarView;
	private ViewPager mPager;
	private TabHost mTabHost;
	private View mHeaderImage;
	private View mHeaderLogo;
	private ImageView mHomeIcon;
	private TextView mTitle;
	Scene mScene1, mScene2;
    ViewGroup mSceneRoot = null;
    TransitionManager mTransitionManager;
    boolean mTransMax = true;

	public static void show(FragmentManager fm, View actionBarView) {
		final FragmentTransaction ft = fm.beginTransaction();
		mEditionFragment = new EditionFragment2();
		mEditionFragment.mActionBarView = actionBarView;

		ft.replace(R.id.container_edition, mEditionFragment);
		ft.commitAllowingStateLoss();
	}

	private static float motionStartY = 0.0f;
	private static float motionRecord = 0.0f;
	private static float sensibility = 0.0f;

	public static void onTouchEvent(MotionEvent event) {
		if (null != mEditionFragment) {
			final EditionFragment2 ef = mEditionFragment;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(ef.mTransMax){
					ef.mTransitionManager.transitionTo(ef.mScene2);
					ef.mTransMax = false;
				}else{
					ef.mTransitionManager.transitionTo(ef.mScene1);
					ef.mTransMax = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
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
		final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.edition_fragment_layout0,
				container, false);
		mSceneRoot = (ViewGroup) view.findViewById(R.id.sceneRoot);
        TransitionInflater inflater2 = TransitionInflater.from(context);
        mScene1 = Scene.getSceneForLayout(mSceneRoot, R.layout.edition_fragment_layout, context);
        mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.edition_fragment_layout2, context);
        mTransitionManager = inflater2.inflateTransitionManager(R.transition.transitions_mgr,
                mSceneRoot);

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
}
