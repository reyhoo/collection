package com.chinaums.opensdk.download.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst.DynamicDownloadConf;
import com.chinaums.opensdk.data.model.ResourceSignHistory;
import com.chinaums.opensdk.download.process.BizListParseProcess;
import com.chinaums.opensdk.manager.OpenConfigManager;
import com.chinaums.opensdk.manager.OpenDynamicBizHistoryManager;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class BizListPack extends AbsListPack {

    /**
     * 列表信息
     */
    private transient List<BasePack> basePacks;

    public BizListPack(DynamicResourceWorkspace resourceWorkspace) {
        super(resourceWorkspace);
    }

    @Override
    protected String getPreloadResSignForUpdateSign() throws Exception {
        return OpenDynamicBizHistoryManager.getInstance().getResourcePreload()
                .getBizlistSign();
    }

    @Override
    protected String getHistoryResSign() throws Exception {
        return ResourceSignHistory.getBizlistSign();
    }

    @Override
    protected void updateSignHistory(String sign) throws Exception {
        ResourceSignHistory.setBizlistSign(sign);
    }

    @Override
    public boolean initPack() throws Exception {
        basePacks = BizListParseProcess.getInstance().parseFileToBasePacks(
                getResourceWorkspace(),
                getResProcessPath() + File.separator + getResProcessFileName());
        return true;
    }

    @Override
    protected String getResOriginalPathSuffix() {
        return DynamicDownloadConf.BIZ_LIST_ORIGINAL_FOLDER;
    }

    @Override
    protected String getPreloadResPath() throws Exception {
        return DynamicDownloadConf.BIZ_LIST_FOLDER;
    }

    @Override
    protected String getResProcessPathSuffix() {
        return DynamicDownloadConf.BIZ_LIST_PROCESS_FOLDER;
    }

    @Override
    protected String getResOriginalFileName() {
        return DynamicDownloadConf.BIZ_LIST_ORIGINAL_FILE_NAME;
    }

    @Override
    protected String getPrintLog(String msg) {
        return this.getClass().toString() + " [全业务列表] " + msg;
    }

    @Override
    protected String getSignUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_BIZLIST_SIGN_URL);
    }

    @Override
    public String getResUrl() {
        return OpenConfigManager
                .getProperty(OpenConfigManager.DOWNLOAD_FILE_BIZLIST_URL);
    }

    @Override
    protected String getResProcessFileName() {
        return DynamicDownloadConf.BIZ_LIST_PROCESS_FILE_NAME;
    }

    public final List<BasePack> getBasePacks() {
        return basePacks;
    }

    public BasePack getByClassTypeAndCode(Class<?> clazz, String code)
            throws Exception {
        for (BasePack pack : basePacks) {
            if (pack == null)
                continue;
            else if (clazz.isInstance(pack) && code.equals(pack.getCode()))
                return pack;
        }
        return null;
    }

    public List<? extends BasePack> getArrayByClassTypeAndCode(
            Class<? extends BasePack> clazz, String code) throws Exception {
        List<BasePack> packs = new ArrayList<BasePack>();
        for (BasePack pack : basePacks) {
            if (pack == null)
                continue;
            else if (clazz.isInstance(pack) && code.equals(pack.getCode()))
                packs.add(pack);
        }
        return packs;
    }

    public List<? extends BasePack> getArrayByClassType(
            Class<? extends BasePack> clazz) throws Exception {
        List<BasePack> packs = new ArrayList<BasePack>();
        for (BasePack pack : basePacks) {
            if (pack == null)
                continue;
            else if (clazz.isInstance(pack))
                packs.add(pack);
        }
        return packs;
    }

    public String getAdsCodes() throws Exception {
        String codes = "";
        for (BasePack pack : basePacks) {
            if (pack == null)
                continue;
            else if ("ad".equalsIgnoreCase(pack.getType())) {
                if (UmsStringUtils.isEmpty(codes)) {
                    codes += pack.getCode();
                } else {
                    codes += "," + pack.getCode();
                }
            }
        }
        return codes;
    }

    public String getDisplayBizCodes() throws Exception {
        String codes = "";
        List<? extends BasePack> packs = getArrayByClassType(BasePack.class);
        for (BasePack pack : packs) {
            if (pack == null)
                continue;
            if (UmsStringUtils.isEmpty(codes)) {
                codes += pack.getCode();
            } else {
                codes += "," + pack.getCode();
            }
        }
        return codes;
    }

}
