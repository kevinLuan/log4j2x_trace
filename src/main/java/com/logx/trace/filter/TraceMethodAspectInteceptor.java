package com.logx.trace.filter;
//
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//
//
//@Aspect
//@Component
//public class TraceMethodAspectInteceptor {
//  public static final Logger LOGGER = LogManager.getLogger(TraceMethodAspectInteceptor.class);
//
//  @AfterReturning(value = "execution(* com.weidian.common.http.HTTPService.*(..))", returning = "result")
//  private void traceProxy(JoinPoint jp, Object result) {
//    traceLogger(jp, result);
//  }
//
//  public static void traceLogger(JoinPoint jp, Object result) {
//    if (TraceLogContext.isTrace()) {
//      MethodSignature methodSignature = (MethodSignature) jp.getSignature();
//      String[] names = methodSignature.getParameterNames();
//      Class<?> types[] = methodSignature.getParameterTypes();
//      StringBuilder builder = new StringBuilder();
//      for (int i = 0; i < jp.getArgs().length; i++) {
//        if (names != null) {
//          builder.append(names[i] + ":" + stringify(jp.getArgs()[i]) + "|");
//        } else {
//          builder.append(generatedKey(types, i) + ":" + stringify(jp.getArgs()[i]) + "|");
//        }
//      }
//      builder.append("result:" + stringify(result));
//      LOGGER.info("prefix:trace|" + builder.toString());
//    }
//  }
//
//  private static String generatedKey(Class<?>[] types, int i) {
//    return "param" + i + "_" + types[i].getSimpleName();
//  }
//
//  public static String stringify(Object obj) {
//    try {
//      String result = JacksonOperation.stringify(obj);
//      if (result.startsWith("\"") && result.endsWith("\"")) {
//        return result.substring(1, result.length() - 1);
//      }
//      return result;
//    } catch (Exception ex) {
//    }
//    return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
//  }
//}
