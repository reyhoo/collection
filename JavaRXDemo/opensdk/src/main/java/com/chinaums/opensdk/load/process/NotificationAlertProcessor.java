package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicDialogConst;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.load.view.AbsBizWebView;
import com.chinaums.opensdk.manager.OpenDialogManager;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * desc: 提示信息 创建日期：2014年9月13日 Copyright 2012　银联商务有限公司　All Rights Reserved
 */
public class NotificationAlertProcessor extends AbsStdDynamicProcessor {

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        try {
            NotificationAlertWebRequestModel requestModel = (NotificationAlertWebRequestModel) model
                    .getRequestModel();
            if (requestModel == null)
                throw new Exception("我去，怎么没有，肯定哪里错了.");
            else if (DynamicDialogConst.DYNAMIC_DIALOG_OPEN_TYPE_TOTAST
                    .equalsIgnoreCase(requestModel.getOpenType())) {
                OpenDialogManager.getInstance().showHint(
                        model.getActivity(), requestModel.getMsg());
                setRespAndCallWeb(model, createSuccessResponse(null));
                return;
            }
            openDialog(model, requestModel.getMsgType(),
                    requestModel.getTitle(), requestModel.getMsg(),
                    requestModel.getBtnNameArray());
        } catch (Exception e) {
            apiRunExceptionCallBack(model, e);
        }
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        NotificationAlertWebResponse response = new NotificationAlertWebResponse();
        response.selectIndex = String.valueOf(data.getIntExtra("choose", 0));
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(response)));
    }

    /**
     * 打开对话框
     */
    private void openDialog(final DynamicWebModel model, final String msgType,
                            final String title, final String msg, final String[] btnNames) {
        ((AbsBizWebView) (model.getWebView())).openDialog(model, title, msg,
                btnNames, null);
    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new NotificationAlertWebRequestModel(model.getRequestObj());
    }

    @Override
    public int getType() {
        return DynamicProcessorType.NAVIGATOR_NOTIFICATION_ALERT;
    }

    private class NotificationAlertWebRequestModel extends AbsWebRequestModel {

        /**
         * 提示类型（0=提示，1=警告，2=错误）
         */
        private String msgType;

        /**
         * 提示框中的主体内容
         */
        private String msg;

        /**
         * 操作事件，dialog/totast，默认为dialog(dialog：以对话框方式打开，该方式需要用户确认后才会关闭；
         * totast：以提示信息的方式打开，不需要用户确认就会消失，会忽略title和btnNames参数)
         */
        private String openType;

        /**
         * 提示框标题信息，如果没有传递，则根据msgType类型显示默认的提示，内容见msgType描述
         */
        private String title;

        /**
         * 以逗号分隔的按钮标题名称，如果为空，则默认为"确定"。
         */
        private String btnNames;

        public NotificationAlertWebRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                // 显示提示框类型 Toast或对话框
                openType = getRequest().getJSONObject("info").getString(
                        "action");
                msgType = data.getString("msgType");
                msg = data.getString("msg");
                title = data.getString("title");
                btnNames = data.getString("btnNames");

                msgType = UmsStringUtils.isBlank(msgType) ? DynamicDialogConst.DYNAMIC_DIALOG_MSG_TYPE_TIP
                        : msgType;
                if (UmsStringUtils.isBlank(title)) {
                    if (DynamicDialogConst.DYNAMIC_DIALOG_MSG_TYPE_TIP
                            .equals(msgType)) {
                        title = DynamicDialogConst.DYNAMIC_DIALOG_TIP_DEFAULT_TITLE;
                    } else if (DynamicDialogConst.DYNAMIC_DIALOG_MSG_TYPE_WARNING
                            .equals(msgType)) {
                        title = DynamicDialogConst.DYNAMIC_DIALOG_WARNING_DEFAULT_TITLE;
                    } else if (DynamicDialogConst.DYNAMIC_DIALOG_MSG_TYPE_ERROR
                            .equals(msgType)) {
                        title = DynamicDialogConst.DYNAMIC_DIALOG_ERROR_TITLE;
                    } else {
                        title = DynamicDialogConst.DYNAMIC_DIALOG_TIP_DEFAULT_TITLE;
                    }
                }
                msg = UmsStringUtils.isBlank(msg) ? DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_MSG
                        : msg;
                btnNames = UmsStringUtils.isBlank(btnNames) ? DynamicDialogConst.DYNAMIC_DIALOG_DEFAULT_BTN_NAME
                        : btnNames.trim();
                openType = UmsStringUtils.isBlank(openType)
                        || !DynamicDialogConst.DYNAMIC_DIALOG_OPEN_TYPE_DIALOG
                        .equalsIgnoreCase(openType) ? DynamicDialogConst.DYNAMIC_DIALOG_OPEN_TYPE_TOTAST
                        : openType.trim();
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getMsgType() {
            return msgType;
        }

        public String getMsg() {
            return msg;
        }

        public String getTitle() {
            return title;
        }

        public String[] getBtnNameArray() {
            return btnNames.split(",");
        }

        public final String getOpenType() {
            return openType;
        }

        public final String getBtnNames() {
            return btnNames;
        }
    }

    private class NotificationAlertWebResponse {

        /**
         * selectIndex为选中的索引
         */
        private String selectIndex;

        public String getSelectIndex() {
            return selectIndex;
        }

        public void setSelectIndex(String selectIndex) {
            this.selectIndex = selectIndex;
        }

    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

}
