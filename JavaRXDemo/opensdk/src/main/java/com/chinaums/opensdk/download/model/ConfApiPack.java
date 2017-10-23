package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;

public class ConfApiPack extends ConfPack {

    /**
     * apiJsStr
     */
    private String apiJsStr = "";

    @Override
    protected boolean initPack() throws Exception {
        boolean success = false;
        String apiStr = UmsFileUtils.readFile2String(getResProcessPath()
                + File.separator
                + DynamicDownloadConf.CONF_API_PROCESS_FILE_NAME);
        if (UmsStringUtils.isNotBlank(apiStr)) {
            this.apiJsStr = apiStr;
            success = true;
        }
        return success;
    }

    public final String getApiJsStr() {
        return apiJsStr;
    }

}
