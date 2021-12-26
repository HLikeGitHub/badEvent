package com.yjy.web.comm.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author rwx
 * @info IPUtil
 * @mail aba121mail@qq.com
 */
public class IPUtil {

    public static String getReqIP(HttpServletRequest request) {
        String ip = "";
        try {
            ip = request.getHeader("x-forwarded-for");
            if (isIPNull(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (isIPNull(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (isIPNull(ip)) {
                ip = request.getRemoteAddr();
                if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                    // 根据网卡取本机配置的IP
                    ip = InetAddress.getLocalHost().getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length() = 15
            if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        } catch (Exception e) {
            ip = "";
            System.out.println("IPUtil.getIP() error, e="+e.getClass());
            e.printStackTrace();
        }
        return ip;
    }

    private static boolean isIPNull(String ip){
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}