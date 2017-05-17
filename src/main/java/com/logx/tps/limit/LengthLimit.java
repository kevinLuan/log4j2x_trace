package com.logx.tps.limit;

import java.util.concurrent.atomic.AtomicLong;

public class LengthLimit extends AbstractTpsLimit {
	/**
	 * 限制次数(一个周期内允许写入LOG的次数)
	 */
	private long length;
	private final AtomicLong counter = new AtomicLong();

	/**
	 * @param periodTimeMillis
	 *            限制周期
	 * @param limitSizeByte
	 *            限制size速率
	 * 
	 */
	public LengthLimit(final long length, final int periodTimeMillis) {
		super(periodTimeMillis);
		counter.set(0);
		if (length < 1) {
			throw new IllegalArgumentException("参数不合法length:" + length);
		}
		this.length = length;
	}

	/**
	 * 是否超过速率
	 */
	public boolean isOverstepTps(long len) {
		long old = counter.get();
		if (isExpireAndUpdate()) {
			long currVal = old;
			OK: while (true) {
				if (counter.compareAndSet(currVal, currVal - old)) {
					break OK;
				}
				currVal = counter.get();
			}
			return false;
		}
		if (length > counter.addAndGet(len)) {
			return false;
		} else {
			setMsg(counter.get());
			return true;// 超过阈值
		}
	}

	private void setMsg(long num) {
		this.msg = "写入日志" + period_ms + "ms周期内限制:" + length + "length,当前已到达:" + num + "length";
	}
}