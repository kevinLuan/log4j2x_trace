package com.logx.tps.monitor;

import java.util.concurrent.atomic.AtomicLong;

public class ServerMonitorStrategy {
    // 最后报警时间
    private AtomicLong lastMonitor = new AtomicLong(0);
    private final long startTime;
    private LEVEL level = LEVEL.second;

    public static enum LEVEL {
        second(1, "1秒"), //
        five_second(5, "5秒"), //
        ten_second(10, "10秒"), //
        thirty_second(30, "30秒"), //
        minute(60, "一分钟"), //
        five_minute(60 * 5, "5分钟"), //
        ten_minute(60 * 10, "10分钟"), //
        thirty_minute(60 * 30, "30分钟"), //
        hour(60 * 60, "1小时");
        private int times;

        public LEVEL nextLevel() {
            switch (this) {
            case second:
                return five_second;
            case five_second:
                return ten_second;
            case ten_second:
                return thirty_second;
            case thirty_second:
                return minute;
            case minute:
                return five_minute;
            case five_minute:
                return ten_minute;
            case ten_minute:
                return thirty_minute;
            default:
                return hour;
            }
        }

        private LEVEL(int second_times, String desc) {
            this.times = second_times;
        }

        public int getTimes() {
            return times;
        }
    }

    public ServerMonitorStrategy() {
        this.startTime = System.currentTimeMillis();
    }

    private boolean isTimeout() {
        long oldVal = lastMonitor.get();
        long times = (System.currentTimeMillis() - oldVal) / 1000;
        if (times >= level.getTimes()) {
            if (lastMonitor.compareAndSet(oldVal, System.currentTimeMillis())) {
                level = level.nextLevel();
                return true;
            }
        }
        return false;
    }

    public boolean isMonitor() {
        if (isTimeout()) {
            return true;
        } else {
            return false;
        }
    }

    public long getShutdownTime() {
        return System.currentTimeMillis() - startTime;
    }

    public LEVEL getLevel() {
        return level;
    }
}
