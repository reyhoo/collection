package com.example.administrator.bigfileupload;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.uploadBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadBtn:
                upload();
                break;
        }
    }

    private void upload() {
        DialogUtil.showLoading(this);
        Map<String,FileItem>fileItems = new HashMap<>();
        FileItem fileitem = new FileItem();
        fileitem.setFile(new File("/mnt/sdcard/android-ndk-r11b-windows-x86_64.zip"));
        fileItems.put("file",fileitem);
//        HttpClient.getInstance().fileUpload("http://192.168.0.100:8088/testServer/upload", fileItems, new HttpCallback<byte[]>() {
//            @Override
//            public void onResult(int statusCode, byte[] data) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DialogUtil.cancelLoading();
//                    }
//                });
//
//                try {
//                    Log.i("MainActivity_","MainActivity_"+new String(data,"utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onException(Exception e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DialogUtil.cancelLoading();
//                    }
//                });
//                e.printStackTrace();
//
//            }
//
//            @Override
//            public void onProgress(long writencount, long totalcount) {
//            }
//        });
        HashMap<String,Object>items = new HashMap<>();
        items.put("file",fileitem.getFile());
        items.put("username","中华人民共和国");
        upLoadFile("http://192.168.0.100:8088/testServer/upload",items);
    }





    /**
     *上传文件
     * @param actionUrl 接口地址
     * @param paramsMap 参数
     * @param callBack 回调
     * @param <T>
     */
    public void upLoadFile(String requestUrl, HashMap<String, Object> paramsMap) {
        try {
            //补全请求地址
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, "你好"+file.getName(), RequestBody.create(null, file));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            //单独设置参数 比如读取超时时间
            final Call call = new OkHttpClient().newBuilder().build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    DialogUtil.cancelLoading();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    DialogUtil.cancelLoading();
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.i("MainActivity_","MainActivity_"+string);
                    } else {
                        Log.i("MainActivity_","MainActivity_code:"+response.code());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.cancelLoading();
        }
    }
}
