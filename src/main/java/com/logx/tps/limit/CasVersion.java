package com.logx.tps.limit;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @CreateTime 2016年9月19日 上午10:41:50
 * @author SHOUSHEN LUAN
 */
public abstract class CasVersion {
    private AtomicLong timesVersion = new AtomicLong(0);

    public CasVersion() {
        timesVersion.set(0);
    }

    protected long getCurrVersion() {
        if (timesVersion.get() < Long.MAX_VALUE) {
            return timesVersion.get();
        } else {
            resetTimeVersion();
            return timesVersion.get();
        }
    }

    private synchronized void resetTimeVersion() {
        if (timesVersion.get() == Long.MAX_VALUE) {
            timesVersion.set(0);
        }
    }

    protected long nextVersion() {
        long next = timesVersion.get();
        next = timesVersion.incrementAndGet();
        if (next == Long.MAX_VALUE) {
            resetTimeVersion();
            return timesVersion.incrementAndGet();
        }
        return next;
    }

    /**
     * 获取并发原型性锁
     */
    public boolean acquireCASLock(long old_version) {
        return old_version + 1 == nextVersion();
    }
}
