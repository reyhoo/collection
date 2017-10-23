package com.chinaums.opensdk.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.chinaums.opensdk.activity.base.IDynamicBizActivity;
import com.chinaums.opensdk.cons.DynamicResourceWorkspace;
import com.chinaums.opensdk.cons.OpenOSType;
import com.chinaums.opensdk.download.listener.ResourceManagerCheckUserInvalidMultiListener;
import com.chinaums.opensdk.download.listener.ResourceManagerMultiListener;
import com.chinaums.opensdk.download.listener.ResourceNetProcessListener;
import com.chinaums.opensdk.load.process.IDynamicProcessor;
import com.chinaums.opensdk.load.process.delegate.OpenScanBarDelegateParamDefined;
import com.chinaums.opensdk.load.process.delegate.OpenSwipeCardDelegateParamDefined;
import com.chinaums.opensdk.load.process.listener.DynamicAPICallback;
import com.chinaums.opensdk.load.process.listener.ProcessListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class OpenDelegateDefined {

    public interface IProcessDelegate {

        /**
         * 得到客户端信息
         */
        public Client getClient();

        /**
         * 得到用户信息
         */
        public User getUser();

        /**
         * 获取用户token
         */
        public void getXmsmkToken(Activity activity, String tokenType, DynamicAPICallback apiCallback);

        /**
         * 得到定位信息
         */
        public Location getLocation();

        /**
         * 得到主动获取地理定位信息
         */
        public Location getActiveLocation(String type, Handler mHandler,
                                          DynamicAPICallback apiCallback);

        /**
         * 得到动态加载页面的实现
         */
        public Class<? extends IDynamicBizActivity> getOpenDynamicBizActivityClazz();

        /**
         * 得到自定义API的映射关系
         */
        public Map<String, Class<? extends IDynamicProcessor>> getExtDynamicProcessorMap();

        /**
         * 得到可打开的本地Native页面的映射关系
         */
        public Map<String, Class<? extends Activity>> getDynamicOpenPageMap();

        /**
         * 动态调用时的默认参数修改
         */
        public String getOpenConfigProperty(String key);

        /**
         * 得到历史资源key的生成规则
         */
        public IOpenDynamicResourceHistoryKeyGenerateRule getDynamicResourceHistoryKeyGenerateRule();

        /**
         * 检查用户失效后处理
         */
        public void processUserInvalid(Context context);

        public IOpenDynamicListProcess getDynamicUmsNet();

        public IDynamicUmsDeviceAndPay getDynamicUmsDeviceAndPay();

        public IOpenDynamicProcessorDelegate getOpenDynamicProcessorDelegate();

    }

    public interface IOpenDynamicListProcess {

        /**
         * 获得个性化列表
         */
        public void getPersonalBizList(
                ResourceNetProcessListener<OpenDelegateDefined.IPersonalBizListResponse> listener)
                throws Exception;

        /**
         * 获得推荐列表
         */
        public void getRecommendBizList(
                ResourceNetProcessListener<IRecommendBizListResponse> listener)
                throws Exception;

        /**
         * 获得待展示列表
         */
        public void getDisplayBizList(
                ResourceNetProcessListener<IDisplayBizListResponse> listener)
                throws Exception;

        public void updatePersonalBizList(
                String biz,
                ResourceNetProcessListener<IUpdatePersonalBizListResponse> listener)
                throws Exception;

        public void updateList(DynamicResourceWorkspace space,
                               final ResourceManagerCheckUserInvalidMultiListener listener)
                throws Exception;

        public void refreshList(DynamicResourceWorkspace space,
                                final ResourceManagerMultiListener listener) throws Exception;

        public void updateListByHistory(DynamicResourceWorkspace space,
                                        final ResourceManagerMultiListener listener) throws Exception;
    }

    /**
     * api对应的代理类
     */
    public interface IOpenDynamicProcessorDelegate {

        public void processPagePay(Activity activity, int requestCode,
                                   String openPayRequest,
                                   String bizCode, DynamicAPICallback apiCallback);

        /**
         * 第三方调用支付
         */
        public void processShowPayCenter(Activity activity, int requestCode,
                                         String showPayCenterRequest,
                                         String bizCode, DynamicAPICallback apiCallback);

        /**
         * 第三方调用支付回调
         */
        public String processShowPayCenterCallback(int resultCode, Intent data, Context context);

        /**
         * 调用POS通远程快捷支付
         */
        public void processShowQuickPay(Activity activity, int requestCode,
                                        Bundle showQuickPayRequest,
                                        String bizCode, DynamicAPICallback apiCallback);

        public void processScanBar(
                Activity activity,
                int requestCode,
                OpenScanBarDelegateParamDefined.OpenScanBarRequest openScanBarRequest);

        public OpenScanBarDelegateParamDefined.OpenScanBarResponse processScanBarCallback(
                int resultCode, Intent data);

        /**
         * 农产品支付加密商户号
         */
        public String encryptPayPassword(String merchantNo);

        /**
         * 友盟统计
         */
        public void umengEvent(Activity activity, String id,
                               HashMap<String, String> m);

        /**
         * 跳转主页
         */
        public void navToMainPage(Activity activity);

        public void showShareView(Activity activity, String shareInfo,
                                  DynamicAPICallback apiCallback, int requestCode, String bizCode);

        public void showShare(Activity activity, String shareInfo,
                              DynamicAPICallback apiCallback, int requestCode, String bizCode);

    }

    /**
     * 支付、签购单、刷卡器使用等相关功能实现类
     */
    public interface IDynamicUmsDeviceAndPay {

        /**
         * 打印小票
         */
        public void boxPrintBill(Activity activity, String content,
                                 String pageCount, ProcessListener listener);

        /**
         * 刷卡取卡号
         */
        public void getTrack2(Activity activity, String boxType)
                throws Exception;

        /**
         * 刷卡取卡号
         */
        public OpenSwipeCardDelegateParamDefined.OpenSwipeCardResponse processTrack2Callback(
                int resultCode, Intent data) throws Exception;

        /**
         * 上电
         */
        public void powerOnIcc(final Activity activity,
                               final ProcessListener listener);

        /**
         * 执行apdu指令
         */
        public void sendApdu(final Activity activity, final String apdu,
                             final ProcessListener listener);

        public void powerOffIcc(final Activity activity,
                                final ProcessListener listener);

        public void getOfflineTransactionInfo(final Activity activity,
                                              final ProcessListener listener);


        public void uploadOfflineTransactionInfo(final Activity activity,
                                                 final String identifier, final String transactionId,
                                                 final String boxId, final ProcessListener listener);

    }

    /**
     * 为了保证客户端启动时可以获取历史资源列表，所以需要app提供历史资源存储时的key规则
     */
    public interface IOpenDynamicResourceHistoryKeyGenerateRule {

        /**
         * 个性化列表的key
         */
        public String getKeyForPersonalBizList();

        /**
         * 待展示列表的key
         */
        public String getKeyForDisplayBizList();

        /**
         * 广告列表的key
         */
        public String getKeyForDisplayAdsBizList();

        /**
         * 推荐列表的key
         */
        public String getKeyForRecommendBizList();

    }

    public static class Location implements Serializable {

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
         * 选择城市的编码
         */
        private String cityCode;

        /**
         * 用户手动选择城市
         */
        private String chosenCity;

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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCoorType() {
            return coorType;
        }

        public void setCoorType(String coorType) {
            this.coorType = coorType;
        }

        public String getAltitude() {
            return altitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getChosenCity() {
            return chosenCity;
        }

        public void setChosenCity(String chosenCity) {
            this.chosenCity = chosenCity;
        }

    }

    /**
     * 用户信息
     */
    public static class User {

        /**
         * code
         */
        private String code;

        /**
         * realName
         */
        private String realName;

        /**
         * name
         */
        private String name;

        /**
         * nickName
         */
        private String nickName;

        /**
         * email
         */
        private String email;

        /**
         * mobile
         */
        private String mobile;

        /**
         * sex
         */
        private String sex;

        /**
         * isAuth
         */
        private String isAuth;

        /**
         * isAuth
         */
        private String certificateType;

        /**
         * certificateNumber
         */
        private String certificateNumber;

        /**
         * countryCode
         */
        private String countryCode;

        /**
         * country
         */
        private String country;

        /**
         * provinceCode
         */
        private String provinceCode;

        /**
         * province
         */
        private String province;

        /**
         * cityCode
         */
        private String cityCode;

        /**
         * city
         */
        private String city;

        /**
         * districtCode
         */
        private String districtCode;

        /**
         * district
         */
        private String district;

        /**
         * address
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

    /**
     * 客户端信息
     */
    public static class Client {

        /**
         * 客户端类型，取值为androidphone、iphone、winphone、androidpad、ipad
         */
        private OpenOSType type;

        /**
         * 当前客户端版本号
         */
        private String version;

        /**
         * 客户端系统编码，如无为空
         */
        private String sysCode;

        /**
         * 设备imei
         */
        private String imei;

        /**
         * 设备imsi
         */
        private String imsi;

        /**
         * landscape为横屏,portrait为竖屏
         */
        private String screenOrientation;

        /**
         * 宽度分辨率，px为单位
         */
        private String screenDisplayWidth;

        /**
         * 高度分辨率，px为单位
         */
        private String screenDisplayHeight;

        /**
         * 绑定的设备号
         */
        private String boxId;

        /**
         * 客户端业务类型
         */
        private String appName;

        /**
         * releaseState
         */
        private String releaseState;

        /**
         * cusCode
         */
        private String cusCode;

        public OpenOSType getType() {
            return type;
        }

        public void setType(OpenOSType type) {
            this.type = type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getReleaseState() {
            return releaseState;
        }

        public void setReleaseState(String releaseState) {
            this.releaseState = releaseState;
        }

        public String getCusCode() {
            return cusCode;
        }

        public void setCusCode(String cusCode) {
            this.cusCode = cusCode;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }

        public String getScreenOrientation() {
            return screenOrientation;
        }

        public void setScreenOrientation(String screenOrientation) {
            this.screenOrientation = screenOrientation;
        }

        public String getScreenDisplayWidth() {
            return screenDisplayWidth;
        }

        public void setScreenDisplayWidth(String screenDisplayWidth) {
            this.screenDisplayWidth = screenDisplayWidth;
        }

        public String getScreenDisplayHeight() {
            return screenDisplayHeight;
        }

        public void setScreenDisplayHeight(String screenDisplayHeight) {
            this.screenDisplayHeight = screenDisplayHeight;
        }

        public String getBoxId() {
            return boxId;
        }

        public void setBoxId(String boxId) {
            this.boxId = boxId;
        }

    }

    /**
     * 获取资源包列表
     */
    public static interface IPersonalBizListResponse extends INetPackResponse {

        public String getBiz();

    }

    /**
     * 推荐资源包列表
     */
    public static interface IRecommendBizListResponse extends INetPackResponse {

        public String getBiz();

    }

    /**
     * 资源包列表
     */
    public static interface IDisplayBizListResponse extends INetPackResponse {

        public String getBiz();

        public String getAds();

    }

    /**
     * 更新资源包列表
     */
    public static interface IUpdatePersonalBizListResponse extends
            INetPackResponse {

    }

    public static interface INetPackResponse {

    }

}