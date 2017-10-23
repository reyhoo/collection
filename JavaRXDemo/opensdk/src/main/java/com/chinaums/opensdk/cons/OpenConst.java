package com.chinaums.opensdk.cons;

import java.io.File;

public class OpenConst {

    public static class Environment {
        public static String PROD = "PROD";
        public static String TEST = "TEST";
        public static String UAT = "UAT";
    }

    public static class BackendEnvironment {
        public static String PROD = "prod";
        public static String TEST = "test";
        public static String UAT = "uat";
    }

    public static class Message {
        public static String EMPTY_RESPONSE = "网络数据错误";
        public static String CONNECT_TIMEOUT = "网络连接超时";
        public static String BIZ_TIP_UPDATA_APP = "功能暂不支持，请升级您的客户端";
        public static String NET_REQUEST_ERROR = "请求报文参数有错误，请检查报文";
        public static String COMUNICATION_EORROR = "通讯错误";
        public static String NOT_NETWORK = "当前无网络";
        public static String BIZ_PREPARE_ERROR = "业务校验错误";
        public static String BIZ_LOAD_ERROR = "业务加载错误";
        public static String MACKEY_NOT_REACHABLE = "mackey获取失败";
    }

    public static class DynamicBizType {
        public final static String CONFIG_BIZ = "conf";
        public final static String SHARE_BIZ = "share";
        public final static String ADS_BIZ = "ad";
        public final static String BIZ = "biz";
        public final static String NATIVE_BIZ = "native";
        public final static String WEB_BIZ = "web";
        public final static String APP_BIZ = "app";
        public final static String WEEX_BIZ = "weexweb";
        public final static String WEEX_REMOTE_BIZ = "weexremote";
    }

    public static class DynamicCallback {
        public final static String RESP_CODE_OK = "0000";
        public final static String RESP_CODE_NO_OK = "0001";
        public final static String RESP_MESSAGE_OK = "成功";
        public final static String RESP_MESSAGE_NO_OK = "未知错误";
        public final static String CALLBACK_STATE_SUCCESS = "success";
        public final static String CALLBACK_STATE_ERROR = "error";
        public final static String CALLBACK_STATE_CANCEL = "cancel";
        public final static String CALLBACK_STATE_TIMEOUT = "timeout";
    }

    public static class DynamicCommonConst {

        public final static String DEFAULT_WEB_CALLBACK_NAME = "UmsApi.onCallback";
        public final static String WEB_INIT_CALLBACK_NAME = "onInitContext";
        public final static String WEB_RECOVER_CALLBACK_NAME = "onRecoverContext";

        public final static String WEB_REQUEST_OBJ_FIELD_NAME_INFO = "info";
        public final static String WEB_REQUEST_OBJ_FIELD_NAME_REQUEST_ID = "requestId";
        public final static String WEB_REQUEST_OBJ_FIELD_NAME_REQUEST_ACTION = "action";

        public final static int ACTIVITY_REQUEST_CODE_API = 9999;
        public final static int ACTIVITY_REQUEST_CODE_NAVIGATOR = 9998;

        public final static String REMOTE_WEB_PREFIX = "http:";
        public final static String LOCAL_FILE_PREFIX = "file://";

        public final static int SCAN_BARCODE_ACTIVITY_CODE = 999; // 扫描API code码

        public final static String UMS_API_FLAG = "umsApi"; // api标记
    }

    // 获得图像媒体文件相关
    public static class DynamicMediaConst {
        public static final int DYNAMIC_MEDIA_DESTINATIONTYPE_URI = 1;
        public static final int DYNAMIC_MEDIA_DESTINATIONTYPE_BASE64 = 0;
        public static final int DYNAMIC_MEDIA_SOURCETYPE_ALL = 0;
        public static final int DYNAMIC_MEDIA_SOURCETYPE_CAMERA = 1;
        public static final int DYNAMIC_MEDIA_SOURCETYPE_PHOTO = 2;
        public static final int DYNAMIC_MEDIA_ENCODINGTYPE_JPG = 0;
        public static final int DYNAMIC_MEDIA_ENCODINGTYPE_PNG = 1;
        public static final int DYNAMIC_MEDIA_MEDIATYPE_PHOTO = 0;
        public static final int DYNAMIC_MEDIA_MEDIATYPE_CAMERA = 1;
        public static final int DYNAMIC_MEDIA_MEDIATYPE_ALL = 2;
    }

    public static class DynamicDownloadConf {

