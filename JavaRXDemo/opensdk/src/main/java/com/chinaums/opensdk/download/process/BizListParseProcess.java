package com.chinaums.opensdk.download.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenConst;
import com.chinaums.opensdk.cons.OpenConst.DynamicBizName;
import com.chinaums.opensdk.download.model.AdsPack;
import com.chinaums.opensdk.download.model.BasePack;
import com.chinaums.opensdk.download.model.ConfApiLevelPack;
import com.chinaums.opensdk.download.model.ConfApiPack;
import com.chinaums.opensdk.download.model.ConfNavigationListPack;
import com.chinaums.opensdk.download.model.LargeIconPack;
import com.chinaums.opensdk.download.model.LocalAppPack;
import com.chinaums.opensdk.download.model.LocalBizPack;
import com.chinaums.opensdk.download.model.LocalWeexPack;
import com.chinaums.opensdk.download.model.RemoteWebPack;
import com.chinaums.opensdk.download.model.RemoteWeexPack;
import com.chinaums.opensdk.download.model.SharePack;
import com.chinaums.opensdk.download.model.StdIconPack;
import com.chinaums.opensdk.download.model.ThirdPartyAppPack;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态加载相关的数据转换工具
 */
public class BizListParseProcess {

    private static BizListParseProcess instance;

    public static BizListParseProcess getInstance() {
        if (instance == null) {
            instance = new BizListParseProcess();
        }
        return instance;
    }

    private BizListParseProcess() {

    }

    public String parseFileToStr(String filePath) throws Exception {
        String str = UmsFileUtils.readFile2String(filePath);
        return str;
    }

    public List<BasePack> parseFileToBasePacks(
            DynamicResourceWorkspace resourceWorkspace, String filePath)
            throws Exception {
        String jsonStr = parseFileToStr(filePath);
        JSONArray jsonArray = JSON.parseArray(jsonStr);
        List<BasePack> basePacks = parserJsonArrToBasePacks(resourceWorkspace,
                jsonArray);
        return basePacks;
    }

    /**
     * Json 转对象
     */
    private List<BasePack> parserJsonArrToBasePacks(
            DynamicResourceWorkspace resourceWorkspace, JSONArray jsonArr)
            throws Exception {
        List<BasePack> allPacks = new ArrayList<BasePack>();
        Gson gson = new Gson();
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonItem = jsonArr.getJSONObject(i);
            String bizType = jsonItem.getString("type");
            fillConfPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillSharePack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillAdsPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillBizPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillNativePack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillWebPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillWeexBizPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillRemoteWeexBizPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillAppPack(resourceWorkspace, bizType, gson, jsonItem, allPacks);
            fillStdIconPack(resourceWorkspace, bizType, gson, jsonItem,
                    allPacks);
            fillLargeIconPack(resourceWorkspace, bizType, gson, jsonItem,
                    allPacks);
        }
        return allPacks;
    }

    private void fillConfPack(DynamicResourceWorkspace resourceWorkspace,
                              String bizType, Gson gson, JSONObject jsonItem,
                              List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.CONFIG_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = null;
        if (DynamicBizName.CONF_NAVIGATION_LIST.equals(jsonItem
                .getString("code"))) {
            pack = gson.fromJson(jsonItem.toString(),
                    ConfNavigationListPack.class);
        } else if (DynamicBizName.CONF_API_LEVEL.equals(jsonItem
                .getString("code"))) {
            pack = gson.fromJson(jsonItem.toString(), ConfApiLevelPack.class);
        } else if (DynamicBizName.CONF_API.equals(jsonItem.getString("code"))) {
            pack = gson.fromJson(jsonItem.toString(), ConfApiPack.class);
        } else {
            pack = gson.fromJson(jsonItem.toString(), SharePack.class);
        }
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillSharePack(DynamicResourceWorkspace resourceWorkspace,
                               String bizType, Gson gson, JSONObject jsonItem,
                               List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.SHARE_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), SharePack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillAdsPack(DynamicResourceWorkspace resourceWorkspace,
                             String bizType, Gson gson, JSONObject jsonItem,
                             List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.ADS_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), AdsPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillBizPack(DynamicResourceWorkspace resourceWorkspace,
                             String bizType, Gson gson, JSONObject jsonItem,
                             List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), LocalBizPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillNativePack(DynamicResourceWorkspace resourceWorkspace,
                                String bizType, Gson gson, JSONObject jsonItem,
                                List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.NATIVE_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), LocalAppPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillWebPack(DynamicResourceWorkspace resourceWorkspace,
                             String bizType, Gson gson, JSONObject jsonItem,
                             List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.WEB_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), RemoteWebPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillWeexBizPack(DynamicResourceWorkspace resourceWorkspace,
                                 String bizType, Gson gson, JSONObject jsonItem,
                                 List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.WEEX_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), LocalWeexPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillRemoteWeexBizPack(DynamicResourceWorkspace resourceWorkspace,
                                       String bizType, Gson gson, JSONObject jsonItem,
                                       List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.WEEX_REMOTE_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), RemoteWeexPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillAppPack(DynamicResourceWorkspace resourceWorkspace,
                             String bizType, Gson gson, JSONObject jsonItem,
                             List<BasePack> allPacks) throws Exception {
        if (!OpenConst.DynamicBizType.APP_BIZ.equalsIgnoreCase(bizType))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(),
                ThirdPartyAppPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillStdIconPack(DynamicResourceWorkspace resourceWorkspace,
                                 String bizType, Gson gson, JSONObject jsonItem,
                                 List<BasePack> allPacks) throws Exception {
        if (!(OpenConst.DynamicBizType.BIZ.equalsIgnoreCase(bizType)
                || OpenConst.DynamicBizType.APP_BIZ
                .equalsIgnoreCase(bizType)
                || OpenConst.DynamicBizType.NATIVE_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEB_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEEX_REMOTE_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEEX_BIZ
                .equalsIgnoreCase(bizType)))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), StdIconPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

    private void fillLargeIconPack(DynamicResourceWorkspace resourceWorkspace,
                                   String bizType, Gson gson, JSONObject jsonItem,
                                   List<BasePack> allPacks) throws Exception {
        if (!(OpenConst.DynamicBizType.BIZ.equalsIgnoreCase(bizType)
                || OpenConst.DynamicBizType.APP_BIZ
                .equalsIgnoreCase(bizType)
                || OpenConst.DynamicBizType.NATIVE_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEB_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEEX_REMOTE_BIZ
                .equalsIgnoreCase(bizType) || OpenConst.DynamicBizType.WEEX_BIZ
                .equalsIgnoreCase(bizType)))
            return;
        BasePack pack = gson.fromJson(jsonItem.toString(), LargeIconPack.class);
        pack.setResourceWorkspace(resourceWorkspace);
        allPacks.add(pack);
    }

}