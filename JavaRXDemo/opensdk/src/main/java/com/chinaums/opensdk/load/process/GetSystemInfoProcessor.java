package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.manager.OpenDelegateDefined.Client;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 类 SystemInfoProcessor 的实现描述：得到系统信息API
 */
public class GetSystemInfoProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_SYSTEM_INFO;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        Client client = OpenDelegateManager.getProcessDelegate()
                .getClient();
        SysInfo sysInfo = new SysInfo();
        sysInfo.setType(client.getType().toString());
        sysInfo.setVersion(client.getVersion());
        sysInfo.setSysCode(client.getSysCode());
        sysInfo.setImei(client.getImei());
        sysInfo.setImsi(client.getImsi());
        sysInfo.setScreenOrientation(client.getScreenOrientation());
        sysInfo.setScreenDisplayWidth(client.getScreenDisplayWidth());
        sysInfo.setScreenDisplayHeight(client.getScreenDisplayHeight());
        sysInfo.setBoxId(client.getBoxId());
        sysInfo.setAppName(client.getAppName());
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(sysInfo)));
    }

    private class SysInfo {

        /**
         * 客户端类型，取值为androidphone、iphone、winphone、androidpad、ipad
         */
        private String type;

        /**
         * 当前客户端版本号
         */
        private String version;

        /**
         * 客户端系统编码，如无为空
         */
        private String sysCode;

        /**
         * 设备imei
         */
        private String imei;

        /**
         * 设备imsi
         */
        private String imsi;

        /**
         * landscape为横屏,portrait为竖屏
         */
        private String screenOrientation;

        /**
         * 宽度分辨率，px为单位
         */
        private String screenDisplayWidth;

        /**
         * 高度分辨率，px为单位
         */
        private String screenDisplayHeight;

        /**
         * 绑定的设备号
         */
        private String boxId;

        /**
         * 客户端业务类型
         */
        private String appName;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getScreenOrientation() {
            return screenOrientation;
        }

        public void setScreenOrientation(String screenOrientation) {
            this.screenOrientation = screenOrientation;
        }

        public String getScreenDisplayWidth() {
            return screenDisplayWidth;
        }

        public void setScreenDisplayWidth(String screenDisplayWidth) {
            this.screenDisplayWidth = screenDisplayWidth;
        }

        public String getScreenDisplayHeight() {
            return screenDisplayHeight;
        }

        public void setScreenDisplayHeight(String screenDisplayHeight) {
            this.screenDisplayHeight = screenDisplayHeight;
        }

        public String getBoxId() {
            return boxId;
        }

        public void setBoxId(String boxId) {
            this.boxId = boxId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

    }

}
