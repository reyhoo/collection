package com.chinaums.opensdk.exception;


public class ResourceDownloadException extends Exception {

	private static final long serialVersionUID = -1019231312850658441L;

	public ResourceDownloadException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ResourceDownloadException(String detailMessage) {
		super(detailMessage);
	}

}
