package com.example.androidjs;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/30.
 */
public class JavaScriptObject {
    Context mContxt;

    public JavaScriptObject(Context mContxt) {
        this.mContxt = mContxt;
    }
    @JavascriptInterface //sdk17版本以上加上注解
    public void fun1FromAndroid(String name) {
        Toast.makeText(mContxt, name, Toast.LENGTH_LONG).show();
    }
    @JavascriptInterface //sdk17版本以上加上注解
    public void fun2(String name) {
        Toast.makeText(mContxt, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
    }
}