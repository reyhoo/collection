package com.example.yaolei.h5test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("file:///android_asset/index.html");

        Map<String,String> params = new HashMap<>();
        params.put("name","姚磊");
        try {
                HttpClient.getInstance().post("http://192.168.0.102:8088/JQuery/hello",params,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        okhttp3.OkHttpClient okHttpClient  = new okhttp3.OkHttpClient();
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
//        requestBuilder.post(new FormBod)
        okhttp3.RequestBody body = new FormBody.Builder().add("name","姚磊").build();
//        requestBuilder.url("http://192.168.0.102:8088/JQuery/hello?name=姚磊").get();
        requestBuilder.url("http://192.168.0.102:8088/JQuery/hello").post(body);
        okHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
