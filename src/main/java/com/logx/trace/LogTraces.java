package com.logx.trace;

import java.util.Vector;


public class LogTraces {
  private Vector<Log> logs;

  public LogTraces() {
    logs = new Vector<Log>();
  }

  public static LogTraces get() {
    return new LogTraces();
  }

  public void push(Log log) {
    logs.add(log);
  }

  public Vector<Log> getLogs() {
    return this.logs;
  }
}
