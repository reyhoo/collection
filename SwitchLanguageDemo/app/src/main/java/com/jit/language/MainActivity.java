package com.jit.language;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity implements OnClickListener{

	private Dialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("info", "MainActivity::onCreate():"+ Locale.getDefault());
		switchLanguage(PreferenceUtil.getString("language", ""));
		setContentView(R.layout.activity_main);
		setRequestedOrientation();
		TextView textView = (TextView) findViewById(R.id.text);
		Button button = (Button) findViewById(R.id.btn);
		Button button2 = (Button) findViewById(R.id.btn_2);
		textView.setText(R.string.hello_world);
		button.setText(R.string.switch_language);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mDialog == null) {
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.dialog_select_lanuage,null);
					TextView english = (TextView) layout.findViewById(R.id.select_english);
					TextView chinese = (TextView) layout.findViewById(R.id.select_chinese);
					TextView auto = (TextView) layout.findViewById(R.id.select_auto);
					mDialog = new Dialog(MainActivity.this, R.style.Custom_Dialog_Theme);
					mDialog.setCanceledOnTouchOutside(false);
					english.setOnClickListener(MainActivity.this);
					chinese.setOnClickListener(MainActivity.this);
					auto.setOnClickListener(MainActivity.this);
					mDialog.setContentView(layout);
				}
				mDialog.show();
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(MainActivity.this, SecondActivity.class);
			    startActivity(it);
			}
		});

		Log.i("info","Time:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("",new DateFormatSymbols());
		Log.i("info","MainActivity:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US).format(new Date())+","+ Locale.getDefault());
	}

	@Override
	public void onClick(View v) {
		mDialog.dismiss();	
		switch (v.getId()) {
			case R.id.select_english:
				switchLanguage("en");
				break;
			case R.id.select_chinese:
				switchLanguage("zh");
				break;
			case R.id.select_auto:
				switchLanguage("auto");
				break;
			default:
				break;
		}
		//更新语言后，destroy当前页面，重新绘制

		Intent it = new Intent(MainActivity.this, MainActivity.class);
		startActivity(it);
		finish();

	}
	@Override
	protected void onDestroy() {
		Log.i("info", "MainActivity::onDestroy()");
		super.onDestroy();
	}
}
