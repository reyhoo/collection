package com.chinaums.opensdk.load.model.data;

import android.app.Activity;
import android.os.Handler;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.OpenConst.DynamicCommonConst;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;
import com.chinaums.opensdk.load.process.IDynamicProcessor;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;


public class DynamicWebModel {

    /**
     * id
     */
    private final long id = System.currentTimeMillis();

    /**
     * 请求JSON
     */
    private final JSONObject requestObj;

    /**
     * 请求对象
     */
    private AbsWebRequestModel requestModel;

    /**
     * 响应对象
     */
    private AbsWebResponseModel responseModel;

    /**
     * 浏览器控件
     */
    private final WebView webView;

    /**
     * 对应的activity类
     */
    private final Activity activity;

    /**
     * 处理类
     */
    private final IDynamicProcessor processor;

    /**
     * handler
     */
    private final Handler handler;

    public DynamicWebModel(JSONObject requestObj, IDynamicProcessor processor,
                           WebView webView, Activity activity, Handler handler) {
        this.requestObj = requestObj;
        this.processor = processor;
        this.webView = webView;
        this.activity = activity;
        this.handler = handler;
    }

    public static DynamicWebModel getModel(String param, WebView webView,
                                           Activity activity, Handler handler) throws Exception {
        DynamicWebModel model = null;
        AbsBizWebView view = (AbsBizWebView) webView;
        try {
            if (UmsStringUtils.isBlank(param))
                return model;
            JSONObject requestObj = JSON.parseObject(param);
            int type = requestObj.getJSONObject("info").getIntValue("type");
            IDynamicProcessor processor = view.getDynamicProcessor(type);
            if (processor == null)
                return model;
            model = new DynamicWebModel(requestObj, processor, webView,
                    activity, handler);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return model;
    }

    public static DynamicWebModel getBackModel(int backNum, WebView webView,
                                               Activity activity, Handler handler) throws Exception {
        DynamicWebModel model = null;
        AbsBizWebView view = (AbsBizWebView) webView;
        try {
            JSONObject requestObj = new JSONObject();
            JSONObject info = new JSONObject();
            JSONObject data = new JSONObject();
            info.put("type", DynamicProcessorType.NAVIGATOR_PAGE_BACK_3005);
            data.put("backNum", backNum);
            requestObj.put("info", info);
            requestObj.put("data", data);
            IDynamicProcessor processor = view
                    .getDynamicProcessor(DynamicProcessorType.NAVIGATOR_PAGE_BACK_3005);
            if (processor == null)
                return model;
            model = new DynamicWebModel(requestObj, processor, webView,
                    activity, handler);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return model;
    }

    public static DynamicWebModel getPageForwardModel(String url,
                                                      WebView webView, Activity activity, Handler handler)
            throws Exception {
        DynamicWebModel model = null;
        AbsBizWebView view = (AbsBizWebView) webView;
        try {
            if (UmsStringUtils.isBlank(url))
                return model;
            JSONObject requestObj = new JSONObject();
            JSONObject info = new JSONObject();
            JSONObject data = new JSONObject();
            info.put("type", DynamicProcessorType.NAVIGATOR_PAGE_FORWARD);
            data.put("url", url);
            requestObj.put("info", info);
            requestObj.put("data", data);
            IDynamicProcessor processor = view
                    .getDynamicProcessor(DynamicProcessorType.NAVIGATOR_PAGE_FORWARD);
            if (processor == null)
                return model;
            model = new DynamicWebModel(requestObj, processor, webView,
                    activity, handler);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return model;
    }

    public static boolean checkOldBizApiPlugin(String url) {
        return url.startsWith("objc");
    }

    public String getRequestId() {
        String ret = null;
        try {
            if (getRequestObj() == null)
                return ret;
            JSONObject info = getRequestObj().getJSONObject(
                    DynamicCommonConst.WEB_REQUEST_OBJ_FIELD_NAME_INFO);
            if (info == null)
                return ret;
            return info
                    .getString(DynamicCommonConst.WEB_REQUEST_OBJ_FIELD_NAME_REQUEST_ID);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ret;
    }

    public String getAction() {
        String ret = null;
        try {
            if (getRequestObj() == null)
                return ret;
            JSONObject info = getRequestObj().getJSONObject(
                    DynamicCommonConst.WEB_REQUEST_OBJ_FIELD_NAME_INFO);
            if (info == null)
                return ret;
            return info
                    .getString(DynamicCommonConst.WEB_REQUEST_OBJ_FIELD_NAME_REQUEST_ACTION);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return ret;
    }

    public final long getId() {
        return id;
    }

    public JSONObject getRequestObj() {
        return requestObj;
    }

    public IDynamicProcessor getProcessor() {
        return processor;
    }

    public WebView getWebView() {
        return webView;
    }

    public Activity getActivity() {
        return activity;
    }

    public AbsWebRequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(AbsWebRequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public AbsWebResponseModel getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(AbsWebResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    public final Handler getHandler() {
        return handler;
    }
    
}
