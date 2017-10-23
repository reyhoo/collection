package com.chinaums.opensdk.load.process;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinaums.opensdk.cons.DynamicProcessorType;
import com.chinaums.opensdk.cons.OpenConst.DynamicCallback;
import com.chinaums.opensdk.exception.DynamicProcessorCallException;
import com.chinaums.opensdk.load.model.data.DynamicWebModel;
import com.chinaums.opensdk.load.model.reqres.AbsWebRequestModel;
import com.chinaums.opensdk.util.ContactsUtils;
import com.chinaums.opensdk.util.ContactsUtils.Contact;
import com.chinaums.opensdk.util.JsonUtils;
import com.chinaums.opensdk.util.UmsLog;
import com.chinaums.opensdk.util.UmsStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类 SystemOpenContactsProcessor 的实现描述：通讯录获得人员信息
 */
public class SystemOpenContactsProcessor extends AbsStdDynamicProcessor {

    private final static String ACTION_SELECT = "select";
    private final static String ACTION_FIND = "find";

    @Override
    public int getType() {
        return DynamicProcessorType.SYSTEM_GET_PHONE_NUMBER;
    }

    @Override
    public void onCallback(DynamicWebModel model, int resultCode, Intent intent)
            throws Exception {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // 操作成功
                getContactPhone(model, intent);
                break;
            case Activity.RESULT_CANCELED:
                // 操作取消
                setRespAndCallWeb(
                        model,
                        createErrorResponse("cancel",
                                DynamicCallback.CALLBACK_STATE_CANCEL));
                break;
            default:
                // 其他操作
                setRespAndCallWeb(
                        model,
                        createErrorResponse("unknown",
                                DynamicCallback.CALLBACK_STATE_ERROR));
                break;
        }
    }

    /**
     * 获取选择联系人的电话号码
     */
    private void getContactPhone(DynamicWebModel model, Intent intent) {
        Cursor cursor = null;
        Cursor phone = null;
        try {
            ContentResolver resolver = model.getActivity().getContentResolver(); // 得到contentresolver对象
            cursor = resolver.query(intent.getData(), null, null, null, null);
            if (!cursor.moveToFirst()) {
                throw new DynamicProcessorCallException("没有获得选择内容");
            }
            int nameIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);// 取得联系人的名字索引
            String contact = cursor.getString(nameIndex);// 取得联系人的名字
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));// 取得联系人的ID索引值
            phone = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);// 第一个参数是确定查询电话号，第三个参数是查询具体某个人的过滤值
            SystemOpenContactResponse resp = new SystemOpenContactResponse();
            resp.setUserName(contact);
            getPhoneNumbersWithContact(phone, model, resp);
        } catch (DynamicProcessorCallException e) {
            this.setRespAndCallWeb(
                    model,
                    createErrorResponse(e.getMessage(),
                            DynamicCallback.CALLBACK_STATE_ERROR));
        } catch (Exception e1) {
            UmsLog.e("", e1);
            this.setRespAndCallWeb(
                    model,
                    createErrorResponse("获得通讯录信息失败",
                            DynamicCallback.CALLBACK_STATE_ERROR));
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (phone != null && !cursor.isClosed()) {
                phone.close();
            }
        }
    }

    /**
     * 根据指定的联系人cursor获取电话号码
     */
    private void getPhoneNumbersWithContact(final Cursor contactCursor,
                                            final DynamicWebModel model, final SystemOpenContactResponse resp) {
        final List<String> strPhoneNumbers = new ArrayList<String>();
        final String strPhoneNumber[] = {""};
        // 一个人可能有几个号码
        while (contactCursor.moveToNext()) {
            strPhoneNumbers
                    .add(contactCursor.getString(contactCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        if (!strPhoneNumbers.isEmpty()) {
            strPhoneNumber[0] = strPhoneNumbers.get(0);
        }
        if (strPhoneNumbers.size() > 1) {
            new AlertDialog.Builder(model.getActivity())
                    .setTitle("请选择此联系人的号码")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setSingleChoiceItems(
                            strPhoneNumbers.toArray(new String[]{}), 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    strPhoneNumber[0] = strPhoneNumbers
                                            .get(which);
                                }
                            })
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    resp.setPhoneNumber(strPhoneNumber[0]);
                                    setRespAndCallWeb(model,
                                            createSuccessResponse(JsonUtils
                                                    .convert2Json(resp)));
                                }
                            }).show();
        } else {
            resp.setPhoneNumber(strPhoneNumber[0]);
            setRespAndCallWeb(model,
                    createSuccessResponse(JsonUtils.convert2Json(resp)));
        }
    }

    @Override
    protected void execute(final DynamicWebModel model) throws Exception {
        // 正常处理
        if (ACTION_SELECT.equals(model.getAction())) {
            model.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        openContacts(model);
                    } catch (Exception e) {
                        // 异常处理
                        apiRunExceptionCallBack(model, e);
                    }
                }
            });

        } else if (ACTION_FIND.equals(model.getAction())) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    findContacts(model);
                }
            }).start();
        }

    }

    /**
     * 打开系统相机
     */
    private void openContacts(DynamicWebModel model) throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        model.getActivity().startActivityForResult(intent,
                DynamicProcessorType.SYSTEM_GET_PHONE_NUMBER);
    }

    /**
     * 查找通讯录
     */
    private void findContacts(final DynamicWebModel model) {
        SystemContactRequestModel requestModel = (SystemContactRequestModel) model
                .getRequestModel();
        List<Contact> contacts = ContactsUtils.search(model.getActivity(),
                requestModel.getFilter(), requestModel.getFields(),
                requestModel.getMatchType());
        final ArrayList<SystemOpenContactResponse> backContacts = new ArrayList<SystemOpenContactResponse>();
        int size = requestModel.getMaxNum();
        if (contacts.size() < requestModel.getMaxNum() || size == -1
                || size == 0) {
            size = contacts.size();
        }
        SystemOpenContactResponse pesponse = null;
        Contact contact = null;
        for (int i = 0; i < size; i++) {
            contact = contacts.get(i);
            pesponse = new SystemOpenContactResponse();
            pesponse.setPhoneNumber(contact.phoneNumber);
            pesponse.setUserName(contact.name);
            backContacts.add(pesponse);
        }
        model.getHandler().post(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();
                jsonArray.addAll(backContacts);
                setRespAndCallWeb(model, createSuccessResponse(jsonArray));
            }
        });

    }

    @Override
    protected AbsWebRequestModel makeNewWebRequestModel(DynamicWebModel model) {
        return new SystemContactRequestModel(model.getRequestObj());
    }

    @Override
    public DynamicRequestType getProcessorType() {
        return DynamicRequestType.ASYNCHRONIZED;
    }

    /**
     * JS请求JSON报文
     */
    private class SystemContactRequestModel extends AbsWebRequestModel {

        /**
         * 需要查找的内容。
         */
        private String filter;

        /**
         * matchType：匹配类型，默认为模糊匹配。exact：精确匹配，fuzzy：模糊匹配，expression：正则表达式匹配。
         */
        private String matchType = "fuzzy";

        /**
         * fields：匹配的属性的字符串数组。userName：联系人名称，phoneNumbers：联系人电话。emails：
         * 联系人email地址。
         */
        private String[] fields;

        /**
         * maxNum：查找返回的最大数目。如果为-1或不传则为全部。
         */
        private Integer maxNum;

        public SystemContactRequestModel(JSONObject request) {
            super(request);
        }

        @Override
        protected void initByRequest() {
            try {
                JSONObject data = getRequest().getJSONObject("data");
                filter = data.getString("filter");
                matchType = data.getString("matchType");
                fields = JsonUtils.fromJsonString(data.getString("fields"),
                        String[].class);
                maxNum = data.getInteger("maxNum");
            } catch (Exception e) {
                UmsLog.e("", e);
            }
        }

        public String getFilter() {
            return filter;
        }

        public String getMatchType() {
            return matchType;
        }

        public String[] getFields() {
            return fields;
        }

        public int getMaxNum() {
            return maxNum == null ? -1 : maxNum.intValue();
        }

    }

    private class SystemOpenContactResponse {

        /**
         * phoneNumber
         */
        private String phoneNumber;

        /**
         * userName
         */
        private String userName;

        public void setPhoneNumber(String phoneNumber) {
            if (UmsStringUtils.isNotBlank(phoneNumber)) {
                this.phoneNumber = phoneNumber.replace("+86", "").replace(" ",
                        "");
            } else {
                this.phoneNumber = phoneNumber;
            }
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getUserName() {
            return userName;
        }
    }
}
