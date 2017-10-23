package com.chinaums.opensdk.cons;

import com.chinaums.opensdk.cons.OpenConst.Environment;


public enum OpenEnvironment {

    PROD(Environment.PROD), UAT(Environment.UAT), TEST(Environment.TEST);

    private String value;

    private OpenEnvironment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
