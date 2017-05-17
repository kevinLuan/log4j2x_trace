package com.logx.test;

import com.logx.tps.limit.Limit;

public class TestLogTpsLimit {
	public static void main(String[] args) {
		Limit limit = new Limit(100, 200, 10);
		for (int i = 0; i < 500; i++) {
			System.out.println(i + "->" + limit.isOverstepLimit(2));
			if (i % 200 == 0) {
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
