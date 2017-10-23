package com.chinaums.opensdk.event.model;

import com.chinaums.opensdk.cons.OpenEventName;


public class NetStatusChangedEvent extends AbsUmsWebEvent {

    public NetStatusChangedEvent(String params) {
        super(params);
    }
    
    @Override
    public String getEventName() {
        return OpenEventName.NETSTATUS.toString();
    }

}