        //eg.""
        private static final String BIZ_ROOT_FOLDER = "";

        //eg."/list/bizlist"
        public static final String BIZ_LIST_FOLDER = File.separator + "list"
                + File.separator + "bizlist";

        //eg."/list/category"
        public static final String CATEGORY_LIST_FOLDER = File.separator
                + "list" + File.separator + "category";

        //eg."/list/area"
        public static final String AREA_FOLDER = File.separator + "list"
                + File.separator + "area";

        //eg."/list/clientUpdate"
        public static final String CLIENTUPDATE_FOLDER = File.separator
                + "list" + File.separator + "clientUpdate";

        //eg."/res"
        public static final String BIZ_RES_FILE_FOLDER = File.separator + "res";

        //eg."/img/stdicon"
        public static final String BIZ_STD_ICON_FILE_FOLDER = File.separator
                + "img" + File.separator + "stdicon";

        //eg."/img/largeicon"
        public static final String BIZ_LARGE_ICON_FILE_FOLDER = File.separator
                + "img" + File.separator + "largeicon";

        //eg."/img/ads"
        public static final String BIZ_ADS_FILE_FOLDER = File.separator + "img"
                + File.separator + "ads";

        //eg."/img/category"
        public static final String BIZ_CATEGORY_IMG_FILE_FOLDER = File.separator
                + "img" + File.separator + "category";

        //eg."/res/apk"
        public static final String BIZ_APK_FILE_FOLDER = BIZ_RES_FILE_FOLDER
                + File.separator + "apk";

        //eg."/download/list/bizlist"
        public static final String BIZ_LIST_ORIGINAL_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_LIST_FOLDER;

        //eg."/process/list/bizlist"
        public static final String BIZ_LIST_PROCESS_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_LIST_FOLDER;

        //eg."/download/list/category"
        public static final String CATEGORY_LIST_ORIGINAL_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + CATEGORY_LIST_FOLDER;

        //eg."/process/list/category"
        public static final String CATEGORY_LIST_PROCESS_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + CATEGORY_LIST_FOLDER;

        //eg."/download/list/area"
        public static final String AREA_ORIGINAL_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + AREA_FOLDER;

        //eg."/process/list/area"
        public static final String AREA_PROCESS_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + AREA_FOLDER;

        //eg."/download/list/clientUpdate"
        public static final String CLIENTUPDATE_ORIGINAL_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + CLIENTUPDATE_FOLDER;

        //eg."/process/list/clientUpdate"
        public static final String CLIENTUPDATE_PROCESS_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + CLIENTUPDATE_FOLDER;

        //eg."/download/res"
        public static final String BIZ_ORIGINAL_RES_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_RES_FILE_FOLDER;

        //eg."/process/res"
        public static final String BIZ_PROCESS_RES_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_RES_FILE_FOLDER;

        //eg."/download/img/stdicon"
        public static final String BIZ_ORIGINAL_STD_ICON_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_STD_ICON_FILE_FOLDER;

        //eg."/process/img/stdicon"
        public static final String BIZ_PROCESS_STD_ICON_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_STD_ICON_FILE_FOLDER;

        //eg."/download/img/largeicon"
        public static final String BIZ_ORIGINAL_LARGE_ICON_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_LARGE_ICON_FILE_FOLDER;

        //eg."/process/img/largeicon"
        public static final String BIZ_PROCESS_LARGE_ICON_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_LARGE_ICON_FILE_FOLDER;

        //eg."/download/img/ads"
        public static final String BIZ_ORIGINAL_ADS_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_ADS_FILE_FOLDER;

        //eg."/process/img/ads"
        public static final String BIZ_PROCESS_ADS_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_ADS_FILE_FOLDER;

        //eg."/download/img/category"
        public static final String BIZ_ORIGINAL_CATEGORY_IMG_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_CATEGORY_IMG_FILE_FOLDER;

        //eg."/process/img/category"
        public static final String BIZ_PROCESS_CATEGORY_IMG_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_CATEGORY_IMG_FILE_FOLDER;

        //eg."/download/res/apk"
        public static final String BIZ_ORIGINAL_APK_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "download" + BIZ_APK_FILE_FOLDER;

        //eg."/process/res/apk"
        public static final String BIZ_PROCESS_APK_FILE_FOLDER = BIZ_ROOT_FOLDER
                + File.separator + "process" + BIZ_APK_FILE_FOLDER;

