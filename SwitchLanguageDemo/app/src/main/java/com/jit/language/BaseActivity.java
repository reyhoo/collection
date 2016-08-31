package com.jit.language;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//初始化PreferenceUtil
		PreferenceUtil.init(this);
		//根据上次的语言设置，重新设置语言

	}
	
	
	protected void switchLanguage(String language) {



		//设置应用语言类型



		Log.i("switchLanguage","language:"+language);
		Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
       if (language.equals("en")) {
            config.locale = Locale.ENGLISH;
		   resources.updateConfiguration(config, dm);
		   PreferenceUtil.commitString("language", language);
        } else if(language.equals("zh")){
        	 config.locale = Locale.SIMPLIFIED_CHINESE;
		   resources.updateConfiguration(config, dm);
		   PreferenceUtil.commitString("language", language);
        }else{
		   config.locale = Locale.getDefault();
		   resources.updateConfiguration(config, dm);
		   PreferenceUtil.commitString("language", "");
	   }

        
        //保存设置语言的类型

    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		Log.i("info", "onConfigurationChanged:"+newConfig.locale);

		
		
	}
}
