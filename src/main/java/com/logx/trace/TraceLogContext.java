package com.logx.trace;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.core.LogEvent;
//
//
//public class TraceLogContext {
//  private static final String DUMP = "dump";
//  private static final String TRACE_POJO = "trace_pojo";
//  private static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();
//  public static final Logger LOGGER = LogManager.getLogger(TraceLogContext.class);
//
//  public static HttpServletRequest get() {
//    return threadLocal.get();
//  }
//
//  public static void clearAll() {
//    threadLocal.remove();
//  }
//
//  public static void set(HttpServletRequest request) {
//    threadLocal.set(request);
//  }
//
//  public static boolean isTrace() {
//    if (get() != null) {
//      return get().getParameter(DUMP) != null;
//    }
//    return false;
//  }
//
//}
