package com.chinaums.opensdk.net.http;

import com.chinaums.opensdk.cons.OpenConst.HttpHeaders;
import com.chinaums.opensdk.manager.OpenEnvManager;
import com.chinaums.opensdk.net.IProgressUpdate;
import com.chinaums.opensdk.net.http.HttpRequest.RequestMethod;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class HttpTransporter implements IHttpTransport {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int SOCKET_BUFFER_SIZE = 8192;

    @Override
    public com.chinaums.opensdk.net.http.HttpResponse doHttpRequest(
            HttpRequest request, int timeout, IProgressUpdate progress)
            throws Exception {
        HttpClient httpClient = null;
        HttpUriRequest httpRequest = null;
        InputStream is = null;
        try {
            httpRequest = getHttpUriRequest(request);
            HttpParams httpParams = getHttpParams(timeout);

            httpClient = new DefaultHttpClient(httpParams);

            HttpResponse httpResponse = httpClient.execute(httpRequest);
            // 请求成功，进度50
            try {
                if (progress != null)
                    progress.onProgressUpdate(50);
            } catch (Exception e) {
            }

            com.chinaums.opensdk.net.http.HttpResponse response = new com.chinaums.opensdk.net.http.HttpResponse();
            response.status = httpResponse.getStatusLine().getStatusCode();

            Header[] headers = httpResponse.getAllHeaders();
            for (Header header : headers) {
                response.headers.put(header.getName(), header.getValue());
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            int contentLength = (int) httpEntity.getContentLength();
            if (contentLength > 0) {
                is = httpEntity.getContent();
                response.payload = readStream(is, contentLength, progress);
            } else {
                response.payload = new byte[0];
            }
            try {
                if (progress != null)
                    progress.onProgressUpdate(100);
            } catch (Exception e) {
            }
            return response;
        } finally {
            try {
                if (is != null)
                    is.close();
                if (httpRequest != null)
                    httpRequest.abort();
                if (httpClient != null)
                    httpClient.getConnectionManager().shutdown();
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }
    }

    private HttpUriRequest getHttpUriRequest(HttpRequest request) {
        HttpUriRequest httpRequest = null;

        if (StringUtils.isBlank(request.url))
            throw new IllegalArgumentException("没有传URL参数");

        if (request.requestMethod == RequestMethod.Post)
            httpRequest = new HttpPost(request.url);
        else
            httpRequest = new HttpGet(request.url);

        if (request.contentType != null)
            httpRequest.setHeader("Content-Type",
                    request.contentType.getValue());
        if (!request.headers.containsKey(HttpHeaders.USER_AGENT)) {
            String userAgent = OpenEnvManager.getNetUserAgent();
            if (UmsStringUtils.isNotBlank(userAgent)) {
                request.headers.put(HttpHeaders.USER_AGENT, userAgent);
            }
        }

        for (Entry<String, String> entry : request.headers.entrySet()) {
            httpRequest.setHeader(entry.getKey(), entry.getValue());
        }

        if (httpRequest instanceof HttpGet)
            return httpRequest;

        if (request.payload != null) {
            ByteArrayEntity bae = new ByteArrayEntity(request.payload);
            ((HttpPost) httpRequest).setEntity(bae);
        } else {
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : request.params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
            UrlEncodedFormEntity uefEntity;
            try {
                uefEntity = new UrlEncodedFormEntity(formParams, HTTP.UTF_8);
                ((HttpPost) httpRequest).setEntity(uefEntity);
            } catch (UnsupportedEncodingException e) {
            }
        }

        return httpRequest;
    }

    private HttpParams getHttpParams(int timeout) {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);
        HttpConnectionParams
                .setSocketBufferSize(httpParams, SOCKET_BUFFER_SIZE);

        return httpParams;
    }

    private byte[] readStream(InputStream is, int contentLength,
                              IProgressUpdate progress) throws Exception {
        byte[] result = new byte[contentLength];
        int read = 0, pos = 0;
        while (pos < contentLength) {
            int len = contentLength - pos;
            if (len > 1024)
                len = 1024;
            read = is.read(result, pos, len);
            if (read < 0)
                break;
            pos += read;
            try {
                if (progress != null) {
                    int pv = 50 + (pos / contentLength) * 50;
                    progress.onProgressUpdate(pv);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }
}
