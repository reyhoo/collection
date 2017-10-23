package com.chinaums.opensdk.net;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.chinaums.opensdk.net.http.HttpRequest;
import com.chinaums.opensdk.net.http.HttpResponse;
import com.chinaums.opensdk.net.http.HttpTransporter;
import com.chinaums.opensdk.net.http.IHttpRequestCallback;
import com.chinaums.opensdk.util.UmsLog;

public class UmsConnection {

    private static HttpTransporter httpTransporter = new HttpTransporter();

    /**
     * 进行http请求，在后台线程执行。
     */
    static public void doHttpRequest(final Context context,
                                     final HttpRequest request, final Timeout timeout,
                                     final ILoadingWidget pw, final IHttpRequestCallback callback,
                                     IProgressUpdate progress) {
        AsyncTask<HttpRequest, Void, Object> task = new AsyncTask<HttpRequest, Void, Object>() {
            @Override
            protected void onPreExecute() {
                if (pw != null && isContextAlive(context))
                    pw.showLoading(context);
            }

            @Override
            protected Object doInBackground(HttpRequest... arg0) {
                try {
                    HttpResponse response = httpTransporter.doHttpRequest(
                            request, timeout.getValue(), null);
                    return response;
                } catch (Exception e) {
                    UmsLog.e("HTTP通讯错误", e);
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (pw != null && isContextAlive(context))
                    pw.hideLoading(context);

                if (result instanceof Exception) {
                    if (isContextAlive(context))
                        callback.onException(context, (Exception) result);
                } else if (result instanceof HttpResponse) {
                    HttpResponse response = (HttpResponse) result;
                    if (isContextAlive(context))
                        callback.onSuccess(context, response);
                } else {
                    UmsLog.e("这不可能！");
                }
            }
        };
        task.execute();
    }

    /**
     * 在当前线程（非主线程）进行同步http请求。
     */
    static public HttpResponse doHttpRequest(Context context,
                                             HttpRequest request, Timeout timeout, IProgressUpdate progress)
            throws Exception {
        return httpTransporter.doHttpRequest(request, timeout.getValue(),
                progress);
    }

    /**
     * 检查上下文是否还活着，避免出现context关闭，但是网络请求回来导致程序出错。
     */
    static private boolean isContextAlive(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !activity.isFinishing();
        }
        return true;
    }

}