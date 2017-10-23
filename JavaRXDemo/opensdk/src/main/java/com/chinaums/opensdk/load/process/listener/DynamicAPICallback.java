package com.chinaums.opensdk.load.process.listener;

import android.content.Intent;

public interface DynamicAPICallback {

    public void onAPICallback(int resultCode, Intent data);

}