        public static final String BIZ_LIST_ORIGINAL_FILE_NAME = "bizlist.zip";
        public static final String BIZ_LIST_PROCESS_FILE_NAME = "bizlist.json";
        public static final String BIZ_LIST_SIGNATURE_FILE_NAME = "bizlist.sign";
        public static final String CATEGORY_LIST_ORIGINAL_FILE_NAME = "category.zip";
        public static final String CATEGORY_LIST_PROCESS_FILE_NAME = "category.json";
        public static final String CATEGORY_LIST_SIGNATURE_FILE_NAME = "category.sign";
        public static final String AREA_ORIGINAL_FILE_NAME = "area.zip";
        public static final String AREA_PROCESS_FILE_NAME = "area.json";
        public static final String AREA_SIGNATURE_FILE_NAME = "area.sign";
        public static final String CLIENTUPDATE_ORIGINAL_FILE_NAME = "clientUpdate.zip";
        public static final String CLIENTUPDATE_PROCESS_FILE_NAME = "clientUpdate.json";
        public static final String CLIENTUPDATE_SIGNATURE_FILE_NAME = "clientUpdate.sign";
        public static final String BIZ_RES_FILE_MAIN_PAGE = "index.html";
        public static final String BIZ_RES_FILE_EXTENSION = ".zip";
        public static final String BIZ_RES_IMG_EXTENSION = ".png";
        public static final String BIZ_RES_APK_EXTENSION = ".apk";
        public static final String BIZ_RES_PROPERTIES_EXTENSION = ".properties";
        public static final String CONF_API_PROCESS_FILE_NAME = "api.js";
        public static final String CONF_API_LEVEL_PROCESS_FILE_NAME = "apiLevel.json";
        public static final String CONF_NAVIGATION_PROCESS_FILE_NAME = "navigation.json";
    }

    public static class DynamicDialogConst {
        public static final String DYNAMIC_DIALOG_MSG_TYPE_TIP = "0";
        public static final String DYNAMIC_DIALOG_MSG_TYPE_WARNING = "1";
        public static final String DYNAMIC_DIALOG_MSG_TYPE_ERROR = "2";
        public static final String DYNAMIC_DIALOG_TIP_DEFAULT_TITLE = "提示";
        public static final String DYNAMIC_DIALOG_WARNING_DEFAULT_TITLE = "警告";
        public static final String DYNAMIC_DIALOG_ERROR_TITLE = "错误";
        public static final String DYNAMIC_DIALOG_DEFAULT_MSG = "";
        public static final String DYNAMIC_DIALOG_DEFAULT_BTN_NAME = "确定";
        public static final String DYNAMIC_DIALOG_CANCEL_BTN_NAME = "取消";
        public static final String DYNAMIC_DIALOG_OPEN_TYPE_DIALOG = "dialog";
        public static final String DYNAMIC_DIALOG_OPEN_TYPE_TOTAST = "totast";
        public static final String[] DYNAMIC_DIALOG_DEFAULT_BTN_NAME_ARRAY = new String[]{DYNAMIC_DIALOG_DEFAULT_BTN_NAME};
        public static final String[] DYNAMIC_DIALOG_DEFAULT_CONFIRM_BTN_NAME_ARRAY = new String[]{
                DYNAMIC_DIALOG_DEFAULT_BTN_NAME, DYNAMIC_DIALOG_CANCEL_BTN_NAME};
    }

    public static class DynamicBizIntentExtra {
        public static final String PAGE_FROM = "from";
        public static final String PAGE_TO = "to";
        public static final String PAGE_BIZ_CODE = "code";
        public static final String PAGE_NEED_BACK_HOME = "needBackHome";
        public static final String PAGE_IS_FULLSCREEN = "is.need.head";
        public static final String PAGE_IS_SHOW_BOTTOMTOOLBAR = "is.need.foot";
        public static final String PAGE_IS_SHOW_AREA = "is.need.area";
        public static final String PAGE_BIZ_API_LEVEL = "biz.api.level";
        public static final String PAGE_TITLE = "title";
        public static final String PAGE_ISUSEORIGINALVIEWPORT = "isUseOriginalViewPort";
    }

    public static class DynamicUmsUrlParam {
        public static final String PARAM_IS_FULL_SCREEN = "isFullscreen";
        public static final String PARAM_IS_SHOW_BOTTOM_TOOLBAR = "isShowBottomToolbar";
        public static final String PARAM_IS_SHOW_AREA = "isNeedArea";
        public static final String PARAM_BACK_NUM = "backNum";
        public static final String PARAM_UMS_OPEN = "umsOpenParam";
        public static final String PARAM_IS_NEED_LOGIN = "isNeedLogin";
    }

