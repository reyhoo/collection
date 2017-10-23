package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.manager.OpenDelegateDefined.Location;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 类 GetLocationProcessor 的实现描述：得到用户信息API
 */
public class GetLocationProcessor extends AbsStdDynamicProcessor {

    private final static String ACTION_LOCATION = "getLocation";
    private final static String ACTION_AREA = "getArea";

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {

    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_LOCATION;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        Location location = OpenDelegateManager.getProcessDelegate()
                .getLocation();
        if (location == null) {
            setRespAndCallWeb(
                    model,
                    createErrorResponse("用户位置信息获取失败",
                            DynamicCallback.CALLBACK_STATE_ERROR));
            return;
        }
        if (ACTION_LOCATION.equals(model.getAction())) {
            getLocatedInfo(model, location);
        } else if (ACTION_AREA.equals(model.getAction())) {
            // 这个地方要换成用户选择的地址代理
            // Location location =
            // OpenDelegateManager.getProcessDelegate().getOptLocation();
            AreaInfo locInfo = new AreaInfo();
            locInfo.setCountry(location.getCountry());
            locInfo.setProvince(location.getProvince());
            locInfo.setCity(location.getCity());
            locInfo.setCityCode(location.getCityCode());
            setRespAndCallWeb(model,
                    createSuccessResponse(JsonUtils.convert2Json(locInfo)));
        } else {
            getLocatedInfo(model, location);// /默认发送定位位置
        }
    }

    /**
     * 获取定位位置信息
     */
    private void getLocatedInfo(DynamicWebModel model, Location location) {
        LocationInfo locInfo = new LocationInfo();
        locInfo.setCountry(location.getCountry());
        locInfo.setProvince(location.getProvince());
        locInfo.setCity(location.getCity());
        locInfo.setDistrict(location.getDistrict());
        locInfo.setStreet(location.getStreet());
        locInfo.setLatitude(location.getLatitude());
        locInfo.setLongitude(location.getLongitude());
        locInfo.setCoorType(location.getCoorType());
        locInfo.setAltitude(location.getAltitude());
        locInfo.setChosenCity(location.getChosenCity());
        //设置城市编码
        locInfo.setCityCode(location.getCityCode());
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(locInfo)));
    }

    private class LocationInfo {

        /**
         * 定位后的国家名称
         */
        private String country;

        /**
         * 定位后的省份名称
         */
        private String province;

        /**
         * 定位后的城市名称
         */
        private String city;

        /**
         * 定位后的区县名称
         */
        private String district;

        /**
         * 定位后的街道名称
         */
        private String street;

        /**
         * 定位后的维度
         */
        private String latitude;

        /**
         * 定位后的经度
         */
        private String longitude;

        /**
         * 坐标系
         */
        private String coorType;

        /**
         * 高度
         */
        private String altitude;

        /**
         * 用户手动选择城市
         */
        private String chosenCity;

        /**
         * 选择城市的编码
         */
        private String cityCode;

        public void setCountry(String country) {
            this.country = country;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setCoorType(String coorType) {
            this.coorType = coorType;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getCoorType() {
            return coorType;
        }

        public String getAltitude() {
            return altitude;
        }

        public String getChosenCity() {
            return chosenCity;
        }

        public void setChosenCity(String chosenCity) {
            this.chosenCity = chosenCity;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

    }

    private class AreaInfo {

        /**
         * 定位后的国家名称
         */
        private String country;

        /**
         * 定位后的省份名称
         */
        private String province;

        /**
         * 定位后的城市名称
         */
        private String city;
        
        /**
         * 选择城市的编码
         */
        private String cityCode;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

    }
}
