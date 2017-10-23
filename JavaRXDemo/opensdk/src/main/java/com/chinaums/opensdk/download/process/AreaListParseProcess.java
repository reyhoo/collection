package com.chinaums.opensdk.download.process;

import com.chinaums.opensdk.download.model.AreaListPack.Area;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class AreaListParseProcess {

    private static AreaListParseProcess instance;

    public static AreaListParseProcess getInstance() {
        if (instance == null) {
            instance = new AreaListParseProcess();
        }
        return instance;
    }

    private AreaListParseProcess() {

    }

    public String parseFileToStr(String filePath) throws Exception {
        String str = UmsFileUtils.readFile2String(filePath);
        return str;
    }

    public List<Area> parseJsontoArea(String filePath) throws Exception {
        String jsonStr = parseFileToStr(filePath);
        Gson gson = new Gson();
        List<Area> areas = gson.fromJson(jsonStr, new TypeToken<List<Area>>() {
        }.getType());
        return areas;
    }
    
}