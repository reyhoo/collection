package com.chinaums.opensdk.event.model;

import com.chinaums.opensdk.cons.OpenEventName;

public class PageReloadEvent extends AbsUmsWebEvent {

    public PageReloadEvent(String params) {
        super(params);
    }

    @Override
    public String getEventName() {
        return OpenEventName.RESUME.toString();
    }

}
