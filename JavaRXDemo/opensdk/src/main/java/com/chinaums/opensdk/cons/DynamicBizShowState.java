package com.chinaums.opensdk.cons;


public enum DynamicBizShowState {

    UNKNOWN, UNAVAILABLE, NORMAL, UPDATABLE, HOT, ACTIVE, NEW;

    private static String DYNAMIC_BIZ_STATE_HOT = "hot";
    private static String DYNAMIC_BIZ_STATE_NEW = "new";

    public static DynamicBizShowState getByState(String state) {
        if (state == null) {
            return NORMAL;
        } else if (DYNAMIC_BIZ_STATE_NEW.equalsIgnoreCase(state)) {
            return NEW;
        } else if (DYNAMIC_BIZ_STATE_HOT.equalsIgnoreCase(state)) {
            return HOT;
        } else {
            return NORMAL;
        }
    }

}
