package com.logx.tps.limit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @CreateTime 2016年9月19日 上午11:12:29
 * @author SHOUSHEN LUAN
 */
public abstract class AbstractTpsLimit extends CasVersion {
    /**
     * 周期时间（毫秒）
     */
    protected long period_ms;
    /**
     * 当前处理时间
     */
    protected AtomicLong times;
    protected volatile String msg = null;

    public AbstractTpsLimit(long period_ms) {
        super();
        times = new AtomicLong(System.currentTimeMillis());
        if (period_ms < 1) {
            throw new IllegalArgumentException("参数不合法period_ms:" + period_ms);
        }
        this.period_ms = period_ms;
    }

    protected boolean isExpire() {
        return System.currentTimeMillis() - times.get() > this.period_ms;
    }

    protected boolean isExpireAndUpdate() {
        long old = times.get();
        long old_version = super.getCurrVersion();
        if (isExpire()) {
            if (super.acquireCASLock(old_version)) {// CAS console
                if (times.compareAndSet(old, System.currentTimeMillis())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getMsg() {
        return msg;
    }

}
