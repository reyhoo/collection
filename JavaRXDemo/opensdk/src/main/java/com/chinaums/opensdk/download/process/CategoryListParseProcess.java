package com.chinaums.opensdk.download.process;

import com.chinaums.opensdk.download.model.CategoryListPack.Category;
import com.chinaums.opensdk.util.UmsFileUtils;
import com.google.gson.Gson;

public class CategoryListParseProcess {

    private static CategoryListParseProcess instance;

    public static CategoryListParseProcess getInstance() {
        if (instance == null) {
            instance = new CategoryListParseProcess();
        }
        return instance;
    }

    private CategoryListParseProcess() {

    }

    public String parseFileToStr(String filePath) throws Exception {
        String str = UmsFileUtils.readFile2String(filePath);
        return str;
    }

    public Category parseJsontoCategory(String filePath) throws Exception {
        String jsonStr = parseFileToStr(filePath);
        Gson gson = new Gson();
        Category category = gson.fromJson(jsonStr, Category.class);
        return category;
    }
}