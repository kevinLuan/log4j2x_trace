package com.logx.utils;

import org.apache.logging.log4j.core.LogEvent;

public class LogUtils {
	/**
	 * 获取日志长度
	 * 
	 * @param event
	 * @return
	 */
	public static long getLength(LogEvent event) {
		long length = 0L;
		if (event.getMessage().getFormattedMessage() != null) {
			length = event.getMessage().getFormattedMessage().length();
			if (event.getThrown() != null) {
				length += event.getThrown().toString().length();
			}
		} else {
			length = 4;// null 的字符串长度
		}
		return length;
	}

	public static long getLength(Object msg, Throwable t, Object... params) {
		long length = 0;
		if (msg == null) {
			length += 4;
		} else {
			length += msg.toString().length();
		}
		if (t != null) {
			length += t.toString().length();
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] != null) {
					length += params[i].toString().length();
				}
			}
		}
		return length;
	}
}