    public static class DynamicBizName {
        public static final String CONF_NAVIGATION_LIST = "CONF-NAVIGATION-LIST";
        public static final String CONF_API_LEVEL = "CONF-API-LEVEL";
        public static final String CONF_API = "CONF-API";
    }

    public static class BizUrlStartFlag {
        public static final String LOCAL_WEB = "ums://biz/";
        public static final String REMOTE_BIZ_WEB = "ums://http/";
        public static final String REMOTE_HTTP_WEB = "http://";
        public static final String REMOTE_HTTPS_WEB = "https://";
        public static final String NATIVE = "ums://page/";
        public static final String THIRD_BIZ_APP = "ums://page/dynamicApp";
    }

    public static class CHAR {
        public static final String AMPERSAND = "&";
        public static final String EQUAL = "=";
        public static final String TRUE = "true";
        public static final String FALSE = "false";
        public static final String SLASH = "/";
        public static final String COLON = ":";
        public static final String QUESTION_MARK = "?";
        public static final String COMMA = ",";
        public static final String DOT = ".";
        public static final String SEMICOLON = ";";
        public static final String EXCLAMATION_POINT = "!";
        public static final String SPACE = " ";
        public static final String EMPTY = "";
    }

    public static class ENCODING {
        public static final String UTF8 = "UTF-8";
    }

    public static class UmsOpenCore {
        // 动态加载内核版本
        public static final String UMS_OPEN_CORE = "UmsOpenCore/1.0.0";
    }

    public static class HttpHeaders {
        public static final String USER_AGENT = "User-Agent";
    }

    public static class DynamicBizHistory {

        // 内置的asset目录的文件夹的名字
        public static final String PRELOAD_PROFIX = "openpreload";

        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_PERSONAL_BIZ_CODES = "personalBizCodes";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_DISPLAY_BIZ_CODES = "displayBizCodes";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_DISPLAY_ADS_CODES = "displayAdsCodes";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_RECOMMEND_BIZ_CODES = "recommendBizCodes";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_SIGN_BIZLIST = "bizList.sign";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_SIGN_CATEGORY = "category.sign";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_SIGN_AREA = "area.sign";
        // 内置asset/PreInsConf.properties的json数据的内容
        public static final String PRELOAD_PROPERTIES_KEY_SIGN_CLIENT_UPDATE = "clientUpdate.sign";

        // 内置的推荐列表、个性化列表、可见资源包列表
        public static final String PRELOAD_PROPERTIES_FILE_PATH = PRELOAD_PROFIX + "/PreInsConf.properties";
        // 内置的默认的资源包的标准大小图标
        public static final String DEFAULT_BIZ_STD_ICON_FILE_PATH = PRELOAD_PROFIX + "/default_biz_std_icon.png";
        // 内置的默认的资源包的大尺寸的图标
        public static final String DEFAULT_BIZ_LARGE_ICON_FILE_PATH = PRELOAD_PROFIX + "/default_biz_large_icon.png";
        // 内置的默认的分类的标准的大小图标
        public static final String DEFAULT_CATEGORY_STD_ICON_FILE_PATH = PRELOAD_PROFIX + "/default_category_std_icon.png";
        // 内置的默认的分类的大尺寸的图标
        public static final String DEFAULT_CATEGORY_LARGE_ICON_FILE_PATH = PRELOAD_PROFIX + "/default_category_large_icon.png";

    }

    public static class UmsConnectionReqResErrorCode {
        public static final String REQ_ERR_CODE_SESSION_MACKEY_INVALID = "40004";
        public static final String RES_ERR_CODE_USER_INVALID = "2000";
        public static final String RES_ERR_CODE_SESSIN_NOT_FOUND = "2100";
        public static final String RES_STATUS_FRONT_ERR = "412";
    }

    public static class UmsConnectionReqResStatusCode {
        public static final int DEFAULT_EXCEPTION_NORESPONSE_STATUS = -1; //报文请求超时或者通讯错误时默认状态码
        public static final int REQUEST_NOT_ACCEPTABLE = 406; //无法使用请求的内容特性来响应请求
        public static final int REQUEST_PRECONDITION_FAILED = 412; //先决条件失败
        public static final int UNAUTHORIZED = 401; //授权失败
    }

}
