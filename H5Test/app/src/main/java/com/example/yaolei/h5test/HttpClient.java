package com.example.yaolei.h5test;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClient {

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String FILE_UPLOAD_CONTENT_TYPE = "multipart/form-data; boundary=";

    private static final String boundaryStart = "--------FormBoundary";
    private static final String boundaryEnd = "--";

    private static final int CONN_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    private ExecutorService threadPool;

    private synchronized ExecutorService getThreadPool() {
        if (threadPool == null || threadPool.isShutdown() || threadPool.isTerminated()) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    private HttpClient() {
    }

    private static HttpClient mHttpClient;

    public static synchronized HttpClient getInstance() {
        if (mHttpClient == null) {
            mHttpClient = new HttpClient();
        }
        return mHttpClient;
    }


    public void post(final String url, final Map<String, String> params, final HttpCallback<byte[]> httpCallback) {


        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", POST_CONTENT_TYPE);
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    conn = initConnection(url, METHOD_POST, CONN_TIMEOUT, READ_TIMEOUT, headers);
                    byte[] data = getParamsBytes(params);
                    if (data != null && data.length > 0) {
                        OutputStream out = conn.getOutputStream();
                        out.write(data);
                        out.flush();
                        out.close();
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    readData(conn, out, httpCallback);
                    callbackResult(httpCallback, conn.getResponseCode(), out.toByteArray());
                } catch (Exception e) {
                    callbackException(httpCallback, e);
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });

    }

    public void post(final String url, final String bodyData, final HttpCallback<byte[]> httpCallback) {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    conn = initConnection(url, METHOD_POST, CONN_TIMEOUT, READ_TIMEOUT, headers);
                    byte[] data = bodyData.getBytes("utf-8");
                    if (data != null && data.length > 0) {
                        OutputStream out = conn.getOutputStream();
                        out.write(data);
                        out.flush();
                        out.close();
                    }
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    readData(conn, out, httpCallback);
                    callbackResult(httpCallback, conn.getResponseCode(), out.toByteArray());
                } catch (Exception e) {
                    callbackException(httpCallback, e);
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });
    }

    private String encode(String value) throws Exception {
        return value;
//        return URLEncoder.encode(value, "UTF-8");
    }

    public void get(final String _url, final Map<String, String> params, final HttpCallback<byte[]> httpCallback) {
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    String url = appendGetUrl(_url, params);
                    conn = initConnection(url, METHOD_GET, CONN_TIMEOUT, READ_TIMEOUT, null);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    readData(conn, out, httpCallback);
                    callbackResult(httpCallback, conn.getResponseCode(), out.toByteArray());
                } catch (Exception e) {
                    callbackException(httpCallback, e);
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });
    }

    public void downLoadFile(final String _url, final File outFile, final Map<String, String> params, final HttpCallback<File> httpCallback) {
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    if (outFile == null) {
                        throw new Exception("outFile is null");
                    }
                    if (!outFile.getParentFile().exists()) {
                        outFile.getParentFile().mkdirs();
                    }
                    String url = appendGetUrl(_url, params);
                    conn = initConnection(url, METHOD_GET, CONN_TIMEOUT, READ_TIMEOUT, null);
                    OutputStream out = new FileOutputStream(outFile);
                    readData(conn, out, httpCallback);
                    if (conn.getResponseCode() == 200) {
                        callbackResult(httpCallback, conn.getResponseCode(), outFile);
                    } else {
                        callbackResult(httpCallback, conn.getResponseCode(), null);
                    }
                } catch (Exception e) {
                    callbackException(httpCallback, e);
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });
    }

    public void fileUpload(final String url, final Map<String, String> params, final Map<String, FileItem> fileParams, final HttpCallback<byte[]> httpCallback) {

        String boundaryBody = getRandomString(10);
        final String boundary = boundaryStart + boundaryBody;
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", FILE_UPLOAD_CONTENT_TYPE + boundary.substring(2));
        getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    conn = initConnection(url, METHOD_POST, CONN_TIMEOUT, READ_TIMEOUT, headers);
                    OutputStream out = conn.getOutputStream();

                    writeFileParams(out, fileParams, boundary, null);

                    byte[] paramData = getStringParamsBytes(params, boundary);
                    if (paramData != null) {
                        out.write(paramData);
                        out.flush();
                    }
                    String endString = boundary + boundaryEnd + "\r\n\r\n";
                    out.write(endString.getBytes());
                    out.flush();
                    out.close();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    readData(conn, baos, httpCallback);
                    callbackResult(httpCallback, conn.getResponseCode(), baos.toByteArray());
                } catch (Exception e) {
                    callbackException(httpCallback, e);
                } finally {
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });

    }

    private void writeFileParams(OutputStream out, Map<String, FileItem> fileparams, String boundary, HttpCallback<byte[]> callback) throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            FileItem value = fileparams.get(name);

            StringBuilder sb = new StringBuilder();
            sb.append(boundary).append("\r\n").append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"")
                    .append((value.getFileName() == null || "".equals(value.getFileName().trim())) ? value.getFile().getName() : value.getFileName()).append("\"\r\n").append("Content-Type: ")
                    .append(getContentType(value.getFile())).append("\r\n").append("\r\n");

            out.write(sb.toString().getBytes("utf-8"));
            out.flush();
            InputStream in = new FileInputStream(value.getFile());
            int count;
            byte[] buf = new byte[1024 * 3];
            long writenCount = 0;
            long totalCount = value.getFile().length();
            while ((count = in.read(buf)) != -1) {
                out.write(buf, 0, count);
                writenCount += count;
                if (callback != null)
                    callback.onProgress(writenCount, totalCount);
                if (writenCount % (1024 * 300) == 0)
                    out.flush();
            }
            out.write("\r\n".getBytes("utf-8"));
            out.flush();
        }
    }

    // 普通字符串数据
    private byte[] getStringParamsBytes(Map<String, String> textParams, String boundary) throws Exception {
        if (textParams == null || textParams.isEmpty())
            return null;
        Set<String> keySet = textParams.keySet();
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = textParams.get(name);
            sb.append(boundary).append("\r\n").append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n").append("\r\n").append(value + "\r\n");
        }
        return sb.toString().getBytes("utf-8");
    }

    private byte[] getParamsBytes(Map<String, String> params) throws Exception {
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                sb.append(encode(entry.getKey())).append("=").append(encode(entry.getValue())).append("&");
            }
            if (sb.length() != 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString().getBytes();
        }
        return null;
    }

    private String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @param url
     * @param httpMethod
     * @param connTimeout 默认为传-1
     * @param readTimeout 默认为传-1
     * @param header
     * @return
     * @throws Exception
     */
    private HttpURLConnection initConnection(String url, String httpMethod, int connTimeout, int readTimeout, Map<String, String> header) throws Exception {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        if (connTimeout > 0)
            conn.setConnectTimeout(connTimeout);
        if (readTimeout > 0)
            conn.setReadTimeout(readTimeout);
        conn.setRequestMethod(httpMethod);
        if (header != null && !header.isEmpty()) {
            Iterator<Entry<String, String>> it = header.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                conn.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.connect();
        return conn;
    }

    private void readData(HttpURLConnection conn, OutputStream outStream, HttpCallback callback) throws Exception {
        try {
            InputStream in = conn.getInputStream();
            int totalCount = conn.getContentLength();
            byte[] buf = new byte[1024 * 3];
            int count;
            int writenCount = 0;
            while ((count = in.read(buf)) != -1) {
                outStream.write(buf, 0, count);
                writenCount += count;
                callbackProgress(callback, writenCount, totalCount);
            }
            outStream.flush();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String appendGetUrl(String url, Map<String, String> params) throws Exception {
        if (params != null && !params.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, String>> it = params.entrySet().iterator();
            if (url.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                sb.append(encode(entry.getKey())).append("=").append(encode(entry.getValue())).append("&");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
                url = url + sb.toString();
            }
        }
        return url;
    }

    private String getContentType(File file) {
        return "application/octet-stream";
    }

    private void callbackResult(final HttpCallback callback, final int statusCode, final Object obj) {
        try {
            if(obj instanceof  byte[]){
                Log.i("HttpClient","HttpClient::onResult::statusCode:"+statusCode+";data:"+new String((byte[])obj,"utf-8"));
            }else{
                Log.i("HttpClient","HttpClient::onResult::statusCode:"+statusCode+";data:"+obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (callback == null) return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                DialogUtil.cancelLoading();
                callback.onResult(statusCode, obj);
            }
        });
    }

    private void callbackException(final HttpCallback callback, final Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        Log.i("HttpClient","HttpClient::onException::Exception:"+sw.toString());
        pw.close();
        if (callback == null) return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                DialogUtil.cancelLoading();
                callback.onException(e);
            }
        });
    }

    private void callbackProgress(final HttpCallback callback, final long writencount, final long totalcount) {
        Log.i("HttpClient","HttpClient::onProgress::writencount:"+writencount+";totalcount:"+totalcount);
        if (callback == null) return;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                DialogUtil.cancelLoading();
                callback.onProgress(writencount, totalcount);
            }
        });
    }
}
