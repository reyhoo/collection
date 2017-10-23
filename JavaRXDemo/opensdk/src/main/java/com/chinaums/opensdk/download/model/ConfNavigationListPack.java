package com.chinaums.opensdk.download.model;

import android.annotation.SuppressLint;

import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.chinaums.opensdk.util.UmsStringUtils;
import com.chinaums.opensdk.util.UriUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;


@SuppressLint("DefaultLocale")
public class ConfNavigationListPack extends ConfPack {

    /**
     * navigation
     */
    private Navigation navigation = null;

    @Override
    protected boolean initPack() throws Exception {
        boolean success = false;
        String navigationStr = UmsFileUtils
                .readFile2String(getResProcessPath() + File.separator
                        + DynamicDownloadConf.CONF_NAVIGATION_PROCESS_FILE_NAME);
        if (UmsStringUtils.isNotBlank(navigationStr)) {
            Gson gson = new Gson();
            navigation = (Navigation) gson.fromJson(navigationStr,
                    Navigation.class);
            success = true;
        }
        return success;
    }

    public boolean isBlackUrl(String url) throws Exception {
        if (UmsStringUtils.isBlank(url)) {
            throw new Exception("访问地址为空");
        } else if (navigation == null) {
            throw new Exception("黑名单对象初始化失败");
        } else if (navigation.getBlackList() == null
                || navigation.getBlackList().isEmpty()) {
            return false;
        }
        return isBlackUrl(navigation.getBlackList(), url);
    }

    private boolean isBlackUrl(List<String> blackList, String url)
            throws Exception {
        url = UriUtils.decode(url, true);
        if (UmsStringUtils.isBlank(url)) return false;
        for (String str : blackList) {
            if (UmsStringUtils.isBlank(str)) continue;
            if (url.matches(str)) {
                return true;
            }
        }
        return false;
    }

    public class Navigation {

        /**
         * 黑名单列表
         */
        private List<String> blackList;

        public List<String> getBlackList() {
            return blackList;
        }

        public void setBlackList(List<String> blackList) {
            this.blackList = blackList;
        }
    }

}
