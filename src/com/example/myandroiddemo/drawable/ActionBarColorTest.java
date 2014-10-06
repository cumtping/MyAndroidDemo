package com.example.myandroiddemo.drawable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.myandroiddemo.R;

/**
 * @category Failure
 * @author WenpingWang
 * @date 2014/03/23
 *
 */
public class ActionBarColorTest extends Activity {
	private static final String TAG = "color";
	static final int ANIM_TIME = 16;
	static final int ANIM_TIME_INTERVAL = 500;
	static final int MSG_TRANS_ACTION_BAR = 101;
	long mActionBarColor = 0xffff0000;
	boolean mTransAnimation = false;
	int mAlphaInterval;
	int mCurAlpha = 0xff;

	int[] colorList = { 0xffff0000, 0xe0ff0000, 0xd0ff0000, 0xc0ff0000,
			0xb0ff0000, 0xa0ff0000, 0x90ff0000, 0x80ff0000, 0x70ff0000,
			0x60ff0000, 0x50ff0000, 0x40ff0000, 0x30ff0000, 0x20ff0000,
			0x10ff0000, 0x00ff0000 };

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_bar_color_test_layout);

		mAlphaInterval = (int) Math.ceil(0xff / (double) ANIM_TIME);

		setActionBarColorAlpha(0xff);
//		setActionBarColorAlpha(0x77);

		Log.d(TAG, "c1=" + Color.argb(0xff, 255, 0, 0));
		Log.d(TAG, "c2=" + mActionBarColor);

		findViewById(R.id.trans_action_bar).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startTransActionBarAnimation();
						// setActionBarColorAlpha(0xaa);
					}
				});
		findViewById(R.id.detrans_action_bar).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// setActionBarColorAlpha(0);
						startDeTransActionBarAnimation();
					}
				});
	}
	@Override
	protected void onResume() {
		super.onResume();
		setActionBarColorAlpha(0x77);
//		mHandler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				setActionBarColorAlpha(0x77);
//			}
//		}, 3000);
	}
	
	private void startTransActionBarAnimation() {
//		setActionBarColorAlpha(0);
		setActionBarColor(1);
//		mHandler.removeMessages(MSG_TRANS_ACTION_BAR);
//		setActionBarColorAlpha(0xff);
		mTransAnimation = true;
//		Message msg = new Message();
//		msg.what = MSG_TRANS_ACTION_BAR;
//		msg.arg1 = 1;
//		mHandler.sendMessageDelayed(msg, 1000);
		
	}

	private void startDeTransActionBarAnimation() {
		setActionBarColor(1);
//		setActionBarColorAlpha(0xff);
//		mHandler.removeMessages(MSG_TRANS_ACTION_BAR);
//		setActionBarColorAlpha(0);
		mTransAnimation = false;
//		Message msg = new Message();
//		msg.what = MSG_TRANS_ACTION_BAR;
//		msg.arg1 = 1;
//		mHandler.sendMessageDelayed(msg, 1000);
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TRANS_ACTION_BAR:
				// int alpha = 0;
				// if(mTransAnimation){
				// //alpha = 0xff - msg.arg1 * mAlphaInterval;
				// }else{
				// //alpha = msg.arg1 * mAlphaInterval;
				// msg.arg1 = ANIM_TIME - msg.arg1;
				// }
				setActionBarColor(msg.arg1);
				if (msg.arg1 < ANIM_TIME) {
					Message msg2 = new Message();
					msg2.what = MSG_TRANS_ACTION_BAR;
					msg2.arg1 = msg.arg1 + 1;
					this.sendMessageDelayed(msg2, ANIM_TIME_INTERVAL);
				}
				break;

			default:
				break;
			}
		}
	};

	// private long getActionBarColor(int alpha){
	// return mActionBarColor & (alpha << 24 | 0xffffffff);
	// }

	private void setActionBarColor(int count) {
		if (!mTransAnimation) {
			count = ANIM_TIME - count;
		}
		count = Math.min(ANIM_TIME, count);
		count = Math.max(1, count);
		Log.d(TAG, "count=" + count + " color=" + colorList[count - 1]);
		ColorDrawable colorDrawable = new ColorDrawable(colorList[count - 1]);
		getActionBar().setBackgroundDrawable(colorDrawable);
	}

	@SuppressLint("NewApi")
	private void setActionBarColorAlpha(int alpha) {
		int color = Color.rgb(0, 0, 255);
		Log.d(TAG, "alpha=" + alpha + " color=" + color);
		final ColorDrawable colorDrawable = new ColorDrawable(color);
		colorDrawable.setAlpha(alpha);
		getWindow().getDecorView().postOnAnimation(new Runnable() {
			@Override
			public void run() {
				getActionBar().setBackgroundDrawable(colorDrawable);
			}
		});
	}
}
