package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.download.process.ClientUpdateListParseProcess;
import com.chinaums.opensdk.manager.OpenConfigManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;

import java.io.File;

@SuppressWarnings("serial")
public class ClientUpdateListPack extends AbsListPack {

    public ClientUpdateListPack(DynamicResourceWorkspace resourceWorkspace) {
        super(resourceWorkspace);
    }

    /**
     * clientUpdateList
     */
    private ClientUpdateList clientUpdateList;

    @Override
    protected String getPreloadResSignForUpdateSign() throws Exception {
        return OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getClientUpdateSign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        return OpenDynamicBizHistoryManager.getInstance()
                .getResourceSignHistory().getClientUpdateSign();
    }

    @Override
    protected void updateSignHistory(String sign) throws Exception {
        OpenDynamicBizHistoryManager.getInstance().getResourceSignHistory()
                .setClientUpdateSign(sign);
    }

    @Override
    protected String getSignUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_CLIENT_UPDATE_SIGN_URL);
    }

    @Override
    public String getResUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_CLIENT_UPDATE_URL);
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [应用升级列表] " + msg;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.CLIENTUPDATE_ORIGINAL_FOLDER;
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.CLIENTUPDATE_FOLDER;
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.CLIENTUPDATE_PROCESS_FOLDER;
    }

    @Override
    protected String getResOriginalFileName() {
        return DynamicDownloadConf.CLIENTUPDATE_ORIGINAL_FILE_NAME;
    }

    @Override
    protected String getResProcessFileName() {
        return DynamicDownloadConf.CLIENTUPDATE_PROCESS_FILE_NAME;
    }

    @Override
    protected boolean initPack() throws Exception {
        clientUpdateList = ClientUpdateListParseProcess.getInstance()
                .parseJsontoClientUpdateList(
                        getResProcessPath() + File.separator
                                + getResProcessFileName());
        return true;
    }

    public ClientUpdateList getClientUpdateList() {
        return clientUpdateList;
    }

    /**
     * 客户端升级表
     */
    public static class ClientUpdateList {

        /**
         * ANDROIDPHONE
         */
        private ClientUpdateRange ANDROIDPHONE;

        /**
         * IPHONE
         */
        private ClientUpdateRange IPHONE;

        /**
         * ANDROIDPAD
         */
        private ClientUpdateRange ANDROIDPAD;

        /**
         * IPAD
         */
        private ClientUpdateRange IPAD;

        public ClientUpdateRange getANDROIDPHONE() {
            return ANDROIDPHONE;
        }

        public void setANDROIDPHONE(ClientUpdateRange aNDROIDPHONE) {
            ANDROIDPHONE = aNDROIDPHONE;
        }

        public ClientUpdateRange getIPHONE() {
            return IPHONE;
        }

        public void setIPHONE(ClientUpdateRange iPHONE) {
            IPHONE = iPHONE;
        }

        public ClientUpdateRange getANDROIDPAD() {
            return ANDROIDPAD;
        }

        public void setANDROIDPAD(ClientUpdateRange aNDROIDPAD) {
            ANDROIDPAD = aNDROIDPAD;
        }

        public ClientUpdateRange getIPAD() {
            return IPAD;
        }

        public void setIPAD(ClientUpdateRange iPAD) {
            IPAD = iPAD;
        }

    }

    public static class ClientUpdateRange {

        /**
         * max
         */
        private ClientUpdate max;

        /**
         * min
         */
        private ClientUpdate min;

        public ClientUpdate getMax() {
            return max;
        }

        public void setMax(ClientUpdate max) {
            this.max = max;
        }

        public ClientUpdate getMin() {
            return min;
        }

        public void setMin(ClientUpdate min) {
            this.min = min;
        }

    }

    public static class ClientUpdate {

        /**
         * clientVersion
         */
        private String clientVersion;

        /**
         * clientVersionCode
         */
        private String clientVersionCode;

        /**
         * description
         */
        private String description;

        /**
         * downloadUrl
         */
        private String downloadUrl;

        public String getClientVersion() {
            return clientVersion;
        }

        public void setClientVersion(String clientVersion) {
            this.clientVersion = clientVersion;
        }

        public String getClientVersionCode() {
            return clientVersionCode;
        }

        public void setClientVersionCode(String clientVersionCode) {
            this.clientVersionCode = clientVersionCode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

    }
}