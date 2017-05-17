package com.logx.tps.limit;

import java.util.concurrent.atomic.AtomicLong;

public class TpsLimit extends AbstractTpsLimit {

	/**
	 * 限制次数(一个周期内允许写入LOG的次数)
	 */
	private long tps;
	private final AtomicLong counter;

	/**
	 * @param periodTimeMillis
	 *            限制周期
	 * @param limitTPS
	 *            限制速率
	 * 
	 */
	public TpsLimit(final long tps, final int periodTimeMillis) {
		super(periodTimeMillis);
		counter = new AtomicLong();
		if (tps < 1) {
			throw new IllegalArgumentException("参数不合法tps:" + tps);
		}
		this.tps = tps;
	}

	/**
	 * 是否超过速率
	 */
	public boolean isOverstepTps() {
		long old = counter.get();
		if (isExpireAndUpdate()) {
			long currVal = old;
			OK: while (true) {
				if (counter.compareAndSet(currVal, currVal - old)) {
					break OK;
				}
				currVal = counter.get();
			}
		}
		if (tps >= counter.incrementAndGet()) {
			return false;
		} else {
			setMsg(counter.get());
			return true;// 超过阈值
		}
	}

	private void setMsg(long num) {
		super.msg = "写入日志" + period_ms + "ms周期内限制tps:" + tps + "次,当前已到达:" + num + "次";
	}
}