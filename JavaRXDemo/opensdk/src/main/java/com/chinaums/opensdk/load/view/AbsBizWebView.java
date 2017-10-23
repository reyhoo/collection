package com.chinaums.opensdk.load.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.activity.base.IDynamicBizActivity;
import com.chinaums.opensdk.cons.OpenConst.DynamicCommonConst;
import com.chinaums.opensdk.cons.OpenConst.DynamicDialogConst;
import com.chinaums.opensdk.download.listener.ResourceProcessListener;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.event.model.AbsUmsWebEvent;
import com.chinaums.opensdk.event.model.PageReloadEvent;
import com.chinaums.opensdk.load.callback.AbsStdDynamicWebCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebResponseModel;
import com.chinaums.opensdk.load.model.url.AbsUmsUrl;
import com.chinaums.opensdk.load.process.IDynamicProcessor;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.manager.OpenDialogManager;
import com.chinaums.opensdk.manager.OpenDynamicWebProcessorManager;
import com.chinaums.opensdk.util.Base64Utils;
import com.chinaums.opensdk.util.UmsEventBusUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@SuppressLint({"SetJavaScriptEnabled"})
public abstract class AbsBizWebView extends WebView {

    /**
     * 上下文
     */
    private final Context context;

    /**
     * from
     */
    private final AbsUmsUrl from;

    /**
     * to
     */
    private final AbsUmsUrl to;

    /**
     * needBackHome
     */
    private Boolean needBackHome;

    /**
     * backNum
     */
    private int backNum = 1;

    /**
     * backType
     */
    private String backType = "";

    /**
     * basePack
     */
    private final BasePack basePack;

    /**
     * dynamicModelMap
     */
    private final Map<Long, DynamicWebModel> dynamicModelMap = new ConcurrentHashMap<Long, DynamicWebModel>();

    /**
     * handler
     */
    private final Handler handler;

    /**
     * result
     */
    private JSONObject result;

    /**
     * api等级
     */
    private Integer apiLevel;

    /**
     * 是否全屏
     */
    private Boolean isFullscreen = false;

    /**
     * 是否显示底部导航
     */
    private Boolean isShowBottomToolbar = false;

    /**
     * 是否显示区域选择
     */
    private Boolean isShowArea = false;

    /**
     * APP自定义API
     */
    private final Map<Integer, IDynamicProcessor> extProcessorMap = new ConcurrentHashMap<Integer, IDynamicProcessor>();

    /**
     * APP注册action表
     */
    private final Map<Integer, List<String>> extActionMap = new ConcurrentHashMap<Integer, List<String>>();

    public AbsBizWebView(Context context, AbsUmsUrl to, AbsUmsUrl from,
                         BasePack basePack, Handler handler, Boolean needBackHome,
                         Boolean isFullscreen, Boolean isShowBottomToolbar,
                         Boolean isShowArea, Integer apiLevel) {
        super(context);
        this.context = context;
        this.from = from;
        this.to = to;
        this.basePack = basePack;
        this.handler = handler;
        this.needBackHome = needBackHome;
        this.isFullscreen = isFullscreen;
        this.isShowBottomToolbar = isShowBottomToolbar;
        this.isShowArea = isShowArea;
        this.apiLevel = (apiLevel == null || apiLevel.intValue() < 0) ? 0
                : apiLevel;
        extProcessorMap.clear();
        extActionMap.clear();
    }

    public abstract void config() throws Exception;

    /**
     * 动态加载引擎执行销毁处理
     */
    public abstract void handleDestroy();

    /**
     * 移除Processor
     */
    public void finishProcess(Long requestId) {
        dynamicModelMap.remove(requestId);
    }

    public void registerEvent() {
        UmsEventBusUtils.register(AbsBizWebView.this);
    }

    public void unRegisterEvent() {
        UmsEventBusUtils.unregister(AbsBizWebView.this);
    }

