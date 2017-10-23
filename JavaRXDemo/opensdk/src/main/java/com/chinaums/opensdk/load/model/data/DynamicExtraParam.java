package com.chinaums.opensdk.load.model.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.manager.OpenDelegateManager;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

/**
 * 动态加载：首次打开参数转换
 */
public class DynamicExtraParam {

    /**
     * 单例
     */
    private static DynamicExtraParam instance = new DynamicExtraParam();

    /**
     * 登录token，用于支付验证
     */
    public static final String UMS_USER_TOKEN = "#umsUser.token#";

    /**
     * 登录用户的唯一标
     */
    public static final String UMS_USER_CODE = "#umsUser.code#";

    /**
     * 登录用户姓名
     */
    public static final String UMS_USER_NAME = "#umsUser.name#";

    /**
     * 登录用户手机号
     */
    public static final String UMS_USER_MOBILE = "#umsUser.mobile#";

    /**
     * 登录用户的唯一标
     */
    public static final String UMS_USER_MERCHANTID = "#umsUser.merchantId#";

    /**
     * 登录用户名称
     */
    public static final String UMS_USER_MERCHANTNAME = "#umsUser.merchantName#";

    /**
     * 从CP获取的token，用于支付验证 #cpUser.token#
     */
    public static final String CP_USER_TOKEN = "#cpUser.token#";

    /**
     * 刷卡器设备号
     */
    public static final String BOX_SN = "#box.sn#";

    /**
     * #umsUser.extUserId#
     */
    public static final String UMS_USER_EXTUSERID = "#umsUser.extUserId#";

    /**
     * #umsUser.appsysid#&
     */
    public static final String UMS_USER_APPSYSID = "#umsUser.appsysid#";

    /**
     * umsUser.userSysId#&
     */
    public static final String UMS_USER_USERSYSID = "#umsUser.userSysId#";

    /**
     * #umsUser.exitMsg#&
     */
    public static final String UMS_USER_EXITMSG = "#umsUser.exitMsg#";

    /**
     * 得到处理类
     *
     * @return
     */
    public static DynamicExtraParam getInstance() {
        return instance;
    }

    /**
     * 将#XXX#中的数据填充成具体的参数
     *
     * @param param
     * @return
     * @throws JSONException
     */
    public String getDynamicExtraParam(String param) throws JSONException {
        try {
            param = convertDynamicExtraParam(param);
            if (UmsStringUtils.isNotBlank(param)) {
                JSONObject jsonObject = JSON.parseObject(param);
                return jsonObject.toString();
            }
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

    /**
     * 将#XXX#中的数据填充成具体的参数
     *
     * @param param
     * @return
     * @throws JSONException
     */
    public String convertDynamicExtraParam(String param) throws JSONException {
        try {
            if (UmsStringUtils.isEmpty(param)) {
                UmsLog.e("资源包跳转附加信息为空");
                return null;
            }
            if (OpenDelegateManager.getProcessDelegate().getUser() == null) {
                return param;
            }
            param = param
                    .replaceAll(
                            UMS_USER_TOKEN,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getCode())
                    .replaceAll(
                            UMS_USER_CODE,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getCode())
                    .replaceAll(
                            UMS_USER_MOBILE,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getMobile())
                    .replaceAll(
                            UMS_USER_NAME,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getRealName())
                    .replaceAll(
                            UMS_USER_MERCHANTID,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getCode())
                    .replaceAll(
                            UMS_USER_MERCHANTNAME,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getRealName())
                    .replaceAll(
                            UMS_USER_EXTUSERID,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getCode())
                    .replaceAll(
                            UMS_USER_USERSYSID,
                            OpenDelegateManager.getProcessDelegate().getUser()
                                    .getCode());
            return param;
        } catch (Exception e) {
            UmsLog.e("", e);
        }
        return null;
    }

}