package com.chinaums.opensdk.cons;


public enum OpenOSType {

    ANDROID_PHONE("ANDROIDPHONE"), IPHONE("IPHONE"), WIN_PHONE("WINPHONE"), ANDROID_PAD(
            "ANDROIDPAD"), IPAD("IPAD");

    /**
     * osType
     */
    private String osType;
    
    OpenOSType(String osType) {
        this.osType = osType;
    }

    @Override
    public String toString() {
        return osType;
    }

}
