package com.chinaums.opensdk.weex.module;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import com.chinaums.opensdk.weex.utils.AppletConstant;
import com.chinaums.opensdk.weex.utils.Utils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @description 设备信息和信息电话通讯模块
 */
public class UmsDeviceModule extends UmsBasicModule {
    // 所需的权限
    private String[] permissions = new String[]{Manifest.permission.READ_CONTACTS};
    private JSCallback linkManCallback;

    /**
     * 获取设备信息
     *
     * @param callback 执行结果JS回调
     */
    @JSMethod
    public void getDeviceInfo(JSCallback callback) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //获取屏幕参数
            WindowManager manager = getActivity().getWindow().getWindowManager();
            DisplayMetrics metric = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metric);
            //获取可绘制区域的高宽
            Rect rect = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("model", Build.DEVICE);
            data.put("pixelRatio", metric.density + "");// 屏幕密度
            data.put("screenWidth", metric.widthPixels + "");// 屏幕宽度（像素）
            data.put("screenHeight", metric.heightPixels + "");// 屏幕高度（像素）
            data.put("windowWidth", mWXSDKInstance.getWeexWidth() + "");
            data.put("windowHeight", mWXSDKInstance.getWeexHeight() + "");
            data.put("system", Build.VERSION.RELEASE);
            data.put("platform", "Android");
            data.put("SDKVersion", Build.VERSION.SDK_INT + "");
            result.put("data", data);
            callByResult(true, result, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 拨打电话
     *
     * @param phoneNo  目标电话号码
     * @param callback 执行结果JS回调
     */
    @JSMethod
    public void callTo(String phoneNo, JSCallback callback) {
        try {
            //检查是否是合法手机号
            if (TextUtils.isEmpty(phoneNo) || !phoneNo.matches(AppletConstant.Regex.PHONE_NUM)) {
                callBySimple(false, callback);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
            getActivity().startActivity(intent);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 发送短信
     *
     * @param phoneNo  目标电话号码
     * @param content  短信内容
     * @param callback 执行结果JS回调
     */
    @JSMethod
    public void smsTo(String phoneNo, String content, JSCallback callback) {
        try {
            //检查是否是合法手机号
            if (TextUtils.isEmpty(phoneNo) || !phoneNo.matches(AppletConstant.Regex.PHONE_NUM)) {
                callBySimple(false, callback);
                return;
            }
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
            intent.putExtra("sms_body", content);
            getActivity().startActivity(intent);
            callBySimple(true, callback);
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 获取手机联系人
     *
     * @param callback 执行结果JS回调
     */
    @JSMethod
    public void getContact(JSCallback callback) {
        try {
            linkManCallback = callback;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (hasPermission(permissions)) {
                    openSelectedContentActivity();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), permissions, AppletConstant.Code.REQUEST_PERMISSION_RESULT);
                }
            } else {
                openSelectedContentActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callBySimple(false, callback);
        }
    }

    /**
     * 启动系统选则联系人界面
     */
    private void openSelectedContentActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        getActivity().startActivityForResult(intent, AppletConstant.Code.SELECTE_LINKMAN_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppletConstant.Code.REQUEST_PERMISSION_RESULT) {
            if (grantResults != null && grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        openSelectedContentActivity();
                    }
                }
            } else {
                showToast("通讯录授权失败");
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppletConstant.Code.SELECTE_LINKMAN_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                LoaderManager.LoaderCallbacks<Cursor> callback = new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        CursorLoader loader = new CursorLoader(getActivity());
                        loader.setUri(data.getData());
                        return loader;
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                        HashMap<String, Object> result = new HashMap<String, Object>();
                        ContentResolver resolverol = getActivity().getContentResolver();
                        cursor.moveToFirst();
                        // 条件为联系人ID
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                        Cursor phone = resolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phone.moveToNext()) {
                            String phoneNo = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String lable = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            result.put("phoneNumber", phoneNo);
                            result.put("name", lable);
                        }
                        if (result.isEmpty()) {
                            callBySimple(false, linkManCallback);
                        } else {
                            callByResult(true, result, linkManCallback);
                        }
                        linkManCallback = null;
                        Utils.CloseableUtil.close(cursor);
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                };
                getActivity().getLoaderManager().initLoader(1, null, callback);
            } else {
                callBySimple(false, linkManCallback);
                linkManCallback = null;
            }
        }
    }
}
