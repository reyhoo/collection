package com.chinaums.opensdk.event.model;


public abstract class AbsUmsBaseEvent implements IUmsEvent {

    /**
     * params
     */
    private String params;

    public AbsUmsBaseEvent() {

    }

    public AbsUmsBaseEvent(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }
    
    @Override
    public String toString() {
        return this.getClass() + "  " + params;
    }

}
