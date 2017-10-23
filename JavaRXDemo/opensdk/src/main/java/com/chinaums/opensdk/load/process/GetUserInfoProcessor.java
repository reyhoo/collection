package com.chinaums.opensdk.load.process;

import android.content.Intent;

import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.manager.OpenDelegateDefined.User;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.JsonUtils;

/**
 * 类 GetUserInfoProcessor 的实现描述：得到用户信息API
 */
public class GetUserInfoProcessor extends AbsStdDynamicProcessor {

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent data)
            throws Exception {
    }

    @Override
    public int getType() {
        return DynamicProcessorType.GET_USER_INFORMATION;
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.SYNCHRONIZED;
    }

    @Override
    protected void execute(DynamicWebModel model) throws Exception {
        User user = OpenDelegateManager.getProcessDelegate().getUser();
        UserInfo userInfo = new UserInfo();
        userInfo.setCode(user.getCode());
        userInfo.setRealName(user.getRealName());
        userInfo.setName(user.getName());
        userInfo.setNickName(user.getNickName());
        userInfo.setEmail(user.getEmail());
        userInfo.setMobile(user.getMobile());
        userInfo.setSex(user.getSex());
        userInfo.setIsAuth(user.getIsAuth());
        userInfo.setCertificateType(user.getCertificateType());
        userInfo.setCertificateNumber(user.getCertificateNumber());
        userInfo.setCountryCode(user.getCountryCode());
        userInfo.setCountry(user.getCountry());
        userInfo.setProvinceCode(user.getProvinceCode());
        userInfo.setProvince(user.getProvince());
        userInfo.setCityCode(user.getCityCode());
        userInfo.setCity(user.getCity());
        userInfo.setDistrictCode(user.getDistrictCode());
        userInfo.setDistrict(user.getDistrict());
        userInfo.setAddress(user.getAddress());
        userInfo.setUserName(user.getUserName());
        setRespAndCallWeb(model,
                createSuccessResponse(JsonUtils.convert2Json(userInfo)));
    }

    private class UserInfo {

        /**
         * 用户号
         */
        private String code;

        /**
         * 真实姓名
         */
        private String realName;

        /**
         * 用户姓名
         */
        private String name;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 邮箱
         */
        private String email;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 0女，1男
         */
        private String sex;

        /**
         * 01非实名，02实名
         */
        private String isAuth;

        /**
         * 01身份证
         */
        private String certificateType;

        /**
         * 证件号码
         */
        private String certificateNumber;

        /**
         * 国家编码
         */
        private String countryCode;

        /**
         * 国家名称
         */
        private String country;

        /**
         * 省份编码
         */
        private String provinceCode;

        /**
         * 省份名称
         */
        private String province;

        /**
         * 城市编码
         */
        private String cityCode;

        /**
         * 城市名称
         */
        private String city;

        /**
         * 区县编码
         */
        private String districtCode;

        /**
         * 区县名称
         */
        private String district;

        /**
         * 住址
         */
        private String address;

        /**
         * 厦门用户信息userName
         */
        private String userName;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getCertificateType() {
            return certificateType;
        }

        public void setCertificateType(String certificateType) {
            this.certificateType = certificateType;
        }

        public String getCertificateNumber() {
            return certificateNumber;
        }

        public void setCertificateNumber(String certificateNumber) {
            this.certificateNumber = certificateNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrictCode() {
            return districtCode;
        }

        public void setDistrictCode(String districtCode) {
            this.districtCode = districtCode;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }
}
