package com.logx.tps.monitor;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @CreateTime 2016年9月18日 下午10:33:23
 * @author SHOUSHEN LUAN
 */
public class LogMonitorFactory {
    private static LogMonitor logMonitor;
    /**
     * 报警策略控制
     */
    private static final ServerMonitorStrategy monitorStrategy = new ServerMonitorStrategy();

    static {
        logMonitor = loadClass();
    }

    /**
     * 加载报警处理器
     */
    private static LogMonitor loadClass() {
        try {
            ServiceLoader<LogMonitor> loader = ServiceLoader.load(LogMonitor.class);
            Iterator<LogMonitor> iterator = loader.iterator();
            LogMonitor monitor = null;
            while (iterator.hasNext()) {
                monitor = iterator.next();
                System.out.println("[MONITOR API]:" + monitor.getClass());
            }
            if (monitor == null) {
                System.out.println("Could not find the “" + LogMonitor.class.getName()
                        + "” The implementation of the class");
            }
            return monitor;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 发送报警消息
     */
    public static void sendMsg(String msg) {
        if (logMonitor != null) {
            if (monitorStrategy.isMonitor()) {
                logMonitor.sendMsg(msg);
            } else {
                // 频繁调用报警忽略处理
            }
        }
    }
}
