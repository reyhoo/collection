package com.chinaums.opensdk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.chinaums.opensdk.R;
import com.chinaums.opensdk.cons.OpenConst;
import com.chinaums.opensdk.download.model.BizPack;
import com.chinaums.opensdk.download.process.DynamicResourceManager;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXLogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WeexActivity extends AppCompatActivity implements IWXRenderListener {

    private static final String FILENAME = "/index.weex.js";
    private WXSDKInstance mWXSDKInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weex);
        String bizCode = getIntent().getStringExtra(OpenConst.DynamicBizIntentExtra.PAGE_BIZ_CODE);
        try {
            BizPack pack = DynamicResourceManager.getInstance().getBiz(bizCode);
            String path = pack.getResProcessPath() + FILENAME;
            mWXSDKInstance = new WXSDKInstance(this);
            mWXSDKInstance.registerRenderListener(this);
            Map<String, Object> options = new HashMap<>();
            String basePath = pack.getResProcessPath() + "/";
            options.put(WXSDKInstance.BUNDLE_URL, basePath);
            mWXSDKInstance.render(getPackageName(), loadFile(path, this), options, null, WXRenderStrategy.APPEND_ASYNC);
        } catch (Exception e) {
            Toast.makeText(this, "页面异常！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        setContentView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWXSDKInstance.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityBack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onCreateOptionsMenu(menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static String loadFile(String path, Context context) {
        if (context == null || TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            inputStream = new FileInputStream(file);
            StringBuilder builder = new StringBuilder(inputStream.available() + 10);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] data = new char[4096];
            int len = -1;
            while ((len = bufferedReader.read(data)) > 0) {
                builder.append(data, 0, len);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            WXLogUtils.e("", e.getMessage());
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                WXLogUtils.e("WXFileUtils loadAsset: ", e.getMessage());
            }
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                WXLogUtils.e("WXFileUtils loadAsset: ", e.getMessage());
            }
        }
        return "";
    }

}
