package com.chinaums.opensdk.event.model;

public abstract class AbsUmsWebEvent extends AbsUmsBaseEvent {

    public AbsUmsWebEvent(String params) {
        super(params);
    }
    
    public abstract String getEventName();

}