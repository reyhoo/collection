package com.chinaums.opensdk.download.process;

import com.chinaums.opensdk.download.model.ClientUpdateListPack.ClientUpdateList;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.google.gson.Gson;

public class ClientUpdateListParseProcess {

    private static ClientUpdateListParseProcess instance;

    public static ClientUpdateListParseProcess getInstance() {
        if (instance == null) {
            instance = new ClientUpdateListParseProcess();
        }
        return instance;
    }

    private ClientUpdateListParseProcess() {

    }

    public String parseFileToStr(String filePath) throws Exception {
        String str = UmsFileUtils.readFile2String(filePath);
        return str;
    }

    public ClientUpdateList parseJsontoClientUpdateList(String filePath)
            throws Exception {
        String jsonStr = parseFileToStr(filePath);
        Gson gson = new Gson();
        ClientUpdateList clientUpdateList = gson.fromJson(jsonStr,
                ClientUpdateList.class);
        return clientUpdateList;
    }

}