package com.chinaums.opensdk.util;

import android.os.Bundle;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class UmsConvertUtil {

    public static Map<String, Integer> convertString2Map(String str) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] strArray = UmsStringUtils.isBlank(str) ? null : str.split(",");
        if (strArray == null)
            return map;
        int index = 0;
        for (String code : strArray) {
            map.put(code, index++);
        }
        return map;
    }

    public static Map<String, Object> convertBundle2Map(Bundle bundle) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            if (bundle == null) return map;
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return map;
    }

    public static Set<String> convertString2SortSet(String str) {
        Set<String> set = new LinkedHashSet<String>();
        String[] strArray = UmsStringUtils.isBlank(str) ? null : str.split(",");
        if (strArray == null)
            return set;
        for (String code : strArray) {
            set.add(code);
        }
        return set;
    }

    public static String convertSet2String(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        if (set == null || set.isEmpty())
            return sb.toString();
        for (String str : set) {
            sb.append(str).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

}
