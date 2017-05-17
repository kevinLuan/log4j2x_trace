package com.logx.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTpsLogger {
	private static Logger ROOT = LoggerFactory.getLogger("ROOT");
	private static Logger Sync = LoggerFactory.getLogger("Sync");
	private static Logger ASync = LoggerFactory.getLogger("ASync");

	@Test
	public void test_sync() throws InterruptedException {
		long start = System.currentTimeMillis();
		final CountDownLatch latch = new CountDownLatch(5);
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 100000; i++) {
						Sync.warn(i + " ----------------同步--------------------------aa");
					}
					latch.countDown();
				}
			}).start();
		}
		latch.await();
		System.out.println("同步log耗时: use time:" + (System.currentTimeMillis() - start));
	}

	@Test
	public void test_async() throws InterruptedException {
		long start = System.currentTimeMillis();
		final CountDownLatch latch = new CountDownLatch(5);
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 100000; i++) {
						ASync.warn(i + " ----------------异步--------------------------aa");
					}
					latch.countDown();
				}
			}).start();
		}
		latch.await();
		System.out.println("异步log耗时: use time:" + (System.currentTimeMillis() - start));
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			test1();
		}
	}

	public static void test1() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				long start = System.currentTimeMillis();
				for (int i = 0; i < 500000; i++) {
					ASync.warn(i + " ------------------------------------------aa");
					Sync.info(i + " ------------------------------------------TEST...");
					ROOT.warn(i + " ------------------------------------------aa");
				}
				System.out.println("use time:" + (System.currentTimeMillis() - start));
			}
		}).start();

	}

	@Test
	public void test_nullLog() {
		ROOT.info(null);
		ROOT.error(null);
		Throwable th = null;
		ROOT.error(null, th);
	}
}
