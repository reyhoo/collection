package com.chinaums.opensdk.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	public static final String REGEX_MOBILE = "^1(3[4-9]|47|5[0-2]|5[7-9]|78|8[23478])\\d{8}$";
	public static final String REGEX_TELECOM = "^1(33|53|77|8[019])\\d{8}$";
	public static final String REGEX_UNICOM = "^1(3[0-2]|45|5[56]|76|8[56])\\d{8}$";

	/**
	 * 0 元--->分 1 分--->元
	 * 
	 * @param yM
	 * @param type
	 * @return
	 */
	public static String moneyTran(String yM, int type) {
		String result = "0";
		double f = 0;
		double money = 0;

		try {
			money = Double.valueOf(yM);
		} catch (Exception e) {

		}

		try {
			if (type == 0) {
				// 元转分
				f = money * 100;
				result = new DecimalFormat("0").format(f);
			} else if (1 == type) {
				// 分转元
				f = money / 100;
				result = new DecimalFormat("0.00").format(f);
			} else if (3 == type) {
				f = money / 10000;
				result = new DecimalFormat("0.00").format(f);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static enum CONMUNICATION_TYPE {
		/**
		 * 移动
		 */
		CHINA_MOBILE_TYPE("通讯运营商：中国移动", "01"),
		/**
		 * 联通
		 */
		CHINA_UNICOM_TYPE("通讯运营商：中国联通", "02"),
		/**
		 * 电信
		 */
		CHINA_TELECOM_TYPE("通讯运营商：中国电信", "03"),
		/**
		 * 未知运营商
		 */
		UNKNOWN("未识别此号码", "-1");
		private String value;
		private String operatorId;

		private CONMUNICATION_TYPE(String value, String operatorId) {
			this.value = value;
			this.operatorId = operatorId;
		}

		public String getValue() {
			return value;
		}

		public String getOperatorId() {
			return operatorId;
		}
	}

	/**
	 * 运营商判断
	 * 
	 * @param mobile
	 * @return
	 */
	public static CONMUNICATION_TYPE getMobileType(String mobile) {
		// 是不是移动
		Pattern mobie = Pattern.compile(REGEX_MOBILE);
		Matcher mobiem = mobie.matcher(mobile);
		if (mobiem.matches()) {
			return CONMUNICATION_TYPE.CHINA_MOBILE_TYPE;
		} else {
			// 是不是电信
			Pattern telecom = Pattern.compile(REGEX_TELECOM);
			Matcher telecomm = telecom.matcher(mobile);
			if (telecomm.matches()) {
				return CONMUNICATION_TYPE.CHINA_TELECOM_TYPE;
			} else {
				// 是不是联通
				Pattern unicom = Pattern.compile(REGEX_UNICOM);
				Matcher unicomm = unicom.matcher(mobile);
				if (unicomm.matches()) {
					return CONMUNICATION_TYPE.CHINA_UNICOM_TYPE;
				}
			}
		}
		return CONMUNICATION_TYPE.UNKNOWN;
	}

	/**
	 * 拨打电话
	 * 
	 * @param context
	 * @param phone_number
	 */
	public static void giveACall(Context context, String phone_number) {
		Intent phoneIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
				+ phone_number));
		context.startActivity(phoneIntent);
	}

	/**
	 * 
	 * @author wxtang 2015-5-14下午11:21:21
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		return df.format(new Date());
	}

	/**
	 * 格式化银行卡号(不做屏蔽处理)
	 * 
	 * @param cardno
	 * @return
	 */
	public static String getFormatCardNoWithoutAsterisk(String cardno) {
		String card_no = cardno;
		if (!"".equals(cardno) && cardno != null && cardno.length() > 12) {
			card_no = cardno.substring(0, 4) + " " + cardno.substring(4, 8)
					+ " " + cardno.substring(8, 12) + " ";
			if (cardno.length() > 16) {
				card_no += cardno.substring(12, 16) + " "
						+ cardno.substring(16);
			} else {
				card_no += cardno.substring(12);
			}
		}
		return card_no;
	}

	/**
	 * 获取当前进程名
	 *
	 * @param context
	 * @return
	 */
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {

				return appProcess.processName;
			}
		}
		return null;
	}

	/**
	 * 获取当前APP的包名
	 *
	 * @param context
	 * @return
	 */
	public static String getAppPackageName(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			// 当前版本的包名
			String packageName = info.packageName;
			return packageName;
		} catch (PackageManager.NameNotFoundException e) {
			UmsLog.e("",e);
		}
		return "";
	}
}