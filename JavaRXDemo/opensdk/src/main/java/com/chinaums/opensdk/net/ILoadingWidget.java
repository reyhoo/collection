package com.chinaums.opensdk.net;

import android.content.Context;

/**
 * 进度条的抽象接口。
 */
public interface ILoadingWidget {
	public void showLoading(Context context);

	public void hideLoading(Context context);
}
