package com.uidemo.bean;

/**
 * 
 * @author Administrator
 * 
 */
public class ListItemInfo {
	/**
	 * 标题资源id
	 */
	public int titleResId;

	/**
	 * icon资源id
	 */
	public int iconResId;

	/**
	 * item 绑定事件函数
	 */
	public Runnable function;
	/***
	 * 有的item右边会有字
	 */
	public String rightText;

}
