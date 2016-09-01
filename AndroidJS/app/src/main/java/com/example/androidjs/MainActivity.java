package com.example.androidjs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private WebView mWebView;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.argb(0,0,0,0));
        mWebView.addJavascriptInterface(new JavaScriptObject(this),"myObj");
        //载入js
        mWebView.loadUrl("file:///android_asset/index.html");

        //点击调用js中方法
        findViewById(R.id.mBtn1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:funFromjs()");
                Toast.makeText(MainActivity.this, "调用javascript:funFromjs()", Toast.LENGTH_LONG).show();
            }
        });
    }
}
