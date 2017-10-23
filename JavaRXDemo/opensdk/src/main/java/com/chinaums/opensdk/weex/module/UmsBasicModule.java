package com.chinaums.opensdk.weex.module;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.chinaums.opensdk.weex.utils.PermissionsChecker;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.common.TypeModuleFactory;
import com.taobao.weex.common.WXModule;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Created at :  2017/6/7.
 * @Developer :  JiangBo
 * @Version :  1.0
 * @Description :  All ums modules must extend this class
 */

public class UmsBasicModule extends WXModule {

    /**
     * @param result   方法执行结果
     * @param callback 回调
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 回调JS
     */
    private void callJS(Map<String, Object> result, JSCallback callback) {
        if (callback != null) {
            callback.invoke(result);
        }
    }

    /**
     * @param success  方法是否执行成功
     * @param callback 回调
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 自建返回结果的调用
     */
    public void callBySimple(boolean success, JSCallback callback) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("result", success ? "success" : "fail");
        callJS(result, callback);
    }

    /**
     * @param success  方法是否执行成功
     * @param result   方法执行结果
     * @param callback 回调
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 回调JS
     */
    protected void callByResult(boolean success, Map<String, Object> result, JSCallback callback) {
        if (result != null) {
            result.put("result", success ? "success" : "fail");
        }
        callJS(result, callback);
    }

    /**
     * @return UmsGlobalEventModule 全局事件注册模块
     * @throws Exception 反射catch
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 反射获取全局事件注册模块
     */

    private UmsGlobalEventModule getGlobalEventModule() throws Exception {
        Class<?> clazz = Class.forName("com.taobao.weex.bridge.WXModuleManager");
        Method method = clazz.getDeclaredMethod("findModule", new Class[]{String.class, String.class, ModuleFactory.class});
        method.setAccessible(true);
        UmsGlobalEventModule globalEventModule = (UmsGlobalEventModule) method.invoke(null, mWXSDKInstance.getInstanceId(), "ums_globalEvent", new TypeModuleFactory<>(UmsGlobalEventModule.class));
        globalEventModule.mWXSDKInstance = mWXSDKInstance;

        return globalEventModule;
    }

    /**
     * @param eventName 全局时间名称
     * @param callback  回调标记
     * @throws Exception 反射catch
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 注册全局事件
     */
    protected void registerEventToGlobal(String eventName, String callback) throws Exception {
        getGlobalEventModule().addEventListener(eventName, callback);
    }

    /**
     * @param eventName 回调标记
     * @throws Exception 反射catch
     * @created at 2017/6/13
     * @developer Jiangbo
     * @version 1.0
     * @description 解注册全局事件
     */
    protected void unRegisterEventToGlobal(String eventName) throws Exception {
        getGlobalEventModule().removeEventListener(eventName);
    }

    /**
     * 获取当前Activity实例
     *
     * @return
     */
    protected Activity getActivity() {
        return ((Activity) mWXSDKInstance.getUIContext());
    }

    /**
     * 检查权限组是否已经拥有
     *
     * @param permissions
     */
    protected boolean hasPermission(String[] permissions) {
        return PermissionsChecker.getIstance(getActivity()).hasPermission(permissions);
    }


    /**
     * 检查权限是否已经拥有
     *
     * @param permission
     */
    protected boolean hasPermission(String permission) {
        return PermissionsChecker.getIstance(getActivity()).hasPermission("");
    }

    /**
     * 打印Toast
     *
     * @param resId
     */
    protected void showToast(@StringRes int resId) {
        showToast(getActivity().getResources().getString(resId));
    }

    /**
     * 打印Toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
