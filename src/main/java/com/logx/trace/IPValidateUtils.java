package com.logx.trace;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.weidian.open.json.utils.StringUtils;


public class IPValidateUtils {
  private List<String> white_ip_list;

  public IPValidateUtils(List<String> white_ip_list) {
    this.white_ip_list = white_ip_list;
  }

  public static IPValidateUtils getInstance(List<String> white_ip_list) {
    return new IPValidateUtils(white_ip_list);
  }

  public boolean validateIPV4(String ip) {
    for (int i = 0; i < white_ip_list.size(); i++) {
      if (matches(white_ip_list.get(i), ip)) {
        return true;
      }
    }
    return false;
  }

  private boolean matches(String regex_ip, String ip) {
    if (regex_ip.equals(ip)) {
      return true;
    } else {
      String[] regexSegments = regex_ip.split("\\.");
      String[] ipSegments = ip.split("\\.");
      if (regexSegments.length == regexSegments.length) {
        for (int i = 0; i < regexSegments.length; i++) {
          String segment = regexSegments[i];
          String ipSegment = ipSegments[i];
          if (segment.equals("*")) {
            continue;
          } else if (segment.equals(ipSegment)) {
            continue;
          } else {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  public boolean isIPV4(String ip) {
    if (ip != null && ip.length() > 0) {
      String[] strs = ip.split("\\.");
      if (strs.length == 4) {
        return true;
      }
    }
    return false;
  }

  public boolean validateIP(HttpServletRequest httpRequest) {
    String ip = getIPAddress(httpRequest);
    return validateIPV4(ip);
  }

  public String getIPAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip != null) {
      String[] ips = ip.split(",");
      ip = ips[0];
    }
    if (StringUtils.isBlank(ip)) {
      ip = request.getHeader("x-real-ip");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

}
