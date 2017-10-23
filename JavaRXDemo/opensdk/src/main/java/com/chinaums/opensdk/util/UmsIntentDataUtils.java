/**
 * 
 */
package com.chinaums.opensdk.util;

import java.io.Serializable;

import android.content.Intent;
import android.os.Parcelable;

/**
 * @author ethan
 * 
 */
public final class UmsIntentDataUtils {

	/**
	 * 
	 * @param intent
	 * @param name
	 * @return
	 */
	public static <T extends Parcelable> T getParcelableExtra(Intent intent,
			String name) {
		T t = null;
		try {
			if (intent == null || UmsStringUtils.isBlank(name))
				return t;
			t = intent.getParcelableExtra(name);
		} catch (Exception e) {
			UmsLog.e("", e);
		}
		return t;
	}

	/**
	 * 
	 * @param intent
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getSerializableExtra(
			Intent intent, String name) {
		T t = null;
		try {
			if (intent == null || UmsStringUtils.isBlank(name))
				return t;
			t = (T) intent.getSerializableExtra(name);
		} catch (Exception e) {
			UmsLog.e("", e);
		}
		return t;
	}

}
