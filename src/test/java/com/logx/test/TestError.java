package com.logx.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestError {
	private static Logger ROOT = LoggerFactory.getLogger(TestError.class);
	private static Logger ASync = LoggerFactory.getLogger("ASync");
	private static Logger Sync = LoggerFactory.getLogger("Sync");

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 2000000; i++) {
//			ROOT.info("ROOT------------------------------------------------------>>>>>" + i);
			ASync.error("ASync.error===============>>>>" + i);
//			Sync.info("Sync.info+++++++++++++++++++" + i);
			ASync.error("xx", new IllegalArgumentException());
			ASync.info("a={},b={}", "AA", "BB");
		}
	}
}
