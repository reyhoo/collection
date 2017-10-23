package com.chinaums.opensdk.manager;

import android.content.Context;
import android.webkit.WebView;

import com.chinaums.opensdk.cons.OpenConst.CHAR;
import com.chinaums.opensdk.cons.OpenConst.UmsOpenCore;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import org.apache.commons.lang3.RandomStringUtils;

public class OpenEnvManager implements IOpenManager {

    private final static String UUID = "uuid"; // 客户端唯一标识
    private final static int UUID_LENGTH = 32; // UUID串码的长度

    /**
     * instance
     */
    private static OpenEnvManager instance;

    /**
     * userAgent
     */
    private String userAgent;

    /**
     * uuid
     */
    private String uuid;

    /**
     * clientInfo
     */
    private IOpenClientInfo clientInfo;

    synchronized public static OpenEnvManager getInstance() {
        if (instance == null) {
            instance = new OpenEnvManager();
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        uuid = getUuid(context);
        clientInfo = getClientInfo(context);
        userAgent = getUserAgent(context);
    }

    @Override
    public void destroy() {
        userAgent = null;
        uuid = null;
        clientInfo = null;
        instance = null;
    }

    /**
     * 获取当前设备的UUID
     */
    public static String getUuid() {
        return getInstance().uuid;
    }

    /**
     * 获取浏览器信息
     */
    public static String getNetUserAgent() {
        return getInstance().userAgent;
    }

    /**
     * 得到客户端信息
     */
    public static IOpenClientInfo getClientInfo() {
        return getInstance().clientInfo;
    }

    /**
     * 得到浏览器的用户代理信息
     */
    private String getUserAgent(Context context) {
        StringBuilder builder = null;
        WebView view = null;
        try {
            builder = new StringBuilder();
            view = new WebView(context);
            String webViewUserAgent = view.getSettings().getUserAgentString();
            if (UmsStringUtils.isBlank(webViewUserAgent)) {
                return null;
            }
            builder.append(webViewUserAgent).append(CHAR.SPACE)
                    .append(UmsOpenCore.UMS_OPEN_CORE);
            builder.append(CHAR.SPACE).append(clientInfo.getClientId())
                    .append(CHAR.SLASH).append(clientInfo.getClientType())
                    .append(CHAR.SLASH).append(clientInfo.getClientVersion());
            return builder.toString();
        } catch (Exception e) {
            UmsLog.e("", e);
        } finally {
            try {
                if (view != null) {
                    view.destroy();
                }
            } catch (Exception e2) {
                UmsLog.e("", e2);
            }
        }
        return null;
    }

    /**
     * 生成当前应用的UUID
     */
    private String getUuid(Context context) {
        String uuid = OpenHistoryDataManager.getHistoryStringData(UUID, null);
        if (UmsStringUtils.isBlank(uuid)) {
            uuid = RandomStringUtils.randomNumeric(UUID_LENGTH);
            OpenHistoryDataManager.setHistoryData(UUID, uuid);
        }
        return uuid;
    }

    /**
     * 得到客户端信息
     */
    private IOpenClientInfo getClientInfo(Context context) {
        try {
            final String clientId = OpenDelegateManager.getProcessDelegate()
                    .getClient().getSysCode();
            final String clientType = OpenDelegateManager.getProcessDelegate()
                    .getClient().getType().toString();
            final String clientVersion = OpenDelegateManager
                    .getProcessDelegate().getClient().getVersion();
            return new IOpenClientInfo() {

                @Override
                public String getClientVersion() {
                    return clientVersion;
                }

                @Override
                public String getClientType() {
                    return clientType;
                }

                @Override
                public String getClientId() {
                    return clientId;
                }
            };
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }
}
