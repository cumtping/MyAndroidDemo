package com.example.myandroiddemo.andrepeater;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.myandroiddemo.R;

public class AndRepeater extends Activity {
	MediaPlayer mPlayer;
	SeekBar mSeekBar;
	boolean mIsPlay = false;
	int UPDATE_TIME = 100;
	private static final int POST_SET_PROGRESS = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.andrepeater_layout);

		mSeekBar = (SeekBar) findViewById(R.id.seek);

		findViewById(R.id.start).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPlayer != null) {
					mPlayer.release();
				}
				mPlayer = MediaPlayer.create(AndRepeater.this,
						R.raw.moon_river);
				mSeekBar.setMax(mPlayer.getDuration());
				mPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						Message msg = mHander.obtainMessage();
						msg.what = POST_SET_PROGRESS;
						msg.arg1 = 0;
						mHander.sendMessageDelayed(msg, UPDATE_TIME + 50);
						mIsPlay = false;
						mPlayer.release();
					}
				});
				mPlayer.start();
				mIsPlay = true;
				UpdateProgressTask task = new UpdateProgressTask();
				task.execute(UPDATE_TIME);
			}
		});

		findViewById(R.id.pause).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPlayer != null) {
					mPlayer.pause();
					mIsPlay = false;
				}
			}
		});

		findViewById(R.id.resume).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPlayer != null) {
					mPlayer.start();
					mIsPlay = true;
					UpdateProgressTask task = new UpdateProgressTask();
					task.execute(UPDATE_TIME);
				}
			}
		});

		findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPlayer != null) {
					mPlayer.stop();
					mPlayer.release();
					mIsPlay = false;
					mPlayer = null;
					Message msg = mHander.obtainMessage();
					msg.what = POST_SET_PROGRESS;
					msg.arg1 = 0;
					mHander.sendMessageDelayed(msg, UPDATE_TIME + 50);
				}
			}
		});

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (mPlayer != null) {
					mPlayer.seekTo(seekBar.getProgress());
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.release();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	class UpdateProgressTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mSeekBar.setProgress(values[0]);
			if (mIsPlay) {
				doInBackground(UPDATE_TIME);
			}
		}

		@Override
		protected String doInBackground(Integer... params) {
			if (mIsPlay) {
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mPlayer != null) {
					publishProgress(mPlayer.getCurrentPosition());
				}
			}
			return null;
		}

	}
	
	Handler mHander = new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
			case POST_SET_PROGRESS:
				int progress = msg.arg1;
				mSeekBar.setProgress(progress);
				break;

			default:
				break;
			}
		}
	};
}
