package com.chinaums.opensdk.cons;

public enum DynamicResourceWorkspace {

    SpaceA("bizspacea"), SpaceB("bizspaceb");

    private final String value;

    DynamicResourceWorkspace(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
