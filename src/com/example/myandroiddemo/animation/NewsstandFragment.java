package com.example.myandroiddemo.animation;

import java.util.ArrayList;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.myandroiddemo.R;
import com.example.myandroiddemo.utils.AndUtils;
import android.widget.AbsListView.OnScrollListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class NewsstandFragment extends Fragment {
	private static final String TAG = "Newsstand";
	private static NewsstandFragment mEditionFragment;
	private Context mContext;
	private static ViewGroup mActionBarView;
	private ViewPager mPager;
	private TabHost mTabHost;
	private FrameLayout mHeaderFrame;
	private ImageView mHeaderImage;
	private ImageView mHeaderLogo;
	private ImageView mHomeIcon;
	private TextView mTitle;
	private int mHeaderWidth;
	private int mHeaderHeight;
	private Point mPoint1;
	private Point mPoint2;
	private int mHomeIconSize;
	private int mLogoIntrinsicSize;
	float mSlopeLogoScale = 0.0f;
	float mSlopeLogoX = 0.0f;
	float mSlopeHeaderY = 0.0f;
	// Touch sensibility.
	float mSensibility = 0.5f;
	int mActionBarHeight;
	ArrayList<View> mPagerViewList;
	ArrayList<ListView> mListViews = new ArrayList<ListView>();
	boolean canLayout = true;
	boolean canRefresh = true;
	ActionIndicatorCtrl mActionIndicator;

	public static void show(FragmentManager fm, ViewGroup actionBarView) {
		final FragmentTransaction ft = fm.beginTransaction();
		mEditionFragment = new NewsstandFragment();
		NewsstandFragment.mActionBarView = actionBarView;
		ft.replace(R.id.container_edition, mEditionFragment);
		ft.commitAllowingStateLoss();
	}

	int lastX, lastY, beginY;

	public static void onTouchEvent(MotionEvent event) {
		final NewsstandFragment ef = mEditionFragment;
		if (null != ef) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				ef.lastX = (int) event.getRawX();
				ef.lastY = (int) event.getRawY();
				if (ef.mPoint1 == null || ef.mPoint2 == null) {
					ef.mPoint1 = new Point(ef.getTranslateStartX(ef.mContext),
							ef.getTranslateStartY(ef.mContext));
					ef.mPoint2 = new Point(
							(ef.mHeaderWidth - ef.mLogoIntrinsicSize) >> 1,
							(ef.mHeaderHeight - ef.mLogoIntrinsicSize) >> 1);
					// Slope.
					ef.mSlopeLogoScale = (1 - (float) ef.mHomeIconSize
							/ ef.mLogoIntrinsicSize)
							/ (ef.mPoint2.y - ef.mPoint1.y);
					ef.mSlopeLogoX = (ef.mPoint2.x - ef.mPoint1.x)
							/ (float) (ef.mPoint2.y - ef.mPoint1.y);
					ef.mSlopeHeaderY = (ef.mHeaderHeight >> 1)
							/ (float) (ef.mPoint2.y - ef.mPoint1.y);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (ef.canLayout) {
					ef.layoutLogo(event);
					ef.layoutHeader(event);
					ef.lastX = (int) event.getRawX();
					ef.lastY = (int) event.getRawY();
				}
				// TODO Some issue to fix in ActionIndicator.
				int dy = (int) event.getRawY() - ef.beginY;
				if(dy > 0 && ef.canRefresh){
					if (dy > 5) {
						ef.mActionIndicator.show();
					}
				}else{
					//ef.mActionIndicator.removeContentView();
				}
				break;
			case MotionEvent.ACTION_UP:
				ef.mActionIndicator.dismiss();
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
		mContext = inflater.getContext();
		final View view = inflater.inflate(R.layout.edition_fragment_layout,
				container, false);
		// views
		mPager = (ViewPager) view.findViewById(R.id.section_pager);
		mTabHost = (TabHost) view.findViewById(R.id.tab_host);
		mHeaderFrame = (FrameLayout) view.findViewById(R.id.header_frame);
		mHeaderImage = (ImageView) view.findViewById(R.id.header_image);
		mHeaderLogo = (ImageView) view.findViewById(R.id.header_logo);
		mHeaderLogo.setPivotX(0.0f);
		mHeaderLogo.setPivotY(0.0f);
		mHomeIcon = (ImageView) (mActionBarView).findViewById(getResources()
				.getIdentifier("home", "id", "android"));
		mHomeIcon.setVisibility(View.INVISIBLE);
		mTitle = (TextView) (mActionBarView).findViewById(getResources()
				.getIdentifier("action_bar_title", "id", "android"));
		mTitle.setVisibility(View.GONE);
		// values
		mActionBarHeight = AndUtils.getInstance(mContext).getActionBarHeight();
		mLogoIntrinsicSize = mHeaderLogo.getBackground().getIntrinsicHeight();
		mHeaderHeight = mHeaderImage.getBackground().getIntrinsicHeight();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(mContext.WINDOW_SERVICE);
		mHeaderWidth = wm.getDefaultDisplay().getWidth();
		mHomeIconSize = getHomeIconSize(mContext);
		mPager.setPadding(0, mHeaderHeight, 0, 0);
		mActionIndicator = new ActionIndicatorCtrl(mContext, 0, 0,
				LayoutParams.MATCH_PARENT, mActionBarHeight,
				R.layout.indicator, R.id.textview);
		setupTitleAnimation();
		setupTab();
		setupViewPager(view);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mActionIndicator.dismiss();
	}

	private void setupTab() {
		if (mTabHost == null) {
			Log.e(TAG, "mTabHost is null!");
			return;
		}

	}

	private void setupViewPager(View parent) {
		if (mPager == null) {
			Log.e(TAG, "mTabHost is null!");
			return;
		}
		mPagerViewList = new ArrayList<View>();
		LayoutInflater lf = getActivity().getLayoutInflater().from(mContext);
		View newsLayout = lf.inflate(R.layout.news_layout, null);
		ListView lv1 = (ListView) newsLayout.findViewById(R.id.news_list);
		lv1.setAdapter(new MyBaseAdapter("list 1/ "));
		lv1.setOnScrollListener(new ListScrollListener());
		mListViews.add(lv1);
		mPagerViewList.add(newsLayout);
		View videoLayout = lf.inflate(R.layout.videos_layout, null);
		ListView lv2 = (ListView) videoLayout.findViewById(R.id.videos_list);
		lv2.setAdapter(new MyBaseAdapter("list 2/ "));
		lv2.setOnScrollListener(new ListScrollListener());
		mListViews.add(lv2);
		mPagerViewList.add(videoLayout);
		mPager.setAdapter(new MyPagerAdapter());
	}

	private void layoutLogo(MotionEvent event) {
		final NewsstandFragment ef = mEditionFragment;
		if (null != ef) {
			int dy = (int) event.getRawY() - ef.lastY;
			int dx = (int) ef.getLogoXDelta(dy);
			dy = (int) (dy * ef.mSensibility);
			dx = (int) (dx * ef.mSensibility);

			int left = ef.mHeaderLogo.getLeft() + dx;
			int top = ef.mHeaderLogo.getTop() + dy;
			int right = ef.mHeaderLogo.getRight() + dx;
			int bottom = ef.mHeaderLogo.getBottom() + dy;

			if (left < ef.mPoint1.x) {
				left = ef.mPoint1.x;
				right = left + ef.mHeaderLogo.getWidth();
			}
			if (left > ef.mPoint2.x) {
				left = ef.mPoint2.x;
				right = left + ef.mHeaderLogo.getWidth();
			}
			if (top < ef.mPoint1.y) {
				top = ef.mPoint1.y;
				bottom = top + ef.mHeaderLogo.getHeight();
			}
			if (top > ef.mPoint2.y) {
				top = ef.mPoint2.y;
				bottom = top + ef.mHeaderLogo.getHeight();
				canRefresh = true;
				ef.beginY = (int) event.getRawY();
			}else{
				canRefresh = false;
			}

			float scale = ef.getLogoScaleValue(top);
			ef.mHeaderLogo.setScaleX(scale);
			ef.mHeaderLogo.setScaleY(scale);
			ef.mHeaderLogo.layout(left, top, right, bottom);
			if (top < ef.mActionBarHeight >> 1) {
				ef.mTitle.setVisibility(View.VISIBLE);
			} else {
				ef.mTitle.setVisibility(View.GONE);
			}
		}
	}

	private void layoutHeader(MotionEvent event) {
		final NewsstandFragment ef = mEditionFragment;
		if (null != ef) {
			int dy1 = (int) event.getRawY() - ef.lastY;
			int dy2 = (int) ef.getHeaderYDelta(dy1);
			dy2 = (int) (dy2 * ef.mSensibility);
			int left = ef.mHeaderFrame.getLeft();
			int top = ef.mHeaderFrame.getTop();
			int right = ef.mHeaderFrame.getRight();
			int bottom = Math.max(ef.mHeaderFrame.getBottom() + dy2,
					ef.mHeaderHeight >> 1);
			bottom = Math.min(bottom, ef.mHeaderHeight);
			mPager.setPadding(0, bottom, 0, 0);
			ef.mHeaderFrame.layout(left, top, right, bottom);
		}
	}

	private float getLogoXDelta(int dy) {
		return dy * mSlopeLogoX;
	}

	private float getHeaderYDelta(int dy) {
		return dy * mSlopeHeaderY;
	}

	private float getLogoScaleValue(float y) {
		return mSlopeLogoScale * (y - mPoint2.y) + 1;
	}

	private ImageView getUpView(Context context) {
		return (ImageView) (mActionBarView).findViewById(getResources()
				.getIdentifier("up", "id", "android"));
	}

	private int getTranslateStartX(Context context) {
		ImageView upView = getUpView(context);
		MarginLayoutParams hmlp = (MarginLayoutParams) mHomeIcon
				.getLayoutParams();
		// TODO Seems that up vies's visibility is wrong.
		// if (upView != null && upView.getVisibility() != View.GONE) {
		MarginLayoutParams bmlp = (MarginLayoutParams) upView.getLayoutParams();
		int margin = bmlp.leftMargin + bmlp.rightMargin + hmlp.leftMargin;
		int intrinsicWidth = upView.getDrawable().getIntrinsicWidth();
		return intrinsicWidth + margin;
		// } else {
		// return hmlp.leftMargin;
		// }
	}

	private int getTranslateStartY(Context context) {
		MarginLayoutParams mlp = (MarginLayoutParams) mHomeIcon
				.getLayoutParams();
		return mlp.topMargin;
	}

	private int getHomeIconSize(Context context) {
		MarginLayoutParams mlp = (MarginLayoutParams) mHomeIcon
				.getLayoutParams();
		int homeIconMargin = mlp.bottomMargin + mlp.topMargin;
		return mActionBarHeight - homeIconMargin;
	}

	private void setupTitleAnimation() {
		// TODO Animations do not seem to work.
		if (mTitle == null) {
			Log.e(TAG, "setupTitleAnimation title is null!");
			return;
		}
		LayoutTransition transtion = new LayoutTransition();
		ObjectAnimator appearingAnimator = ObjectAnimator.ofFloat(null,
				"alpha", 0.0f, 1.0f).setDuration(1000);
		ObjectAnimator disAppearingAnimator = ObjectAnimator.ofFloat(null,
				"alpha", 1.0f, 0.0f).setDuration(1000);
		transtion.setAnimator(LayoutTransition.APPEARING, appearingAnimator);
		transtion.setAnimator(LayoutTransition.DISAPPEARING,
				disAppearingAnimator);
		mActionBarView.setLayoutTransition(transtion);
	}

	class MyBaseAdapter extends BaseAdapter {
		String tag;

		public MyBaseAdapter(String tag) {
			this.tag = tag;
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int pos, View v, ViewGroup p) {
			if (v == null) {
				v = new TextView(mContext);
			}
			((TextView) v).setText(tag + pos);
			return v;
		}

	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mPagerViewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mPagerViewList.get(position));
			return mPagerViewList.get(position);
		}
	}

	class ListScrollListener implements AbsListView.OnScrollListener {
		@Override
		public void onScroll(AbsListView v, int arg1, int arg2, int arg3) {
			if (v.getFirstVisiblePosition() == 0) {
				canLayout = true;
			} else {
				canLayout = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView v, int state) {
			if (state == OnScrollListener.SCROLL_STATE_IDLE
					&& v.getFirstVisiblePosition() == 0) {
				canLayout = true;
			} else {
				canLayout = false;
			}
		}

	}
}
