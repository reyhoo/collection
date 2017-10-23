package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 分享API
 */
public class PageShowShareViewProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(data
                        .getStringExtra("result"))));
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_PAGE_SHOWSHAREVIEW;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        final DynamicAPICallback callback = new DynamicAPICallback() {
            @Override
            public void onAPICallback(int resultCode, Intent data) {
                try {
                    onCallback(model, resultCode, data);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        };
        final String bizCode = ((AbsBizWebView) model.getWebView())
                .getBasePack().getCode();
        model.getHandler().post(new Runnable() {
            public void run() {
                try {
                    PageShareViewRequestModel requestModel = (PageShareViewRequestModel) model
                            .getRequestModel();
                    if ("showShareView".equals(requestModel.getAction())) {
                        OpenDelegateManager
                                .getProcessDelegate()
                                .getOpenDynamicProcessorDelegate()
                                .showShareView(
                                        model.getActivity(),
                                        JsonUtils
                                                .convertObject2String(requestModel),
                                        callback, getType(), bizCode);
                    } else if ("showShare".equals(requestModel.getAction())) {
                        OpenDelegateManager
                                .getProcessDelegate()
                                .getOpenDynamicProcessorDelegate()
                                .showShare(
                                        model.getActivity(),
                                        JsonUtils
                                                .convertObject2String(requestModel),
                                        callback, getType(), bizCode);
                    }
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new PageShareViewRequestModel(model.getRequestObj());
    }

    public class PageShareViewRequestModel extends AbsWebRequestModel {

        /**
         * 分享类型，必填。0-展示分享选择框，1-短信分享，2-微信好友，3-微信朋友圈，4-腾讯微博，5-新浪微博。
         */
        private int shareType;

        /**
         * 业务状态，必填，两位数字字符串，与后台配置一致即可，如00。
         */
        private String procStatus;

        /**
         * 分享方式0-展示分享列表 1-短信分享 2-朋友圈分享 3-腾讯微博分享 4-新浪微博分享
         */
        private int mode;

        /**
         * 额外参数，根据不同分享类型配置不同内容：比如shareType==1时，内容为师徒暗号
         */
        private String cipherNo;

        /**
         * 分享内容填充参数，选填，多个参数之间用----分隔。
         */
        private String params;

        /**
         * 跳转链接，选填。该字段不为空时，客户端忽略从后台获取的跳转链接，取用该字段值
         */
        private String shareUrl;

        /**
         * 图片链接，选填。该字段不为空时，客户端忽略从后台获取的图片链接，取用该字段值
         */
        private String picPath;

        /**
         * 分享标题，选填。该字段不为空时，客户端忽略从后台获取的分享标题，取用该字段值
         */
        private String title;

        /**
         * 分享内容，选填。该字段不为空时，客户端忽略从后台获取的分享内容，取用该字段值
         */
        private String context;

        /**
         * 根据action调showShare/showShareView
         */
        private String action;

        public PageShareViewRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            JSONObject data = getRequest().getJSONObject("data");
            procStatus = data.getString("procStatus");
            shareType = data.getIntValue("shareType");
            mode = data.getIntValue("mode");
            action = getRequest().getJSONObject("info").getString("action");
            if (data.containsKey("shareUrl")) {
                shareUrl = data.getString("shareUrl");
            }
            if (data.containsKey("picPath")) {
                picPath = data.getString("picPath");
            }
            if (data.containsKey("title")) {
                title = data.getString("title");
            }
            if (data.containsKey("context")) {
                context = data.getString("context");
            }
            cipherNo = data.getString("cipherNo");
            params = data.getString("params");
        }

        public int getShareType() {
            return this.shareType;
        }

        public void setShareType(int shareType) {
            this.shareType = shareType;
        }

        public String getProcStatus() {
            return this.procStatus;
        }

        public void setProcStatus(String procStatus) {
            this.procStatus = procStatus;
        }

        public int getMode() {
            return this.mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getCipherNo() {
            return this.cipherNo;
        }

        public String getParams() {
            return this.params;
        }

        public void setCipherNo(String cipherNo) {
            this.cipherNo = cipherNo;
        }

        public String getShareUrl() {
            return this.shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getPicPath() {
            return this.picPath;
        }

        public void setPicPath(String picPath) {
            this.picPath = picPath;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContext() {
            return this.context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getAction() {
            return action;
        }
    }

}
