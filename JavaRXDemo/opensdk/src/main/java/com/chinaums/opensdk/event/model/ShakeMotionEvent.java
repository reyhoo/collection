package com.chinaums.opensdk.event.model;

import com.chinaums.opensdk.cons.OpenEventName;

public class ShakeMotionEvent extends AbsUmsWebEvent {


    public ShakeMotionEvent() {
        super("");
    }

    @Override
    public String getEventName() {
        return OpenEventName.SHAKESTATUS.toString();
    }

}
