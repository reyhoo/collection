package com.chinaums.opensdk.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 联系人工具类
 *
 * @author fengshangyan
 */
public class ContactsUtils {

    /**
     * 查找满足条件的联系人
     *
     * @param context
     * @param str
     * @param fileds
     * @param matchType
     * @return
     */
    public static List<Contact> search(Context context, String str,
                                       Object[] fileds, String matchType) {
        long lastTime = System.currentTimeMillis();
        List<Contact> mAllContactsList = loadContacts(context);
        long nowTime = System.currentTimeMillis();
        System.out.println("loadContacts = " + (nowTime - lastTime));
        List<Contact> filterList = new ArrayList<Contact>();// 过滤后的list

        for (Object filed : fileds) {
            if ("userName".equals(filed.toString())) {
                for (Contact contact : mAllContactsList) {
                    if (contact.phoneNumber != null && contact.name != null) {
                        if (matchType.equals("fuzzy")) {
                            // 姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                            if (contact.name.toLowerCase(Locale.CHINESE)
                                    .contains(str.toLowerCase(Locale.CHINESE))
                                    || contact.sortKey
                                    .toLowerCase(Locale.CHINESE)
                                    .replace(" ", "")
                                    .contains(
                                            str.toLowerCase(Locale.CHINESE))
                                    || contact.sortToken.simpleSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .contains(
                                            str.toLowerCase(Locale.CHINESE))
                                    || contact.sortToken.wholeSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .contains(
                                            str.toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("exact")) {
                            if (contact.name.toLowerCase(Locale.CHINESE)
                                    .equals(str.toLowerCase(Locale.CHINESE))
                                    || contact.sortKey
                                    .toLowerCase(Locale.CHINESE)
                                    .replace(" ", "")
                                    .equals(str
                                            .toLowerCase(Locale.CHINESE))
                                    || contact.sortToken.simpleSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .equals(str
                                            .toLowerCase(Locale.CHINESE))
                                    || contact.sortToken.wholeSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .equals(str
                                            .toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("expression")) {

                            if (contact.name.toLowerCase(Locale.CHINESE)
                                    .matches(str)
                                    || contact.sortToken.simpleSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .matches(str)
                                    || contact.sortToken.wholeSpell
                                    .toLowerCase(Locale.CHINESE)
                                    .matches(str)) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        }
                    }
                }
            } else if ("phoneNumbers".equals(filed.toString())) {
                String simpleStr = str.replaceAll("\\-|\\s", "");
                for (Contact contact : mAllContactsList) {
                    if (contact.phoneNumber != null && contact.name != null) {
                        if (matchType.equals("fuzzy")) {
                            if (contact.simpleNumber.contains(simpleStr)) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("exact")) {
                            if (contact.simpleNumber.equals(simpleStr)) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("expression")) {
                            if (contact.simpleNumber.matches(str
                                    .toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        }
                    }
                }
            } else if ("emails".equals(filed.toString())) {
                for (Contact contact : mAllContactsList) {
                    if (contact.phoneNumber != null && contact.name != null) {
                        if (matchType.equals("fuzzy")) {
                            if (contact.email.contains(str
                                    .toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("exact")) {
                            if (contact.email.equals(str
                                    .toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        } else if (matchType.equals("expression")) {
                            if (contact.email.matches(str
                                    .toLowerCase(Locale.CHINESE))) {
                                if (!filterList.contains(contact)) {
                                    filterList.add(contact);
                                }
                            }
                        }
                    }
                }
            }
        }
        return filterList;
    }

    /**
     * 查询所有联系人的姓名，电话，邮箱
     *
     * @return
     * @throws Exception
     */
    private static ArrayList<Contact> loadContacts(Context context) {
        ArrayList<Contact> allContacts = new ArrayList<Contact>();
        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, new String[]{Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER, "sort_key"}, null, null, "sort_key COLLATE LOCALIZED ASC");
        if (phoneCursor == null || phoneCursor.getCount() == 0) {
            UmsLog.d("未获得联系人数据");
            return allContacts;
        }
        int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(Phone.NUMBER);
        int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
        int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
        int CONTACT_ID = phoneCursor.getColumnIndex(Phone.CONTACT_ID);
        if (phoneCursor.getCount() > 0) {
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
                String email = "";

                phoneNumber = phoneNumber.replace("+86", "");
                Contact sortModel = new Contact(contactName, phoneNumber, email, sortKey);
                // 优先使用系统sortkey取,取不到再使用工具取
                String sortLetters = getSortLetterBySortKey(sortKey);
                if (sortLetters == null) {
                    sortLetters = getSortLetter(contactName);
                }
                sortModel.sortLetters = sortLetters;
                sortModel.sortToken = parseSortKey(sortKey);

                allContacts.add(sortModel);
            }
            UmsLog.d("mAllContactsList size:" + allContacts.size());
        }
        phoneCursor.close();

        return allContacts;
    }

    /**
     * 取sort_key的首字母
     *
     * @param sortKey
     * @return
     */
    private static String getSortLetterBySortKey(String sortKey) {
        if (sortKey == null || "".equals(sortKey.trim())) {
            return null;
        }
        String letter = null;
        // 汉字转换成拼音
        String sortString = sortKey.trim().substring(0, 1)
                .toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    static String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    // String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配

    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public static SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        token.chName = sortKey;
        if (sortKey != null && sortKey.length() > 0) {
            char[] enStrs = sortKey.toCharArray();
            // 其中包含的中文字符
            // String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                // 汉字转换成拼音
                String pinyin = CharacterParser.getInstance().getSelling(
                        enStrs[i] + "");
                if (UmsStringUtils.isBlank(pinyin)) {
                    continue;
                }
                String sortString = pinyin.substring(0, 1);
                // 拼接简拼
                token.simpleSpell += sortString;
                token.wholeSpell += pinyin;
            }
        }
        return token;
    }

    /**
     * 名字转拼音,取首字母
     *
     * @param name
     * @return
     */
    private static String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        // 汉字转换成拼音
        String pinyin = CharacterParser.getInstance().getSelling(name);
        if (UmsStringUtils.isBlank(pinyin))
            return letter;
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    public static class Contact {

        public Contact() {

        }

        public Contact(String name, String number, String email, String sortKey) {
            this.name = name;
            this.phoneNumber = number;
            this.email = email;
            this.sortKey = sortKey;
            if (number != null) {
                this.simpleNumber = number.replaceAll("\\-|\\s", "");
            }
        }

        public String name;
        public String phoneNumber;
        public String email;
        protected String simpleNumber;
        protected String sortKey;
        protected String sortLetters; // 显示数据拼音的首字母
        protected SortToken sortToken = new SortToken();

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result
                    + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
            result = prime * result + ((email == null) ? 0 : email.hashCode());
            result = prime * result
                    + ((sortKey == null) ? 0 : sortKey.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Contact other = (Contact) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (phoneNumber == null) {
                if (other.phoneNumber != null)
                    return false;
            } else if (!phoneNumber.equals(other.phoneNumber))
                return false;
            if (sortKey == null) {
                if (other.sortKey != null)
                    return false;
            } else if (!sortKey.equals(other.sortKey))
                return false;
            return true;
        }

    }

    public static class SortToken {
        public String simpleSpell = "";// 简拼
        public String wholeSpell = "";// 全拼
        public String chName = "";// 中文全名
    }

}
