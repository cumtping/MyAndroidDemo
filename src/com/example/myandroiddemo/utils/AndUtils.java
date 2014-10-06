package com.example.myandroiddemo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class AndUtils {
	private static final String TAG = "AndUtils";
	private static final boolean DEBUG = true;
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
		if(c == null){
			throw new IllegalArgumentException("Context can not be null!");
		}
	}

	public int getStatusBarHeight() {
		int attrId = mContext.getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		return (int) mContext.getResources().getDimension(attrId);
	}

	public int getActionBarHeight() {
		TypedValue tv = new TypedValue();
		if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize,
				tv, true)) {
			return TypedValue.complexToDimensionPixelSize(tv.data, mContext
					.getResources().getDisplayMetrics());
		}
		return 0;
	}
    /**
     * @return the actionBarView
     */
    public ViewGroup getActionBarView(Activity act) {
    	if(act == null){
    		if(DEBUG) Log.d(TAG, "getActionBarView is null");
    		return null;
    	}
        int resId;
        resId = act.getResources().getIdentifier("action_bar", "id", "android");
        if (resId > 0)
            return (ViewGroup) act.findViewById(resId);

        return null;
    }

}
