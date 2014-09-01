package com.example.myandroiddemo.widget;

import com.example.myandroiddemo.R;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabHostTest extends TabActivity {
	TabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host_test_layout);
		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		TabSpec spec = mTabHost.newTabSpec("tab1");
		spec.setContent(R.id.btn);
		TabSpec spec2 = mTabHost.newTabSpec("tab2");
		spec2.setContent(R.id.btn);
		mTabHost.addTab(spec2);
	}
}