    public void back() {
        try {
            DynamicWebModel backModel = null;
            if (needBackHome != null && needBackHome.booleanValue()) {
                backModel = DynamicWebModel.getBackModel(-1, this,
                        (Activity) context, handler);
            } else {
                backModel = DynamicWebModel.getBackModel(backNum, this,
                        (Activity) context, handler);
            }
            backModel.getProcessor().process(backModel);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public void processActivityResult(int requestCode, int resultCode,
                                      Intent data) {
        try {
            Long requestId = null;
            if (DynamicCommonConst.ACTIVITY_REQUEST_CODE_API != requestCode) {
                requestId = getRequestIdByActivityResultCode(requestCode);
            } else {
                requestId = data.getLongExtra("requestId", 0);
            }
            if (requestId == null || requestId.longValue() <= 0)
                throw new Exception("requestId获取的不正确");
            DynamicWebModel model = dynamicModelMap.get(requestId.longValue());
            if (model == null)
                throw new Exception("model对象转换失败");
            finishProcess(requestId);
            model.getProcessor().onCallback(model, resultCode, data);
        } catch (Exception e) {
            UmsLog.e("", e);
        }
    }

    public void sendResume(DynamicWebModel model) {
        AbsWebRequestModel requestModel = model.getRequestModel();
        JSONObject requestObj = new JSONObject();
        String from = (model == null || requestModel.getRequest() == null) ? ""
                : requestModel.getRequest().getJSONObject("data")
                .getString("from");
        requestObj.put("from", from);
        // 关闭当前页面，并回退到指定页面后，如果还是本地资源包页面，则发出页面重新加载通知。
        UmsEventBusUtils.post(new PageReloadEvent(requestObj.toString()));
    }

    /**
     * 根据requestcode得到随机数
     */
    private Long getRequestIdByActivityResultCode(int requestCode)
            throws Exception {
        for (Entry<Long, DynamicWebModel> entry : dynamicModelMap.entrySet()) {
            DynamicWebModel model = entry.getValue();
            if (model != null && model.getProcessor().getType() == requestCode)
                return model.getId();
        }
        return null;
    }

    public void OpenDialogWarning(String msg) {
        OpenDialogWarning(msg, null);
    }

    public void OpenDialogWarning(String msg, JsResult result) {
        openDialog(
                null,
                DynamicDialogConst.DYNAMIC_DIALOG_WARNING_DEFAULT_TITLE,
                UmsStringUtils.isBlank(msg) ? DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_MSG
                        : msg,
                DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_BTN_NAME_ARRAY,
                result);
    }

    public void OpenDialogError(String msg) {
        openDialog(
                null,
                DynamicDialogConst.DYNAMIC_DIALOG_ERROR_TITLE,
                UmsStringUtils.isBlank(msg) ? DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_MSG
                        : msg,
                DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_BTN_NAME_ARRAY, null);
    }

    public void openDialog(final DynamicWebModel model, String title,
                           String msg, final String[] btnNames, final JsResult result) {
        final AtomicInteger callbackChoose = new AtomicInteger(0);
        OpenDialogManager.getInstance().showDialogWithTitle(context, true,
                title, msg, btnNames, callbackChoose, new Runnable() {

                    @Override
                    public void run() {
                        int choose = callbackChoose.get();
                        if (result != null && choose == 0) {
                            result.confirm();
                        } else if (result != null && choose == 1) {
                            result.cancel();
                        }
                        if (model != null)
                            try {
                                model.getProcessor().onCallback(
                                        model,
                                        -1,
                                        new Intent().putExtra("choose",
                                                choose - 1));
                            } catch (Exception e) {
                                UmsLog.e("", e);
                            }
                    }
                });
    }

    public void showSelect(final DynamicWebModel model, final String[] choices,
                           final int selectIndex, final CallWeb callWeb) {
        OpenDialogManager.getInstance().showSelect(model.getActivity(),
                model.getWebView(), choices, selectIndex, callWeb);
    }

    public void openInputPinDialog(Context context, String securityKeyType,
                                   String inputMinLength, String inputMaxLength, String inputType,
                                   String mainAccount, String calcFactor, DialogCallback callback) {
        OpenDialogManager.getInstance().showInputPinDialog(context,
                securityKeyType, inputMinLength, inputMaxLength, inputType,
                mainAccount, calcFactor, callback);
    }

    /**
     * showSelect函数点击事件后的具体处理
     */
    public interface CallWeb {

        public void onCallWeb(int position);
    }

    /**
     * 对话框获取内容回调
     */
    public interface DialogCallback {
        public void onCallback(Context context, String contentStr);
    }

    /**
     * 调用web回调
     */
    public boolean callWeb(DynamicWebModel model) {

        if (model.getProcessor().IsSynchronizedProcessor()) {
            return true;
        }
        try {
            finishProcess(model.getId());
            if (model.getResponseModel() == null)
                return false;

            final String callJavaScriptInfo = new AbsStdDynamicWebCallback() {

                @Override
                protected String getCallbackName(
                        AbsWebRequestModel requestModel,
                        AbsWebResponseModel responseModel) throws Exception {
                    return DynamicCommonConst.DEFAULT_WEB_CALLBACK_NAME;
                }
            }.getWebCallback(model.getRequestModel(), model.getResponseModel());

            UmsLog.i("js api callback================== {}", callJavaScriptInfo);

            final WebView webView = model.getWebView();
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(callJavaScriptInfo);
                }
            });
        } catch (Exception e) {
            UmsLog.e("处理完成后调用web端的callback时出错了", e);
            return false;
        }
        return true;
    }

    public final AbsUmsUrl getFrom() {
        return from;
    }

    public final AbsUmsUrl getTo() {
        return to;
    }

    public final BasePack getBasePack() {
        return basePack;
    }

    public final JSONObject getResult() {
        return result;
    }

    public final void setResult(JSONObject result) {
        this.result = result;
    }

    protected final Handler getDynamicHandler() {
        return handler;
    }

    public abstract void loadUrl(ResourceProcessListener listener)
            throws Exception;

    protected void loadData() throws Exception {
        final AbsBizWebView me = this;
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    me.loadProcess();
                } catch (Exception e) {
                    UmsLog.e("", e);
                    if (e instanceof ActivityNotFoundException) {
                        OpenDialogManager.getInstance().showHint(
                                getMyContext(), "跳转界面不存在");
                    } else {
                        OpenDialogManager.getInstance().showHint(
                                getMyContext(), "找不到对应的业务");
                    }
                    if (!(AbsBizWebView.this instanceof NativeWebView)) {
                        ((IDynamicBizActivity) getMyContext())
                                .onPageLoadException();
                    }
                }
            }
        });
    }

    protected abstract void loadProcess() throws Exception;

    public Boolean getNeedBackHome() {
        return needBackHome;
    }

    public void setNeedBackHome(Boolean needBackHome) {
        this.needBackHome = needBackHome;
    }

    protected Context getMyContext() {
        return context;
    }

    protected Handler getMyHandler() {
        return handler;
    }

    protected Map<Long, DynamicWebModel> getDynamicModelMap() {
        return dynamicModelMap;
    }

    public final void setBackNum(int backNum) {
        this.backNum = backNum;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public int getBackNum() {
        return backNum;
    }

    public final Boolean isFullscreen() {
        return isFullscreen;
    }

    public final Boolean isShowBottomToolbar() {
        return isShowBottomToolbar;
    }

    public final Boolean isShowArea() {
        return isShowArea;
    }

    public final Integer getApiLevel() {
        return apiLevel;
    }

    public IDynamicProcessor getDynamicProcessor(int processorType) {
        return getDynamicProcessor(processorType, "null");
    }

    public IDynamicProcessor getDynamicProcessor(int processorType,
                                                 String action) {
        IDynamicProcessor processor = OpenDynamicWebProcessorManager
                .getInstance().getDynamicProcessor(processorType);
        if (processor == null) {
            if (extProcessorMap.containsKey(processorType)
                    && extActionMap.containsKey(processorType)) {
                processor = extProcessorMap.get(processorType);
            } else {
                processor = null;
            }
        }
        return processor;
    }

    public RegisterStatus injectExtProcessorAndAction(int type, String action,
                                                      String reflectClazz) {

        if (UmsStringUtils.isBlank(reflectClazz)) {
            return RegisterStatus.PARAM_ERROR;
        }
        if (OpenDynamicWebProcessorManager.getInstance().getDynamicProcessor(
                type) != null) {
            return RegisterStatus.TYPE_REGISTERED;
        }
        IDynamicProcessor dynamicProcessor;
        try {
            Class<? extends IDynamicProcessor> clazz = OpenDelegateManager
                    .getProcessDelegate().getExtDynamicProcessorMap()
                    .get(reflectClazz);
            if (clazz == null) {
                return RegisterStatus.NOT_FOUND_REFLECTCLAZZ;
            }
            dynamicProcessor = clazz.newInstance();
            if (type != dynamicProcessor.getType()) {
                return RegisterStatus.PARAM_ERROR;
            }
            if (UmsStringUtils.isBlank(action)
                    || action.equalsIgnoreCase("default")) {
                action = "default";
            }
            extProcessorMap.put(dynamicProcessor.getType(), dynamicProcessor);
            List<String> actionList;
            if (extActionMap.containsKey(dynamicProcessor.getType())) {
                actionList = extActionMap.get(dynamicProcessor.getType());
            } else {
                actionList = new ArrayList<String>();
                extActionMap.put(dynamicProcessor.getType(), actionList);
            }
            if (actionList.contains(action)) {
                return RegisterStatus.ACTION_REGISTERED;
            }
            actionList.add(action);
            return RegisterStatus.SUCCESS;
        } catch (InstantiationException e) {
            UmsLog.e("", e);
            return RegisterStatus.OTHER_ERROR;
        } catch (IllegalAccessException e) {
            UmsLog.e("", e);
            return RegisterStatus.OTHER_ERROR;
        } catch (ClassNotFoundException e) {
            UmsLog.e("", e);
            return RegisterStatus.NOT_FOUND_REFLECTCLAZZ;
        } catch (Exception e) {
            UmsLog.e("", e);
            return RegisterStatus.OTHER_ERROR;
        }
    }

    @Override
    public void destroy() {
        extProcessorMap.clear();
        extActionMap.clear();
        super.destroy();
    }

    /**
     * 根据activity执行api的销毁方法
     */
    protected void handleDestoryExtProcessorByActivity(Activity activity)
            throws Exception {
        for (Entry<Integer, IDynamicProcessor> entry : extProcessorMap
                .entrySet()) {
            IDynamicProcessor processor = entry.getValue();
            UmsLog.d("执行type为【{}】的扩展processor撤销方法", entry.getKey());
            if (processor == null) {
                UmsLog.d("type为【{}】的扩展processor为空", entry.getKey());
                continue;
            }
            processor.onDestory(activity);
            UmsLog.d("type为【{}】的扩展processor的撤销方法", entry.getKey());
        }
    }

    public void onEventMainThread(AbsUmsWebEvent webEvent) {
        UmsLog.d("webEvent {}", webEvent.getParams());
        this.loadUrl("javascript:UmsApi.dispatchEvent('"
                + Base64Utils.encrypt(webEvent.getEventName()) + "','"
                + Base64Utils.encrypt(webEvent.getParams()) + "');");
    }

    /**
     * 注册自定义API的返回码
     */
    public static enum RegisterStatus {

        /**
         * 成功
         */
        SUCCESS,

        /**
         * 未找到reflectClazz映射
         */
        NOT_FOUND_REFLECTCLAZZ,

        /**
         * type已注册
         */
        TYPE_REGISTERED,

        /**
         * type下的action已注册
         */
        ACTION_REGISTERED,

        /**
         * 参数错误
         */
        PARAM_ERROR,

        /**
         * 其他错误
         */
        OTHER_ERROR,

    }

}
