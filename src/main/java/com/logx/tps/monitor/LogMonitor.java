package com.logx.tps.monitor;

/**
 * 定义报警API
 * 
 * @CreateTime 2016年9月8日 下午2:20:39
 * @author SHOUSHEN LUAN
 */
public interface LogMonitor {
    /**
     * 发送报警消息
     */
    public void sendMsg(String msg);

}
