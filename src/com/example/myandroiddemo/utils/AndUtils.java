package com.example.myandroiddemo.utils;

import android.content.Context;
import android.util.TypedValue;

public class AndUtils {
	private Context mContext;
	private static AndUtils mInst;

	synchronized public static AndUtils getInstance(Context c) {
		if (mInst == null) {
			mInst = new AndUtils(c);
		}
		return mInst;
	}

	private AndUtils(Context c) {
		mContext = c;
	}

	public int getStatusBarHeight() {
		int attrId = mContext.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		return (int) mContext.getResources().getDimension(attrId);
	}

	public int getActionBarHeight(Context context) {
		TypedValue tv = new TypedValue();
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true)) {
			return TypedValue.complexToDimensionPixelSize(tv.data, context
					.getResources().getDisplayMetrics());
		}
		return 0;
	}

}
