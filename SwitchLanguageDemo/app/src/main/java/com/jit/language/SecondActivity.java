package com.jit.language;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		Log.i("info", "SecondActivity::onCreate()");
	}
	
	@Override
	protected void onDestroy() {
		Log.i("info", "SecondActivity::onDestroy()");
		super.onDestroy();
	}
}
