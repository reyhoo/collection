package com.chinaums.opensdk.cons;

public class DynamicProcessorType {

    // 环境变量相关
    public final static int GET_SYSTEM_INFO = 1001;// 获取系统信息
    public final static int GET_USER_INFORMATION = 1002;// 获取用户信息
    public final static int GET_LOCATION = 1003;// 获取地理定位信息
    public final static int GET_BOX_INFO = 1004;// 获取刷卡器信息
    public final static int GET_NET_STATE = 1005;// 获取网络信息
    public final static int GET_XMSMK_TOKEN = 1007;// 获取用户token

    // 框架服务相关
    public final static int CONSOLE_OUTPUT_EQUIPMENT = 2001; // 设备控制台输出
    public final static int REGISTER_EXTRA_API = 2002;// 注册自定义API
    public final static int NET_CONNECT = 2051; // 开放平台报文通讯

    // 页面跳转相关
    public final static int NAVIGATOR_PAGE_FORWARD = 3001;// 打开新页面
    public final static int NAVIGATOR_PAGE_BACK = 3002;// 回退页面
    public final static int NAVIGATOR_PAGE_PAY = 3003;// 打开支付页面
    public final static int NAVIGATOR_PAGE_ELECTRIC_VOUCHER = 3004;// 打开签购单查询页面
    public final static int NAVIGATOR_PAGE_BACK_3005 = 3005;// 回退页面  解决资源包单页问题的
    public final static int NAVIGATOR_PAGE_SHOWSHAREVIEW = 3006;// 打开分享界面
    public final static int NAVIGATOR_PAGE_PAY_CENTER = 3007;// 第三方打开支付页面
    public final static int NAVIGATOR_PAGE_RETURN = 3008;// 返回
    public final static int NAVIGATOR_PAGE_QUICK_PAY = 3101;// 打开POS通远程快捷支付

    // 数据存储相关
    public final static int PUBLIC_SET_RESULT = 4001;// 全局数据存储和获取
    public final static int CLIENT_HISTORY_DATA = 4002;// 客户端持久化数据查询、更新、删除

    // 界面相关
    public final static int NAVIGATOR_NOTIFICATION_ALERT = 5001; // 提示框，分带按钮和不带按钮2类
    public final static int SELECT_SHOW = 5002; // 显示选择列表
    public final static int SHOW_SIDESLIP_PAGE = 5003;// 显示侧滑界面【待开发】
    public final static int SHOW_TAB_PAGE = 5004;// 显示tab页面【待开发】
    public final static int SET_TITLE = 5005;// 设置资源包activity的标题

    // 外设相关
    public static final int SCAN_BAR_CODE = 6001;// 扫描二维码
    public static final int SWIPECARD = 6011;// 刷卡获得磁道信息
    public static final int GET_OFFLINE_DATA = 6012;// 脱机数据查询
    public static final int DO_APDU_ORDER = 6013;// 行业卡相关-apdu上电、下电、发送apdu
    public static final int BOX_PRINT = 6021;// 打印纸质签购单
    public static final int TEXT_TO_SPEECH = 6032;// 语音合成
    public static final int SCAN_IBEACON = 6041;// 扫描ibeacon

    // 系统相关
    public final static int SYSTEM_OPEN_WEB_PAGE = 7001;// 调用浏览器组件打开网页
    public final static int SYSTEM_OPEN_TEL = 7002;// 调用拨号盘组件打开拨号盘
    public static final int SYSTEM_GET_PHONE_NUMBER = 7003; // 获得通讯录中某一个人的号码
    public final static int SYSTEM_OPEN_CAMERA = 7004;// 获得图像媒体文件
    public final static int SYSTEM_GET_FILE = 7005;// 获取本地文件
    public final static int SYSTEM_ACTIVE_LOCATION = 7006;// 主动获取地理定位信息
    public final static int SYSTEM_GET_SOUND_STATE = 7007;// 获取静音状态
    public final static int SYSTEM_REFRESH_WEBVIEW = 7008;// 刷新网页
    public final static int SYSTEM_COPY = 7009;// 复制到剪切板

    // 业务相关
    public final static int GET_BANKCARD_INFO = 8001;// 根据卡号或磁道号获取卡片详情
    public final static int UPLOAD_OFFLINE_DATA = 8002;// 脱机数据上送
    public final static int UPLOAD_FILE = 8003;// 文件传输
    public final static int UMENG_EVENT = 8004;// 友盟统计
    public final static int GET_ENCRYPT_PIN = 8021;// 加解密相关-密码键盘

}
