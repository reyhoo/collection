package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.manager.OpenDelegateDefined.Location;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsLog;

/**
 * 主动定位后，获取定位到的数据并返回
 */
public class ActiveLocationProcessor extends AbsStdDynamicProcessor {

    private final static String ACTION_LOCATION = "getLocation";
    private final static String ACTION_AREA = "getArea";
    private final static String ACTION_ALL = "getLocationAndArea";
    private final static String ACTION_CITY = "getLocationCity";

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_ACTIVE_LOCATION;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
        switch (resultCode) {
            case Activity.RESULT_OK:
                Location location = (Location) data
                        .getSerializableExtra("location");
                if (location != null) {
                    JSONObject locInfo = proccessCallback(model.getAction(),
                            location);
                    setRespAndCallWeb(model, createSuccessResponse(locInfo));
                } else {
                    setRespAndCallWeb(
                            model,
                            createErrorResponse("定位失败",
                                    DynamicCallback.CALLBACK_STATE_ERROR));
                }
                break;
            default:
                setRespAndCallWeb(
                        model,
                        createErrorResponse("定位失败",
                                DynamicCallback.CALLBACK_STATE_ERROR));
                break;
        }
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    areaLocate(model);
                } catch (Exception e) {
                    // 异常处理
                    UmsLog.e("", e);
                    apiRunExceptionCallBack(model, e);
                }
            }
        });
    }

    /**
     * 通过两种方式来得到定位的信息 1、是短期的历史 2、通过Hander异步通知 return null
     */
    private void areaLocate(final DynamicWebModel model) throws Exception {
        DynamicAPICallback apiCallback = new DynamicAPICallback() {
            @Override
            public void onAPICallback(int resultCode, Intent data) {
                try {
                    onCallback(model, resultCode, data);
                } catch (Exception e) {
                    apiRunExceptionCallBack(model, e);
                }
            }
        };
        Location location = OpenDelegateManager.getProcessDelegate()
                .getActiveLocation(model.getAction(), model.getHandler(), apiCallback);
    }

    private JSONObject proccessCallback(String action, Location location) {
        JSONObject locInfo = new JSONObject();
        if (ACTION_LOCATION.equals(action)) {
            locInfo.put("country", location.getCountry());
            locInfo.put("province", location.getProvince());
            locInfo.put("city", location.getCity());
            locInfo.put("district", location.getDistrict());
            locInfo.put("street", location.getStreet());
            locInfo.put("latitude", location.getLatitude());
            locInfo.put("longitude", location.getLongitude());
            locInfo.put("coorType", location.getCoorType());
        } else if (ACTION_AREA.equals(action)) {
            locInfo.put("country", location.getCountry());
            locInfo.put("province", location.getProvince());
            locInfo.put("city", location.getCity());
            locInfo.put("cityCode", location.getCityCode());
        } else if (ACTION_ALL.equals(action)) {
            JSONObject locationData = new JSONObject();
            locationData.put("country", location.getCountry());
            locationData.put("province", location.getProvince());
            locationData.put("city", location.getCity());
            locationData.put("district", location.getDistrict());
            locationData.put("street", location.getStreet());
            locationData.put("latitude", location.getLatitude());
            locationData.put("longitude", location.getLongitude());
            locationData.put("coorType", location.getCoorType());
            JSONObject areaData = new JSONObject();
            areaData.put("country", location.getCountry());
            areaData.put("province", location.getProvince());
            areaData.put("city", location.getCity());
            areaData.put("cityCode", location.getCityCode());
            locInfo.put("location", locationData);
            locInfo.put("area", areaData);
        } else if (ACTION_CITY.equals(action)) {
            locInfo.put("country", location.getCountry());
            locInfo.put("province", location.getProvince());
            locInfo.put("city", location.getCity());
            locInfo.put("district", location.getDistrict());
            locInfo.put("street", location.getStreet());
        }
        return locInfo;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

}