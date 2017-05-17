package com.logx.tps.monitor;

import java.sql.Timestamp;

public class SimpleMonitorImpl implements LogMonitor {

    @Override
    public void sendMsg(String msg) {
        System.out.println(new Timestamp(System.currentTimeMillis()) + "-IP:XXX,SERVER:NAME,磁盘写入过快-报警短息:XXX" + msg);
    }

}
