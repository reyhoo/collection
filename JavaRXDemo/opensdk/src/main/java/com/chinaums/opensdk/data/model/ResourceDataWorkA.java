package com.chinaums.opensdk.data.model;

import com.chinaums.opensdk.cons.DynamicResourceWorkspace;

public class ResourceDataWorkA extends AbsResourceData {
    
    @Override
    public DynamicResourceWorkspace getResourceWorkspace() {
        return DynamicResourceWorkspace.SpaceA;
    }

}
