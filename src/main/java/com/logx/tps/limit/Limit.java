package com.logx.tps.limit;

/**
 * 限制时间周期内写入次数及写入长度
 * 
 * @author kevin
 *
 */
public class Limit extends AbstractTpsLimit {
	final TpsLimit tps;
	final LengthLimit length;

	public Limit(long tpsLimit, long limitLength, int periodTimeMillis) {
		super(periodTimeMillis);
		tps = new TpsLimit(tpsLimit, periodTimeMillis);
		length = new LengthLimit(limitLength, periodTimeMillis);
	}

	public boolean isOverstepLimit(long len) {
		this.msg = null;
		boolean isLimit = false;
		if (length.isOverstepTps(len)) {
			isLimit = true;
			if (this.msg == null) {
				this.msg = length.getMsg();
			} else {
				this.msg += "------" + length.getMsg();
			}
		}

		if (tps.isOverstepTps()) {
			isLimit = true;
			if (this.msg == null) {
				this.msg = tps.getMsg();
			} else {
				this.msg += "------" + tps.getMsg();
			}
		}

		if (isLimit) {
			super.msg = this.msg;
		}
		return isLimit;
	}

	@Override
	public String getMsg() {
		return super.getMsg();
	}
}
